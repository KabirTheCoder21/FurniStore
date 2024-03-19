package com.example.updateraho.utils

sealed class RegisterValidation() {
    object Succes : RegisterValidation()
    data class Failed(val message : String) : RegisterValidation()
}
data class RegisterFieldState(
    val email : RegisterValidation,
    val password : RegisterValidation,
    val firstName : RegisterValidation,
    val lastName : RegisterValidation
)
data class LoginFieldState(
    val email : RegisterValidation,
    val password : RegisterValidation
)