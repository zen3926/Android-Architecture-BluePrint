package com.example.blueprint.ui.fund

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.blueprint.R
import com.example.blueprint.databinding.FragmentFundBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FundFragment : Fragment() {
    private lateinit var binding: FragmentFundBinding
    private val fundViewModel by viewModels<FundViewModel> {
        FundViewModel.Companion.Factory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFundBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.actionAddFund.setOnClickListener { fundViewModel.addFund() }
        binding.actionLeave.setOnClickListener { findNavController().navigateUp() }

        fundViewModel.checkFund()
        fundViewModel.fundAmount.observe(viewLifecycleOwner, Observer {
            binding.tableFund.rightText = "$${it ?: 0}"
        })
        fundViewModel.fundOfferAmount.observe(viewLifecycleOwner, Observer {
            it?.run { presentOffer(it) }
        })
    }

    private fun presentOffer(amount: Int) {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.message_fund_offer, amount))
            .setPositiveButton(R.string.hint_continue) { dialog, _ ->
                dialog.dismiss()
                fundViewModel.fundAccepted(amount)
            }
            .setNegativeButton(R.string.hint_cancel) { dialog, _ ->
                dialog.dismiss()
                fundViewModel.fundRejected()
            }
            .create()
        dialog.show()
    }
}
