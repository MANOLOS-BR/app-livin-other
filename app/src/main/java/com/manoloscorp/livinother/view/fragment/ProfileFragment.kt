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
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.manoloscorp.livinother.utils.FormatValues
import com.manoloscorp.livinother.view.activity.LoginActivity
import com.manoloscorp.livinother.viewmodel.ProfileViewModel
import com.mikhaellopez.circularimageview.CircularImageView
import com.soundcloud.android.crop.Crop
import kotlinx.android.synthetic.main.fragment_health_register.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.radioButton_donor
import kotlinx.android.synthetic.main.fragment_profile.radioButton_receiver
import kotlinx.android.synthetic.main.fragment_profile.radioGroup_user_type
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
    private lateinit var mEditProfile: ConstraintLayout
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
                mEditProfile.visibility = View.GONE
            }
            R.id.scrollView_profile -> {
                mShimmerLayout.visibility = View.GONE
                mEditProfile.visibility = View.GONE
                mScrollView.visibility = View.VISIBLE
            }
            R.id.container_edit_profile -> {
                mEditProfile.visibility = View.VISIBLE
                mScrollView.visibility = View.GONE
            }
        }
    }

    private fun setView(root: View) {
        mImageView = root.findViewById(R.id.image_user_profile)
        mScrollView = root.findViewById(R.id.scrollView_profile)
        mShimmerLayout = root.findViewById(R.id.shimmer_layout)
        mEditProfile = root.findViewById(R.id.container_edit_profile)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_take_photo -> {
                onChangePhotoClicked()
            }

            R.id.btn_logout -> {
                mViewModel.logout()

                startActivity(Intent(context, LoginActivity::class.java))
                activity?.finish()
            }

            R.id.button_open_edit -> {
                val user = mViewModel.getUserData()

                setupSpinnerGenre(dropdownGenre_edit)
                setupSpinnerEatingHabit(dropdown_eatingHabit_edit)
                setUserEdit(user)

                setVisibility(container_edit_profile)
            }

            R.id.btn_edit -> {
                val user = getUserEditValues(mViewModel.getUserData())
                mViewModel.updateProfile(1, user)
            }

            R.id.radioButton_donor_edit -> {
                onRadioButtonClicked(view)
            }
            R.id.radioButton_receiver_edit -> {
                onRadioButtonClicked(view)
            }

            R.id.back_arrow_to_profile -> {
                mEditProfile.visibility = View.GONE
                mScrollView.visibility = View.VISIBLE
            }
        }
    }

    private fun getUserEditValues(userData: Profile): Profile {
        val user: Profile = userData

        user.name = text_edit_user_edit.text.toString()
        user.email = text_edit_profile_email_edit.text.toString()

        user.birthDate = formatDateBirthdayToService()

        user.medicalHistory.height = text_edit_height_edit.text.toString().toDouble()
        user.medicalHistory.weight = text_edit_weight_edit.text.toString().toDouble()

        user.eatingHabit = dropdown_eatingHabit_edit.selectedItem.toString()
        user.genre = dropdownGenre_edit.selectedItem.toString()

        user.userType = getUserType()

        user.medicalHistory.drugAddict = checkBox_chemicalAddict_edit.isChecked
        user.medicalHistory.alcoholConsumption = checkBox_alcoholic_edit.isChecked
        user.medicalHistory.communicableDisease = checkBox_communicableDiseases_edit.isChecked
        user.medicalHistory.degenerativeDisease = checkBox_degenerativeDiseases_edit.isChecked
        user.medicalHistory.practicePhysicalActivity =
            checkBox_practicePhysicalActivities_edit.isChecked

        return user
    }

    private fun getUserType(): String {
        return if (radioButton_donor_edit.isChecked) {
            radioButton_donor_edit.text.toString()
        } else {
            radioButton_receiver_edit.text.toString()
        }

//        return if (radioButton_donor.isSelected) {
//            radioButton_donor.text.toString()
//        } else {
//            radioButton_receiver.text.toString()
//        }
    }

    private fun formatDateBirthdayToService(): String {
        val birthday = text_edit_birthday_edit.text.toString()
        return FormatValues.formatBirthdayDate(birthday).toString()
    }

    private fun setUserEdit(profile: Profile) {
        text_edit_user_edit.setText(profile.name)
        text_edit_profile_email_edit.setText(profile.email)

        text_edit_birthday_edit.setText(formatDateBirthday(profile))

        setDropdownGenreEditValue(profile.genre)
        setDropdownEatingHabitEditValue(profile.eatingHabit)

        setProfileType(profile.userType, true)

        text_edit_height_edit.setText(profile.medicalHistory.height.toString())
        text_edit_weight_edit.setText(profile.medicalHistory.weight.toString())

        checkBox_chemicalAddict_edit.isChecked = profile.medicalHistory.drugAddict
        checkBox_alcoholic_edit.isChecked = profile.medicalHistory.alcoholConsumption
        checkBox_communicableDiseases_edit.isChecked = profile.medicalHistory.communicableDisease
        checkBox_degenerativeDiseases_edit.isChecked = profile.medicalHistory.degenerativeDisease
        checkBox_practicePhysicalActivities_edit.isChecked =
            profile.medicalHistory.practicePhysicalActivity
    }

    private fun setDropdownEatingHabitEditValue(param: String) {
        when (param) {
            "Carnivoro" -> dropdown_eatingHabit_edit.setSelection(1)
            "Vegetariano" -> dropdown_eatingHabit_edit.setSelection(2)
            "Vegano" -> dropdown_eatingHabit_edit.setSelection(3)
            "Outros" -> dropdown_eatingHabit_edit.setSelection(4)
        }

    }

    private fun setDropdownGenreEditValue(param: String) {
        when (param) {
            "Feminino" -> dropdownGenre_edit.setSelection(1)
            "Masculino" -> dropdownGenre_edit.setSelection(2)
            "Outros" -> dropdownGenre_edit.setSelection(3)
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

    private fun onChangePhotoClicked() {
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
                R.id.radioButton_donor_edit -> {
                    if (checked) {
                        radioButton_donor_edit.setTextColor(resources.getColor(R.color.white, null))
                        radioButton_receiver_edit.setTextColor(
                            resources.getColor(R.color.dark_grey, null)
                        )
                    }
                }

                R.id.radioButton_receiver ->
                    if (checked) {
                        radioButton_donor.setTextColor(resources.getColor(R.color.dark_grey, null))
                        radioButton_receiver.setTextColor(resources.getColor(R.color.white, null))
                    }

                R.id.radioButton_receiver_edit -> {
                    if (checked) {
                        radioButton_donor_edit.setTextColor(
                            resources.getColor(
                                R.color.dark_grey,
                                null
                            )
                        )
                        radioButton_receiver_edit.setTextColor(
                            resources.getColor(
                                R.color.white,
                                null
                            )
                        )
                    }
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

        mViewModel.updateProfile.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                setProfileValues(it)
            }
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

        text_date_birthday.text = formatDateBirthday(profile)

        text_genre.text = profile.genre
        text_eatingHabit.text = profile.eatingHabit

        setProfileType(profile.userType, false)

        text_weight.text = formatWeight(profile.medicalHistory.weight.toString())
        text_height.text = formatHeight(profile.medicalHistory.height.toString())

        checkbox_chemical_addict.isChecked = profile.medicalHistory.drugAddict
        checkbox_alcoholic.isChecked = profile.medicalHistory.alcoholConsumption
        checkbox_communicable_diseases.isChecked = profile.medicalHistory.communicableDisease
        checkbox_degenerative_diseases.isChecked = profile.medicalHistory.degenerativeDisease
        checkbox_practice_physical_activities.isChecked =
            profile.medicalHistory.practicePhysicalActivity
    }

    private fun formatDateBirthday(profile: Profile): String {
        val dateStr = profile.birthDate
        val date = SimpleDateFormat("yyyy-MM-dd").parse(dateStr)
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        sdf.timeZone = TimeZone.getTimeZone("CET")
        return sdf.format(date)
    }

    private fun formatWeight(value: String): String = "$value kg"

    private fun formatHeight(value: String): String = "$value m"

    private fun setListeners() {
        button_open_edit.setOnClickListener(this)
        btn_edit.setOnClickListener(this)
        back_arrow_to_profile.setOnClickListener(this)

        button_take_photo.setOnClickListener(this)

        radioGroup_user_type.setOnClickListener(null)
        radioButton_donor.setOnClickListener(null)
        radioButton_receiver.setOnClickListener(null)

        radioGroup_user_type_edit.setOnClickListener(this)
        radioButton_donor_edit.setOnClickListener(this)
        radioButton_receiver_edit.setOnClickListener(this)

        btn_logout.setOnClickListener(this)

        removeClick()
    }

    private fun setProfileType(value: String, isEdit: Boolean) {
        if (!isEdit) {
            if (value == "DOADOR") {
                radioButton_donor.isChecked = true
                onRadioButtonClicked(radioButton_donor)
            } else {
                radioButton_receiver.isChecked = true
                onRadioButtonClicked(radioButton_receiver)
            }
        } else {
            if (value == "DOADOR") {
                radioButton_donor_edit.isChecked = true
                onRadioButtonClicked(radioButton_donor_edit)
            } else {
                radioButton_receiver_edit.isChecked = true
                onRadioButtonClicked(radioButton_receiver_edit)
            }
        }

    }

    private fun setupSpinnerEatingHabit(view: View) {
        val spinner = view.findViewById<Spinner>(R.id.dropdown_eatingHabit_edit)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.eating_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }

    private fun setupSpinnerGenre(view: View) {
        val spinner = view.findViewById<Spinner>(R.id.dropdownGenre_edit)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.genre_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }

}