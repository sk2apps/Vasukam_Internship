package com.saggy.vasukaminternship.presentation.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.saggy.vasukaminternship.databinding.FragmentSplashScreenBinding
import com.saggy.vasukaminternship.presentation.activity.home.Home_Activity
import com.saggy.vasukaminternship.utils.FireBaseUtils.firebaseUser


@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {


    private lateinit var _binding: FragmentSplashScreenBinding
    private val binding
        get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (firebaseUser != null) {

            val db = FirebaseFirestore.getInstance()
            db.collection("Users")
                .document(firebaseUser.uid)
                .get()
                .addOnSuccessListener {
                    if(it.exists()) {

                        startActivity(Intent(context, Home_Activity::class.java).apply {
                                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                        )
                        activity?.finish()

                    } else {
                        binding.root.findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToGetStartedFragment())
                        /*startActivity(Intent(context, Home_Activity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        )
                        activity?.finish()*/
                    }
                }

        } else {
            Handler().postDelayed({
                binding.root.findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToGetStartedFragment())
            }, 600)
            /*startActivity(Intent(context, Home_Activity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            )
            activity?.finish()*/
        }
    }


}