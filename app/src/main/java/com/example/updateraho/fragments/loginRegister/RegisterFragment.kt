package com.example.updateraho.fragments.loginRegister

import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.updateraho.R
import com.example.updateraho.data.User
import com.example.updateraho.databinding.FragmentRegisterBinding
import com.example.updateraho.utils.RegisterValidation
import com.example.updateraho.utils.Resource
import com.example.updateraho.viewmodel.RegisterViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment : Fragment() {
private lateinit var binding : FragmentRegisterBinding
private val viewmodel by viewModels<RegisterViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spannable = SpannableString("Have an account? Login")

        // Find the start and end indices of the text to be underlined
        val startIndex = spannable.indexOf("Login")
        val endIndex = startIndex + "Login".length

        // Apply UnderlineSpan to the specified range
        spannable.setSpan(UnderlineSpan(), startIndex, endIndex, 0)
        binding.tvHaveAccLogin.text = spannable

        binding.tvHaveAccLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.apply {
            registerButton.setOnClickListener{
                val user = User(
                    etFirstNameLogin.text.toString().trim(),
                    etLastNameLogin.text.toString().trim(),
                    etEmailLogin.text.toString().trim())
                val password = etPasswordLogin.text.toString()
                viewmodel.createAccountWithEmailAndPassword(user, password)

            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewmodel.register.collect{
                when(it)
                {
                    is Resource.Loading -> {
                        binding.registerButton.startAnimation()
                    }
                    is Resource.success->{
                        Log.d("testing", "onViewCreated: ${it.data.toString()}")
                        binding.registerButton.revertAnimation()
                        setEmpty()
                    }
                    is Resource.Error->{
                        Log.d("error", it.message.toString())
                        val snackbar = Snackbar.make(requireView(),it.message.toString(), Snackbar.LENGTH_SHORT)
                        snackbar.show()
                        Handler().postDelayed({
                            snackbar.dismiss()
                        }, 4000)
                        binding.registerButton.revertAnimation()
                    }
                    else-> Unit
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewmodel.validation.collect {
                if (it.firstName is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.etFirstNameLogin.apply {
                            requestFocus()
                            error = it.firstName.message
                        }
                    }
                }
                else if (it.lastName is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.etLastNameLogin.apply {
                            requestFocus()
                            error = it.lastName.message
                        }
                    }
                }
               else if (it.email is RegisterValidation.Failed) {
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

    private fun setEmpty() {
        binding.apply {
            etFirstNameLogin.setText("")
            etLastNameLogin.setText("")
            etEmailLogin.setText("")
            etPasswordLogin.setText("")
        }
    }
}