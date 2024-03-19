package com.example.updateraho.dialog

import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.updateraho.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.lifecycle.HiltViewModel

fun Fragment.setupBottomSheetDialog(
    onSendClick : (String) ->Unit
)
{
    val dialog = BottomSheetDialog(requireContext(),R.style.DialogStyle)
    val view = layoutInflater.inflate(R.layout.reset_password,null)
    dialog.setContentView(view)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    val etEmail = view.findViewById<EditText>(R.id.etResetPassword)
    val btnSend : Button =view.findViewById(R.id.sendRP)
    val btnCancel : Button = view.findViewById(R.id.cancelRP)

    btnSend.setOnClickListener{

        val email = etEmail.text.toString().trim()
        if(email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onSendClick(email)
            dialog.dismiss()
        }
        else{
            etEmail.requestFocus()
                etEmail.error = "Email cannot be empty"
        }
    }
    btnCancel.setOnClickListener{
        dialog.dismiss()
    }
}