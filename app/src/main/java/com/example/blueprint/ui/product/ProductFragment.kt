package com.example.blueprint.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.blueprint.R
import com.example.blueprint.databinding.FragmentProductBinding
import com.example.blueprint.domain.model.Product
import com.example.blueprint.notification.SimpleNotification

class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding
    private lateinit var drinkAdapter: ProductAdapter
    private lateinit var flowerAdapter: ProductAdapter

    init {
        lifecycleScope.launchWhenStarted {
            productViewModel.checkFund()
        }
    }

    private val productViewModel by viewModels<ProductViewModel> {
        ProductViewModel.Companion.Factory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configViewAndActions()
        configObservers()
    }

    private fun configViewAndActions() {
        val processor = ProductProcessor { product -> productViewModel.purchaseProduct(product) }
        drinkAdapter = ProductAdapter(processor)
        flowerAdapter = ProductAdapter(processor)
        RecyclerView.RecycledViewPool().apply {
            // share recycler view pool for better performance
            binding.drinkList.setRecycledViewPool(this)
            binding.flowerList.setRecycledViewPool(this)
        }
        binding.tableFund.leftText = getString(R.string.label_fund_available)
        binding.drinkList.adapter = drinkAdapter
        binding.flowerList.adapter = flowerAdapter
        binding.actionLeave.setOnClickListener { findNavController().popBackStack() }
    }

    private fun configObservers() {
        productViewModel.drinks.observe(viewLifecycleOwner, Observer {
            it?.apply { drinkAdapter.products = it }
        })

        productViewModel.flowers.observe(viewLifecycleOwner, Observer {
            it?.apply { flowerAdapter.products = it }
        })

        productViewModel.fundAmount.observe(viewLifecycleOwner, Observer {
            binding.tableFund.rightText = "$${it ?: 0}"
        })

        productViewModel.purchaseResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ProductPurchaseSuccess -> presentResult(it.product)
                is ProductPurchaseFail -> presentWarning(it.product)
            }
        })
    }

    private fun presentResult(product: Product) {
        val message =
            getString(R.string.message_product_purchase_success, product.name, product.price)
        SimpleNotification.getInstance(requireContext()).createNotification(message)
        productViewModel.onPurchaseResultReceived()
    }

    private fun presentWarning(product: Product) {
        val message =
            getString(R.string.message_warning_product_purchase_fail, product.name, product.price)
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        productViewModel.onPurchaseResultReceived()
    }
}
