package com.example.updateraho.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.updateraho.utils.LoginFieldState
import com.example.updateraho.utils.RegisterValidation
import com.example.updateraho.utils.Resource
import com.example.updateraho.utils.validateEmail
import com.example.updateraho.utils.validatePassword
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.logging.Handler
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth : FirebaseAuth
) : ViewModel() {
    private val _login = MutableSharedFlow<Resource<FirebaseUser>>()
    val login = _login.asSharedFlow()

    private val _resetPassword = MutableSharedFlow<Resource<String>>()
    val resetPassword = _resetPassword.asSharedFlow()

    private val _validation = Channel<LoginFieldState>()
    val validation = _validation.receiveAsFlow()
    fun login(email : String,password : String)
    {
        if(checkValidation(email,password)) {
            viewModelScope.launch {
                _login.emit(Resource.Loading())
            }

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        it.user?.let {
                            _login.emit(Resource.success(it))
                        }
                    }
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _login.emit(Resource.Error(it.message.toString()))
                    }
                }
        }
        else{
            val loginFieldState = LoginFieldState(
                validateEmail(email), validatePassword(password)
            )
            runBlocking {
                _validation.send(loginFieldState)
            }
        }
    }

    private fun checkValidation(email: String, password: String) : Boolean {
        val emailValidation = validateEmail(email)
        val passwordValidation = validatePassword(password)
        val shouldLogin = emailValidation is RegisterValidation.Succes
                && passwordValidation is RegisterValidation.Succes
        return shouldLogin
    }
    fun resetPassword(email: String){
        viewModelScope.launch {
            _resetPassword.emit(Resource.Loading())
        }

        firebaseAuth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener{ task->
                if(task.isSuccessful)
                {
                    val result = task.result?.signInMethods
                    if (!result.isNullOrEmpty()) {
                        firebaseAuth.sendPasswordResetEmail(email)
                            .addOnSuccessListener {
                                viewModelScope.launch {
                                    _resetPassword.emit(Resource.success(email))
                                }
                            }.addOnFailureListener {
                                viewModelScope.launch {
                                    _resetPassword.emit(Resource.Error(it.message.toString()))
                                }
                            }
                    }
                    else {
                        viewModelScope.launch {
                            _resetPassword.emit(Resource.Error("This Email is not registered"))
                        }
                    }
                }

            }


        }
    }
