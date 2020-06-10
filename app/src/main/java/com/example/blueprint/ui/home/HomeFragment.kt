package com.example.blueprint.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.blueprint.R
import com.example.blueprint.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel by viewModels<HomeViewModel> {
        HomeViewModel.Companion.Factory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configActions()
        configObservers()
    }

    private fun configActions() {
        binding.actionSignOut.setOnClickListener { findNavController().navigate(R.id.action_global_loginFragment) }
        binding.actionDeleteUser.setOnClickListener {
            homeViewModel.deleteUser()
        }
        binding.actionAddFund.setOnClickListener { findNavController().navigate(R.id.action_nav_home_to_fundFragment) }
        binding.actionViewProduct.setOnClickListener { findNavController().navigate(R.id.action_nav_home_to_productFragment) }
    }

    private fun configObservers() {
        homeViewModel.checkFund()
        homeViewModel.userName.observe(viewLifecycleOwner, Observer {
            binding.tableUser.rightText = it ?: getString(R.string.error_empty_user_name)
        })

        homeViewModel.fundAmount.observe(viewLifecycleOwner, Observer {
            binding.tableFund.rightText = "$${it ?: 0}"
        })

        homeViewModel.deleteComplete.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(R.id.action_global_loginFragment)
            homeViewModel.onDeletionNavigationComplete()
        })
    }
}
