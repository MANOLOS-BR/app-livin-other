package com.manoloscorp.livinother.view.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.ScrollView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.facebook.shimmer.ShimmerFrameLayout
import com.manoloscorp.livinother.BuildConfig
import com.manoloscorp.livinother.R
import com.manoloscorp.livinother.service.constants.LivinOtherConstants.CAMERA.ID_PHOTO_PICKER_FROM_CAMERA
import com.manoloscorp.livinother.service.constants.LivinOtherConstants.CAMERA.REQUEST_CODE_TAKE_FROM_CAMERA
import com.manoloscorp.livinother.service.model.Profile
import com.manoloscorp.livinother.view.activity.LoginActivity
import com.manoloscorp.livinother.viewmodel.ProfileViewModel
import com.mikhaellopez.circularimageview.CircularImageView
import com.soundcloud.android.crop.Crop
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var mViewModel: ProfileViewModel

    private lateinit var mContext: Context

    private lateinit var mShimmerLayout: ShimmerFrameLayout
    private lateinit var mScrollView: ScrollView
    private lateinit var mImageView: CircularImageView

    private lateinit var mImageCaptureUri: Uri
    private lateinit var rotatedBitmap: Bitmap
    private lateinit var photoFile: File

    private var isTakenFromCamera = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        setView(root)

        checkPermissions()

        loadSnap()

        observe()

        setVisibility(mShimmerLayout)

        mShimmerLayout.startShimmerAnimation()

        mViewModel.getUserId()

        return root
    }

    private fun setVisibility(param: View) {
        when (param.id) {
            R.id.shimmer_layout -> {
                mShimmerLayout.visibility = View.VISIBLE
                mScrollView.visibility = View.GONE
            }
            R.id.scrollView_profile -> {
                mShimmerLayout.visibility = View.GONE
                mScrollView.visibility = View.VISIBLE
            }
        }
    }

    private fun setView(root: View) {
        mImageView = root.findViewById(R.id.image_user_profile)
        mScrollView = root.findViewById(R.id.scrollView_profile)
        mShimmerLayout = root.findViewById(R.id.shimmer_layout)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_take_photo -> {
                onChangePhotoClicked(button_take_photo)
            }

            R.id.btn_logout -> {
                mViewModel.logout()

                startActivity(Intent(context, LoginActivity::class.java))
                activity?.finish()
            }
        }
    }

    private fun removeClick() {
        radioButton_receiver.isClickable = false
        radioButton_donor.isClickable = false
    }

    override fun onAttach(context: Context) {
        this.mContext = context;
        super.onAttach(context)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED
            && grantResults[1] == PackageManager.PERMISSION_GRANTED
        ) {
        } else if (grantResults[0] == PackageManager.PERMISSION_DENIED
            || grantResults[1] == PackageManager.PERMISSION_DENIED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
                    || shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ) {
                    val builder =
                        AlertDialog.Builder(mContext)
                    builder.setMessage("Essa permissão é importante para o aplicativo.")
                        .setTitle("É necessária uma permissão importante")
                    builder.setPositiveButton(
                        "OK"
                    ) { dialog, id ->
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                            ), 0
                        )
                    }
                    builder.show()
                } else {
                    //Never ask again and handle your app without permission.
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            REQUEST_CODE_TAKE_FROM_CAMERA -> {
                val rotatedBitmap: Bitmap = imageOrientationValidator(photoFile)
                try {
                    val fOut = FileOutputStream(photoFile)
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                    fOut.flush()
                    fOut.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                mImageCaptureUri = FileProvider.getUriForFile(
                    mContext,
                    BuildConfig.APPLICATION_ID,
                    photoFile
                )
                beginCrop(mImageCaptureUri)
            }
            Crop.REQUEST_CROP ->
                if (data != null) {
                    handleCrop(resultCode, data)
                }
        }
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        if (mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
            || mContext.checkSelfPermission(Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ), 0
            )
        }
    }

    private fun imageOrientationValidator(photoFile: File): Bitmap {
        val ei: ExifInterface
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(
                mContext.contentResolver, FileProvider.getUriForFile(
                    mContext,
                    BuildConfig.APPLICATION_ID,
                    photoFile
                )
            )
            ei = ExifInterface(photoFile.absolutePath)
            val orientation = ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )

            rotatedBitmap = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
                ExifInterface.ORIENTATION_NORMAL -> bitmap
                else -> bitmap
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return rotatedBitmap
    }


    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }

    private fun beginCrop(source: Uri) {
        if (photoFile != null) {
            val destination = FileProvider.getUriForFile(
                mContext,
                BuildConfig.APPLICATION_ID,
                photoFile!!
            )
            Log.d("URI: ", destination.toString())
            Crop.of(source, destination).asSquare().start(mContext, this)
        }
    }

    private fun handleCrop(resultCode: Int, result: Intent) {
        if (resultCode == Activity.RESULT_OK) {
            val uri: Uri = Crop.getOutput(result)
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(mContext.contentResolver, uri)
                mImageView.setImageBitmap(bitmap)
                onSaveClicked()
            } catch (e: Exception) {
                Log.d("Error", "error")
            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(mContext, Crop.getError(result).toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun onPhotoPickerItemSelected(item: Int) {
        val intent: Intent
        when (item) {
            ID_PHOTO_PICKER_FROM_CAMERA -> {
                intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try {
                    photoFile = createImageFile()
                } catch (ex: IOException) {

                    ex.printStackTrace()
                }
                if (photoFile != null) {
                    val photoURI = FileProvider.getUriForFile(
                        mContext,
                        BuildConfig.APPLICATION_ID,
                        photoFile
                    )
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                }
                try {
                    startActivityForResult(
                        intent,
                        REQUEST_CODE_TAKE_FROM_CAMERA
                    )
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
                isTakenFromCamera = true
            }
            else -> return
        }
    }

    private fun showDialogPhoto(context: Context) {
        val options =
            arrayOf<CharSequence>("Tirar uma foto", "Cancelar")

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Escolha sua foto de perfil")

        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Tirar uma foto" -> {
                    onPhotoPickerItemSelected(item)
                }
                options[item] == "Cancelar" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    private fun onSaveClicked() {
        saveSnap()
        Toast.makeText(
            mContext,
            getString(R.string.ui_profile_toast_save_text),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun loadSnap() {
        try {
            val fis: FileInputStream =
                mContext.openFileInput(getString(R.string.profile_photo_file_name))
            val bitmap = BitmapFactory.decodeStream(fis)
            mImageView.setImageBitmap(bitmap)
            fis.close()
        } catch (e: IOException) {
            mImageView.setImageResource(R.drawable.blank_profile_picture)
        }
    }

    private fun saveSnap() {
        mImageView.buildDrawingCache()
        val bmap = mImageView.drawingCache
        try {
            val fos: FileOutputStream = mContext.openFileOutput(
                getString(R.string.profile_photo_file_name),
                Context.MODE_PRIVATE
            )
            bmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        }
    }

    private fun onChangePhotoClicked(buttonTakePhoto: ImageButton) {
        showDialogPhoto(mContext)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File? =
            mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
    }

    private fun onRadioButtonClicked(view: View) {

        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radioButton_donor ->
                    if (checked) {
                        radioButton_donor.setTextColor(resources.getColor(R.color.white, null))
                        radioButton_receiver.setTextColor(
                            resources.getColor(R.color.dark_grey, null)
                        )
                    }
                R.id.radioButton_receiver ->
                    if (checked) {
                        radioButton_donor.setTextColor(resources.getColor(R.color.dark_grey, null))
                        radioButton_receiver.setTextColor(resources.getColor(R.color.white, null))
                    }
            }
        }
    }

    private fun observe() {

        mViewModel.idUser.observe(viewLifecycleOwner, Observer {
            if (it != null && it > 0) {
                mViewModel.getProfile(it)
            }
        })

        mViewModel.profile.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                setProfileValues(it)
            }

            mShimmerLayout.stopShimmerAnimation()
            setVisibility(mScrollView)
        })

        mViewModel.validation.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it.errorMessage(), Toast.LENGTH_SHORT).show()
        })
    }

    private fun setProfileValues(profile: Profile) {
        text_user_email.text = profile.name
        text_name.text = profile.name
        text_email.text = profile.email

        formatDateBirthday(profile)

        text_genre.text = profile.genre
        text_eatingHabit.text = profile.eatingHabit

        setProfileType(profile.userType)

        text_weight.text = formatWeight(profile.medicalHistory.weight.toString())
        text_height.text = formatHeight(profile.medicalHistory.height.toString())

        checkbox_chemical_addict.isChecked = profile.medicalHistory.drugAddict
        checkbox_alcoholic.isChecked = profile.medicalHistory.alcoholConsumption
        checkbox_communicable_diseases.isChecked = profile.medicalHistory.communicableDisease
        checkbox_degenerative_diseases.isChecked = profile.medicalHistory.degenerativeDisease
        checkbox_practice_physical_activities.isChecked =
            profile.medicalHistory.practicePhysicalActivity
    }

    private fun formatDateBirthday(profile: Profile) {
        val dateStr = profile.birthDate
        val date = SimpleDateFormat("yyyy-MM-dd").parse(dateStr)
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        sdf.timeZone = TimeZone.getTimeZone("CET")
        val dateText = sdf.format(date)
        text_date_birthday.text = dateText
    }

    private fun formatWeight(value: String): String = "$value kg"

    private fun formatHeight(value: String): String = "$value m"

    private fun setListeners() {
        button_take_photo.setOnClickListener(this)
        radioGroup_user_type.setOnClickListener(null)
        radioButton_donor.setOnClickListener(null)
        radioButton_receiver.setOnClickListener(this)
        btn_logout.setOnClickListener(this)
        removeClick()
    }

    private fun setProfileType(value: String) {
        if (value == "DOADOR") {
            radioButton_donor.isChecked = true
            onRadioButtonClicked(radioButton_donor)
        } else {
            radioButton_receiver.isChecked = true
            onRadioButtonClicked(radioButton_receiver)
        }
    }

}