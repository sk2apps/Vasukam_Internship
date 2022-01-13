package com.saggy.vasukaminternship.presentation.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.saggy.vasukaminternship.R
import com.saggy.vasukaminternship.data.model.user.ProfileInfo
import com.saggy.vasukaminternship.databinding.FragmentOtpVerifyBinding
import com.saggy.vasukaminternship.presentation.activity.home.Home_Activity
import com.saggy.vasukaminternship.utils.FireBaseUtils
import com.saggy.vasukaminternship.utils.displayToast
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class OtpVerifyFragment : Fragment() {

    private lateinit var _binding: FragmentOtpVerifyBinding
    val binding
        get() = _binding

    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var resendingToken: PhoneAuthProvider.ForceResendingToken

    lateinit var phoneNumber: String
    var temp = ""
    var otpId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtpVerifyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        phoneNumber = arguments?.getString("PhoneNumber").toString()
        temp = arguments?.getString("From").toString()


        binding.errorText.text = "Sending OTP"
        binding.errorText.visibility = View.VISIBLE

        binding.resend.setOnClickListener {
            binding.errorText.visibility = View.GONE
            sendOTP()
        }

        binding.backButton.setOnClickListener {
            if (temp == "Login") {
                it.findNavController().navigate(R.id.action_otpVerifyFragment_to_loginFragment)
            } else if (temp == "SignUp") {
                it.findNavController().navigate(R.id.action_otpVerifyFragment_to_signUpFragment)
            }
        }

        binding.login.setOnClickListener {
            it.findNavController().navigate(R.id.action_otpVerifyFragment_to_loginFragment)
        }

        binding.done.setOnClickListener {

            if (binding.otp.text.toString().isEmpty()) {
                Toast.makeText(context, "Enter your OTP", Toast.LENGTH_SHORT).show()
            } else if (binding.otp.text.toString().length != 6) {
                Toast.makeText(context, "Invalid OTP", Toast.LENGTH_SHORT).show()
            } else {
                if (otpId.isNotEmpty()) {
                    val credential =
                        PhoneAuthProvider.getCredential(otpId, binding.otp.text.toString())
                    signInWithPhoneAuthCredential(credential)
                }

            }
        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)

                binding.errorText.text = "OTP sent"
                binding.errorText.visibility = View.VISIBLE
                otpId = p0
                resendingToken = p1
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode
                Toast.makeText(context, "$code yes", Toast.LENGTH_SHORT)
                    .show()
                if (code != null) {
                    binding.otp.setText(code.trim { it <= ' ' })
                    val credential =
                        PhoneAuthProvider.getCredential(otpId, code.trim { it <= ' ' })
                    signInWithPhoneAuthCredential(credential)
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = p0.message
            }

        }

        sendOTP()

    }


    private fun sendOTP() {


        //method to send otp to user
        val options = PhoneAuthOptions.newBuilder(FireBaseUtils.firebaseAuth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        //method to verify otp and sign in user
        FireBaseUtils.firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    binding.errorText.text = "Checking User.."
                    // Sign in success, update UI with the signed-in user's information
                    val user = task.result!!.user!!

                    if (temp == "Login") {
                        checkUser(user)
                    } else if (temp == "SignUp") {

                        saveData(user)

                    } else {
                        //failed
                        binding.errorText.visibility = View.VISIBLE
                        binding.errorText.text = "OTP is incorrect"
                    }
                }
            }
    }

    private fun checkUser(user: FirebaseUser) {
        //login
        val db = FirebaseFirestore.getInstance()
        db.collection("Users")
            .document(user.uid)
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    binding.errorText.text = "Sign in successful"
                    startActivity(
                        Intent(
                            context,
                            Home_Activity::class.java
                        )
                    )
                } else {
                    findNavController().navigate(OtpVerifyFragmentDirections.actionOtpVerifyFragmentToSignUpFragment())
                }
            }
    }

    fun saveData(user: FirebaseUser) {

        //register
        val name: String?
        val emailId: String?
        val DOB: String?
        val username: String?

        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("Users").document(user.uid)

        name = arguments?.getString("name").toString()
        emailId = arguments?.getString("emailId").toString()
        DOB = arguments?.getString("dateOfBirth").toString()
        username = arguments?.getString("username").toString()
        phoneNumber = user.phoneNumber.toString()

        val s = SimpleDateFormat.getDateInstance().format(Date())

        val userData = ProfileInfo(
            name = name,
            username = username,
            dateOfBirth = DOB,
            emailId = emailId,
            phoneNumber = phoneNumber,
            uid = user.uid,
            date_of_registration = s.toString()

        )

        ref.set(userData).addOnSuccessListener {

            startActivity(
                Intent(
                    context,
                    Home_Activity::class.java
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
            activity?.finish()

        }.addOnFailureListener {

            context!!.displayToast("Error to Create Account")

        }


    }


}