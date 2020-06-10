package com.example.blueprint.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.blueprint.R
import com.example.blueprint.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding
    private val welcomeViewModel: WelcomeViewModel by viewModels {
        WelcomeViewModel.Companion.Factory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        welcomeViewModel.userState.observe(viewLifecycleOwner, Observer {
            it?.let { signedIn ->
                when {
                    signedIn -> findNavController().navigate(R.id.action_global_nav_home)
                    else -> findNavController().navigate(R.id.action_global_loginFragment)
                }
                welcomeViewModel.onUserStateNavigationComplete()
            }
        })

        welcomeViewModel.checkUserState()
    }
}
