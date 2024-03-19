package com.example.updateraho.fragments.shopping

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.updateraho.R
import com.example.updateraho.activities.LoginRegisterActivity
import com.example.updateraho.activities.ShoppingActivity
import com.example.updateraho.databinding.FragmentProfileBinding
import com.example.updateraho.fragments.loginRegister.IntroductionFragment
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private lateinit var binding : FragmentProfileBinding
    @Inject
    lateinit var auth : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //For logging out
        binding.logOut.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireActivity(),LoginRegisterActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            requireActivity().finishAffinity()
        }
    }
}