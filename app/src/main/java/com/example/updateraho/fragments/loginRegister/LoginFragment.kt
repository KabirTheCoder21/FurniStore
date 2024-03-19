package com.example.updateraho.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.updateraho.R
import com.example.updateraho.activities.ShoppingActivity
import com.example.updateraho.databinding.FragmentLoginBinding
import com.example.updateraho.dialog.setupBottomSheetDialog
import com.example.updateraho.utils.RegisterValidation
import com.example.updateraho.utils.Resource
import com.example.updateraho.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginFragment : Fragment() {
private lateinit var binding: FragmentLoginBinding
private val viewmodels by viewModels<LoginViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var spannable = SpannableString("Don't have an account? Register")

        // Find the start and end indices of the text to be underlined
        var startIndex = spannable.indexOf("Register")
        var endIndex = startIndex + "Register".length

        // Apply UnderlineSpan to the specified range
        spannable.setSpan(UnderlineSpan(), startIndex, endIndex, 0)
        binding.tvDontHaveAccRegister.text = spannable

        spannable = SpannableString("Forgot Password?")
        startIndex = spannable.indexOf("Forgot Password")
        endIndex = startIndex + "Forgot Password".length
        spannable.setSpan(UnderlineSpan(), startIndex, endIndex, 0)
        binding.tvForgotPassword.text = spannable

        binding.tvForgotPassword.setOnClickListener {
            setupBottomSheetDialog {
                viewmodels.resetPassword(it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewmodels.resetPassword.collect{
                when(it)
                {
                    is Resource.Loading ->{

                    }
                    is Resource.success ->{
                        val snackbar = Snackbar.make(requireView(), "Reset link was sent to your email", Snackbar.LENGTH_SHORT)
                        snackbar.show()
                        Handler().postDelayed({
                            snackbar.dismiss()
                        }, 4000)
                    }
                    is Resource.Error->{
                        val snackbar = Snackbar.make(requireView(),it.message.toString(),Snackbar.LENGTH_SHORT)
                            snackbar.show()
                        Handler().postDelayed({
                            snackbar.dismiss()
                        }, 4000)
                    }
                    else -> Unit
                }
            }
        }

        binding.tvDontHaveAccRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.apply {
            loginButton.setOnClickListener{
                val email = etEmailLogin.text.toString().trim()
                val password = etPasswordLogin.text.toString().trim()
                viewmodels.login(email, password)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewmodels.login.collect{
                when(it)
                {
                    is Resource.Loading ->{
                        binding.loginButton.startAnimation()
                    }
                    is Resource.success ->{
                        binding.loginButton.revertAnimation()
                        Intent(requireActivity(),ShoppingActivity::class.java).also {
                            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(it)
                        }
                    }
                    is Resource.Error->{
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.loginButton.revertAnimation()
                    }
                    else -> Unit
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewmodels.validation.collect{
                if (it.email is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.etEmailLogin.apply {
                            requestFocus()
                            error = it.email.message
                        }
                    }
                }
                else if(it.password is RegisterValidation.Failed)
                {
                    withContext(Dispatchers.Main){
                        binding.etPasswordLogin.apply {
                            requestFocus()
                            error = it.password.message
                        }
                    }
                }
            }
        }
    }
}