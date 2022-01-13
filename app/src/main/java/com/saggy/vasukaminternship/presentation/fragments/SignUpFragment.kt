package com.saggy.vasukaminternship.presentation.fragments

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.navigation.findNavController
import com.google.firebase.database.FirebaseDatabase
import com.saggy.vasukaminternship.R
import com.saggy.vasukaminternship.databinding.FragmentSignUpBinding
import com.saggy.vasukaminternship.presentation.activity.home.Home_Activity
import com.saggy.vasukaminternship.utils.FireBaseUtils.firebaseAuth
import com.saggy.vasukaminternship.utils.FireBaseUtils.firebaseDatabase
import com.saggy.vasukaminternship.utils.FireBaseUtils.firebaseUser
import com.saggy.vasukaminternship.utils.Helper
import com.saggy.vasukaminternship.utils.displayToast
import java.util.*


class SignUpFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var _binding: FragmentSignUpBinding
    val binding
        get() = _binding

    var uid: String = ""
    var age: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dob.isFocusable = false
        binding.dob.isEnabled = false

        if (firebaseUser != null) {
            uid = firebaseUser.uid
        }


        binding.errorText.visibility = View.GONE

        binding.backButton.setOnClickListener {

        }

        binding.login.setOnClickListener {
            it.findNavController()
                .navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
        }

        binding.calender.setOnClickListener {
            showDatePickerDialog()
        }

        binding.signup.setOnClickListener {

            if (binding.username.text.toString().isEmpty()) {
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Enter Username to continue."
            } else if (binding.name.text.toString().isEmpty()) {
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Enter your Name to continue."
            } else if (binding.email.text.toString().isEmpty()) {
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Enter Email id to continue."
            } else if (!Helper.isValidEmail(binding.email.text.toString())) {
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Enter Vaild Email."
            } else if (binding.phoneNumber.text.isEmpty()) {
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Enter Phone Number to continue"
            } else if (binding.dob.text.toString().isEmpty()) {
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Enter Date of birth to continue"
            } else if (age < 16) {
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "You age should be greater than 16 years to sign up"
            } else {
                binding.errorText.visibility = View.GONE

                val number =
                    binding.ccp.selectedCountryCodeWithPlus + binding.phoneNumber.text.toString()
                val loginDataBundle = Bundle()
                loginDataBundle.putString("PhoneNumber", number)
                loginDataBundle.putString("From", "SignUp")
                loginDataBundle.putString("name", binding.name.text.toString())
                loginDataBundle.putString("emailId", binding.email.text.toString())
                loginDataBundle.putString("username", binding.username.text.toString())
                loginDataBundle.putString("dateOfBirth", binding.dob.text.toString())
                it.findNavController()
                    .navigate(R.id.action_signUpFragment_to_otpVerifyFragment, loginDataBundle)

            }
        }
    }


    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            context!!,
            this,
            Calendar.getInstance()[Calendar.YEAR],
            Calendar.getInstance()[Calendar.MONTH],
            Calendar.getInstance()[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.show()
    }

    override fun onDateSet(datePicker: DatePicker, i: Int, i1: Int, i2: Int) {
        binding.dob.setText(i2.toString() + "/" + (i1 + 1) + "/" + i)
        val currentDate = Calendar.getInstance().get(Calendar.YEAR);
        age = currentDate - datePicker.year
    }

}
