package com.example.blueprint.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.blueprint.R
import com.example.blueprint.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.button6.setOnClickListener { findNavController().navigate(R.id.action_global_loginFragment) }
        binding.button7.setOnClickListener { findNavController().navigate(R.id.action_nav_home_to_fundFragment) }
        binding.button8.setOnClickListener { findNavController().navigate(R.id.action_nav_home_to_productFragment) }
    }
}
