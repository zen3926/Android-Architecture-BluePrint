package com.example.blueprint.ui.home

import android.content.Context
import androidx.lifecycle.*
import com.example.blueprint.constant.VIEW_MODEL_INIT_ERROR
import com.example.blueprint.repository.FundRepo
import com.example.blueprint.repository.UserManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val userManager: UserManager,
    private val fundRepo: FundRepo,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _fundAmount = MutableLiveData<Int>()
    val fundAmount: LiveData<Int>
        get() = _fundAmount

    val userName: LiveData<String> = Transformations.map(userManager.user) {
        it?.userName
    }

    private val _deleteComplete = MutableLiveData<Boolean>()
    val deleteComplete: LiveData<Boolean>
        get() = _deleteComplete

    /**
     * Delete user account
     */
    fun deleteUser() {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                userManager.removeUser()
            }
            _deleteComplete.value = true
        }
    }

    /**
     * Acknowledge delete navigation complete
     */
    fun onDeletionNavigationComplete() {
        _deleteComplete.value = null
    }

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

    companion object {
        /**
         * Factory for constructing HomeViewModel with parameter
         */
        class Factory(private val context: Context) : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return HomeViewModel(
                        UserManager.getRepo(context),
                        FundRepo.getRepo(context)
                    ) as T
                }
                throw IllegalArgumentException(VIEW_MODEL_INIT_ERROR)
            }

        }
    }
}
