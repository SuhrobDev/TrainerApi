package com.example.newtrainerapp.ui

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.newtrainerapp.controller.Extensions
import com.example.newtrainerapp.databinding.FragmentSignUpBinding
import com.example.newtrainerapp.mvvm.ActivityViewModel
import com.example.newtrainerapp.retrofit.models.request.LogInRequest
import com.example.newtrainerapp.retrofit.models.request.SignUpRequest
import com.example.newtrainerapp.utils.SharedPref


class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {
    private lateinit var viewModel: ActivityViewModel

    private val sharedPref by lazy {
        SharedPref(requireContext())
    }

    override fun onViewCreated() {
        viewModel = ViewModelProvider(requireActivity())[ActivityViewModel::class.java]

        (activity as AppCompatActivity?)?.supportActionBar?.title="Sign Up"

        binding.rSignupBtn.setOnClickListener {
            val username = binding.rName.text.toString()
            val email = binding.rEmail.text.toString()
            val name = binding.rSurname.text.toString()
            val password = binding.rPassword.text.toString()
            val role = listOf("ROLE_USER")

            if (username.trim().isNotEmpty() &&
                email.trim().isNotEmpty() &&
                name.trim().isNotEmpty() &&
                password.trim().isNotEmpty()
            ) {
                if (email.endsWith("@gmail.com") && password.trim() > 8.toString()) {
                    viewModel.signUp(
                        SignUpRequest(name, username, email, role, password),
                        LogInRequest(username, password),
                        requireContext()
                    )
                    sharedPref.setPassword(password)
                    sharedPref.setUserName(username)
                    Extensions.controller?.startMainFragment(LoginFragment())
                } else if (!email.trim().endsWith("@gmail.com")) {
                    Toast.makeText(
                        requireContext(),
                        "Please enter an email true ",
                        Toast.LENGTH_SHORT
                    ).show()
                }else if (password.trim()< 8.toString()){
                    Toast.makeText(requireContext(), "weak password", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Please fill fields", Toast.LENGTH_SHORT).show()
            }

//            Extensions.controller?.replaceFragment(TrainerFragment())
        }
        binding.logIn.setOnClickListener {
            Extensions.controller?.replaceFragment(LoginFragment())
        }
    }
}