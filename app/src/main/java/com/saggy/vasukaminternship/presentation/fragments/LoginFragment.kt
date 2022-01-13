package com.saggy.vasukaminternship.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.saggy.vasukaminternship.R
import com.saggy.vasukaminternship.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var _binding: FragmentLoginBinding
    private val binding
        get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signup.setOnClickListener {

            it.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())

        }

        binding.login.setOnClickListener {
            if (binding.phoneNumber.text.toString().isEmpty()) {
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Please enter mobile number"
            } else {
                binding.errorText.visibility = View.GONE
                val number = binding.ccp.selectedCountryCodeWithPlus + binding.phoneNumber.text.toString()
                val loginDataBundle = Bundle()
                loginDataBundle.putString("PhoneNumber",number)
                loginDataBundle.putString("From","Login")
                it.findNavController().navigate(R.id.action_loginFragment_to_otpVerifyFragment,loginDataBundle)
            }
        }

        binding.backButton.setOnClickListener {
            it.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToGetStartedFragment())
        }

    }

}