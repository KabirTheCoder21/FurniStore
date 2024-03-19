package com.example.updateraho.utils

import android.content.pm.ResolveInfo
import android.util.Patterns

fun validateFirstName(firstName : String):RegisterValidation{
    if(firstName.isEmpty())
        return RegisterValidation.Failed("First Name cannot be empty")
    return RegisterValidation.Succes
}
fun validateLastName(lastName : String):RegisterValidation{
    if(lastName.isEmpty())
        return RegisterValidation.Failed("Last Name cannot be empty")
    return RegisterValidation.Succes
}
fun validateEmail(email:String) : RegisterValidation{
    if(email.isEmpty())
        return RegisterValidation.Failed("Email cannot be empty")
    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterValidation.Failed("Wrong Email Format")

    return RegisterValidation.Succes
}
fun validatePassword(password : String) : RegisterValidation {
    if(password.isEmpty())
        return RegisterValidation.Failed("Password cannot be empty")
    if(password.length < 6)
        return RegisterValidation.Failed("Password should contains 6 character")

    return RegisterValidation.Succes
}