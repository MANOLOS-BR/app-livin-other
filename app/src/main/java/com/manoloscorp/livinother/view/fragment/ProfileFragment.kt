package com.manoloscorp.livinother.view.fragment

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.ScrollView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.facebook.shimmer.ShimmerFrameLayout
import com.manoloscorp.livinother.R
import com.manoloscorp.livinother.service.model.Profile
import com.manoloscorp.livinother.view.activity.LoginActivity
import com.manoloscorp.livinother.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var mViewModel: ProfileViewModel

    private lateinit var mContext: Context

    private lateinit var mShimmerLayout: ShimmerFrameLayout
    private lateinit var mScrollView: ScrollView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        observe()

        mScrollView = root.findViewById(R.id.scrollView)

        mShimmerLayout = root.findViewById(R.id.shimmer_layout)

        mShimmerLayout.visibility = View.VISIBLE
        mScrollView.visibility = View.GONE

        mShimmerLayout.startShimmerAnimation()

        mViewModel.getUserId()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()

    }

    override fun onAttach(context: Context) {
        this.mContext = context;
        super.onAttach(context)
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

            mShimmerLayout.visibility = View.GONE
            mScrollView.visibility = View.VISIBLE

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

    /**
     * Inicializa os eventos de click
     */
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

    override fun onClick(view: View) {

        when (view.id) {
            R.id.button_take_photo -> {
                selectImage(mContext)
            }

            R.id.btn_logout -> {
                mViewModel.logout()

                startActivity(Intent(context, LoginActivity::class.java))
                activity?.finish()
            }
        }
    }

    private fun selectImage(context: Context) {
        val options =
            arrayOf<CharSequence>("Tirar uma foto", "Escolher da galeria", "Cancelar")

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Escolha sua foto de perfil")

        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Tirar uma foto" -> {
                    val takePicture =
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePicture, 0)
                }
                options[item] == "Escolher da galeria" -> {
                    val pickPhoto = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    startActivityForResult(pickPhoto, 1)
                }
                options[item] == "Cancelar" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == RESULT_OK && data != null) {
                    val selectedImage = data.extras!!["data"] as Bitmap?
                    image_user_profile.setImageBitmap(selectedImage)
                }
                1 -> if (resultCode == RESULT_OK && data != null) {
                    val selectedImage = data.data
                    val filePathColumn =
                        arrayOf(MediaStore.Images.Media.DATA)
                    if (selectedImage != null) {

                        val cursor: Cursor? = mContext.contentResolver.query(
                            selectedImage,
                            filePathColumn, null, null, null
                        )
                        if (cursor != null) {
                            cursor.moveToFirst()
                            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                            val picturePath = cursor.getString(columnIndex)
                            image_user_profile.setImageBitmap(BitmapFactory.decodeFile(picturePath))
                            cursor.close()
                        }
                    }
                }
            }
        }
    }

    private fun removeClick() {
        radioButton_receiver.isClickable = false
        radioButton_donor.isClickable = false
    }
}