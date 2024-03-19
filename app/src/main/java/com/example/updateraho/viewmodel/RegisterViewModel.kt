package com.example.updateraho.viewmodel

import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.updateraho.data.User
import com.example.updateraho.utils.Constants.USER_COLLECTION
import com.example.updateraho.utils.RegisterFieldState
import com.example.updateraho.utils.RegisterValidation
import com.example.updateraho.utils.Resource
import com.example.updateraho.utils.validateEmail
import com.example.updateraho.utils.validateFirstName
import com.example.updateraho.utils.validateLastName
import com.example.updateraho.utils.validatePassword
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth : FirebaseAuth,
    private val db : FirebaseFirestore
) : ViewModel(){

    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register : Flow<Resource<User>> = _register

    private val _validation = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()

    fun createAccountWithEmailAndPassword(user : User,password : String) {
        if (checkValidation(user, password)) {
            runBlocking {
                _register.emit(Resource.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                    it.user?.let {
                        saveUserInfo(it.uid,user)
                    }
                }.addOnFailureListener {
                    _register.value = Resource.Error(it.message.toString())
                }
        }
        else{
            val registerFieledState = RegisterFieldState(
                validateEmail(user.email), validatePassword(password),
                validateFirstName(user.firstName),
                validateLastName(user.lastName)
            )
            runBlocking {
                _validation.send(registerFieledState)
            }
        }
    }

    private fun saveUserInfo(useruid:String,user : User) {
        db.collection(USER_COLLECTION)
            .document(useruid)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.success(user)
            }
            .addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }
    }
}

private fun checkValidation(user: User, password: String): Boolean {
    val emailValidation = validateEmail(user.email)
    val passwordValidation = validatePassword(password)
    val firstNameValidation = validateFirstName(user.firstName)
    val lastNameValidation = validateLastName(user.lastName)
    return (emailValidation is RegisterValidation.Succes
            && passwordValidation is RegisterValidation.Succes && firstNameValidation is RegisterValidation.Succes
            && lastNameValidation is RegisterValidation.Succes)
}