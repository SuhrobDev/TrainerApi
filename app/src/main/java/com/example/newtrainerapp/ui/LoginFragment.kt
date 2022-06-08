package com.example.newtrainerapp.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.newtrainerapp.controller.Extensions
import com.example.newtrainerapp.databinding.FragmentLoginBinding
import com.example.newtrainerapp.mvvm.ActivityViewModel
import com.example.newtrainerapp.retrofit.models.request.LogInRequest
import com.example.newtrainerapp.utils.SharedPref

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private lateinit var viewModel: ActivityViewModel
    private val sharedPref by lazy {
        SharedPref(requireContext())
    }

    override fun onViewCreated() {
        viewModel = ViewModelProvider(requireActivity())[ActivityViewModel::class.java]

        (activity as AppCompatActivity?)?.supportActionBar?.title="Log In"

        binding.rLoginBtn.setOnClickListener {
            val username = binding.rUsername.text.toString()
            val password = binding.rPassword.text.toString()
            val request: LogInRequest = LogInRequest(username, password)
            viewModel.logIn(request, requireContext())
//            if (username.trim().isNotEmpty() && password.trim().isNotEmpty()) {
////                binding.rUsername.setText(sharedPref.getUserName())
////                binding.rPassword.setText(sharedPref.getPassword())
//                username = binding.rUsername.text.toString()
//                password = binding.rPassword.text.toString()
//                request = LogInRequest(username, password)
//                viewModel.logIn(request, requireContext())
//            } else if (username.trim().isEmpty() && password.trim().isEmpty()) {
//                binding.rUsername.setText(sharedPref.getUserName())
//                binding.rPassword.setText(sharedPref.getPassword())
//                username = sharedPref.getUserName().toString()
//                password = sharedPref.getPassword().toString()
//                request = LogInRequest(username, password)
//                viewModel.logIn(request, requireContext())
//                Toast.makeText(requireContext(), "please enter login!", Toast.LENGTH_SHORT).show()
//            }
        }

        binding.rSignupBtn.setOnClickListener {
            Extensions.controller?.replaceFragment(SignUpFragment())
        }

    }

}