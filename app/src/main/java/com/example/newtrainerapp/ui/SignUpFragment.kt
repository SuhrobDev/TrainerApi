package com.example.newtrainerapp.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.newtrainerapp.controller.Extensions
import com.example.newtrainerapp.databinding.FragmentSignUpBinding
import com.example.newtrainerapp.mvvm.ActivityViewModel
import com.example.newtrainerapp.retrofit.models.request.LogInRequest
import com.example.newtrainerapp.retrofit.models.request.SignUpRequest


class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {
    private lateinit var viewModel: ActivityViewModel

    override fun onViewCreated() {
        viewModel = ViewModelProvider(requireActivity())[ActivityViewModel::class.java]

        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        binding.rSignupBtn.setOnClickListener {
            val username = binding.rName.text.toString()
            val email = binding.rEmail.text.toString()
            val name = binding.rSurname.text.toString()
            val password = binding.rPassword.text.toString()
            val role = listOf("ROLE_USER")

            viewModel.signUp(
                SignUpRequest(name, username, email, role, password),
                LogInRequest(username, password),
                requireContext()
            )
            Extensions.controller?.replaceFragment(TrainerFragment())
        }
        binding.logIn.setOnClickListener {
            Extensions.controller?.replaceFragment(LoginFragment())
        }
    }
}