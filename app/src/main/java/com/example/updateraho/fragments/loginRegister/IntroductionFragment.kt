package com.example.updateraho.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.updateraho.R
import com.example.updateraho.activities.ShoppingActivity
import com.example.updateraho.databinding.FragmentIntroductionBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class IntroductionFragment : Fragment(R.layout.fragment_introduction) {
    private lateinit var binding: FragmentIntroductionBinding
@Inject
lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroductionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            binding.startButton.setOnClickListener {
                findNavController().navigate(R.id.action_introductionFragment_to_accountOptionsFragment)

            }
    }

    override fun onStart() {
        super.onStart()
        checkLoggedInUser()
    }
    private fun checkLoggedInUser() {
        if (auth.currentUser != null) {
            val intent = Intent(requireActivity(), ShoppingActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            requireActivity().finishAffinity()
        }
    }
}