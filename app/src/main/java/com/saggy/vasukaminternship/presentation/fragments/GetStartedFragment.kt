package com.saggy.vasukaminternship.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.saggy.vasukaminternship.R
import com.saggy.vasukaminternship.data.model.onBoarding.OnBoardingData
import com.saggy.vasukaminternship.databinding.FragmentGetStartedBinding
import com.saggy.vasukaminternship.presentation.Adapter.OnBoardingAdapter


class GetStartedFragment : Fragment() {

    private lateinit var _binding: FragmentGetStartedBinding
    private val binding
        get() = _binding

    private lateinit var onBoardingAdapter: OnBoardingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGetStartedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val onBoardingdata: MutableList<OnBoardingData> = ArrayList()

        onBoardingdata.add(OnBoardingData(R.drawable.pexels_arthur_brognoli))
        onBoardingdata.add(OnBoardingData(R.drawable.pexels_jj_jordan))
        onBoardingdata.add(OnBoardingData(R.drawable.pexels_chbani_med))
        onBoardingdata.add(OnBoardingData(R.drawable.pexels_brenoanp))

        binding.login.setOnClickListener {
           it.findNavController().navigate(GetStartedFragmentDirections.actionGetStartedFragmentToLoginFragment())
        }

        binding.signup.setOnClickListener {
            it.findNavController().navigate(GetStartedFragmentDirections.actionGetStartedFragmentToSignUpFragment())
        }

        setOnboardingAdapter(onBoardingdata)

    }

    private fun setOnboardingAdapter(data: List<OnBoardingData>) {
        onBoardingAdapter = OnBoardingAdapter(context!!, data)
        binding.viewPager.adapter = onBoardingAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

}

