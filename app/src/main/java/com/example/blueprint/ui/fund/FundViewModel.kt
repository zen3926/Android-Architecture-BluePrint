package com.example.blueprint.ui.fund

import android.content.Context
import androidx.lifecycle.*
import com.example.blueprint.constant.VIEW_MODEL_INIT_ERROR
import com.example.blueprint.repository.FundRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.SecureRandom

class FundViewModel(
    private val fundRepo: FundRepo,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _fundAmount = MutableLiveData<Int>()
    val fundAmount: LiveData<Int>
        get() = _fundAmount

    private val _fundOfferAmount = MutableLiveData<Int>()
    val fundOfferAmount: LiveData<Int>
        get() = _fundOfferAmount

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
     * Add random fund amount
     */
    fun addFund() {
        val randomAmount = SecureRandom().nextInt(500)
        _fundOfferAmount.value = randomAmount
    }

    /**
     * Accept offered amount
     */
    fun fundAccepted(amount: Int) {
        viewModelScope.launch {
            val newAmount = withContext(ioDispatcher) {
                fundRepo.addFund(amount)
            }
            _fundAmount.value = newAmount
            _fundOfferAmount.value = null
        }
    }

    /**
     * Reject offer amount
     */
    fun fundRejected() {
        _fundOfferAmount.value = null
    }

    companion object {
        /**
         * Factory for constructing FundViewModel with parameter
         */
        class Factory(private val context: Context) : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(FundViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return FundViewModel(FundRepo.getRepo(context)) as T
                }
                throw IllegalArgumentException(VIEW_MODEL_INIT_ERROR)
            }
        }
    }
}
