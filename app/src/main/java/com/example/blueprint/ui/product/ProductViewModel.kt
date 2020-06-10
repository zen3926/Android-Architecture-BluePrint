package com.example.blueprint.ui.product

import android.content.Context
import androidx.lifecycle.*
import com.example.blueprint.constant.VIEW_MODEL_INIT_ERROR
import com.example.blueprint.database.AppDatabase
import com.example.blueprint.domain.model.Product
import com.example.blueprint.domain.model.asProduct
import com.example.blueprint.repository.DrinkRepo
import com.example.blueprint.repository.FlowerRepo
import com.example.blueprint.repository.FundRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductViewModel(
    private val fundRepo: FundRepo,
    drinkRepo: DrinkRepo,
    flowerRepo: FlowerRepo,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _fundAmount = MutableLiveData<Int>()
    val fundAmount: LiveData<Int>
        get() = _fundAmount

    val drinks: LiveData<List<Product>> = Transformations.map(drinkRepo.getAll()) {
        it.map { drink -> drink.asProduct() }
    }

    val flowers: LiveData<List<Product>> = Transformations.map(flowerRepo.getAll()) {
        it.map { flower -> flower.asProduct() }
    }

    private val _purchaseResult = MutableLiveData<ProductPurchaseResult>()
    val purchaseResult: LiveData<ProductPurchaseResult>
        get() = _purchaseResult

    /**
     * Check available fund
     */
    fun checkFund() {
        viewModelScope.launch {
            val amount = withContext(ioDispatcher) {
                fundRepo.checkAvailableFund()
            }
            _fundAmount.value = amount
        }
    }

    /**
     * purchase product
     */
    fun purchaseProduct(product: Product) {
        viewModelScope.launch {
            val success = withContext(ioDispatcher) {
                fundRepo.deductFund(product.price)
            }
            when {
                success -> {
                    _purchaseResult.value = ProductPurchaseSuccess(product)
                    checkFund()
                }
                else -> _purchaseResult.value = ProductPurchaseFail(product)
            }
        }
    }

    /**
     * Acknowledge purchase Result received
     */
    fun onPurchaseResultReceived() {
        _purchaseResult.value = null
    }

    companion object {
        /**
         * Factory for constructing ProductViewModel with parameter
         */
        class Factory(private val context: Context) : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
                    val database = AppDatabase.getInstance(context)
                    @Suppress("UNCHECKED_CAST")
                    return ProductViewModel(
                        FundRepo.getRepo(context),
                        DrinkRepo(database.getDrinkDao()),
                        FlowerRepo(database.getFlowerDao())
                    ) as T
                }
                throw IllegalArgumentException(VIEW_MODEL_INIT_ERROR)
            }
        }
    }
}

sealed class ProductPurchaseResult(val product: Product)
class ProductPurchaseSuccess(product: Product) : ProductPurchaseResult(product)
class ProductPurchaseFail(product: Product) : ProductPurchaseResult(product)
