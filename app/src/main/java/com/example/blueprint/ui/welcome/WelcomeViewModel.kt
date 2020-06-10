package com.example.blueprint.ui.welcome

import android.content.Context
import androidx.lifecycle.*
import com.example.blueprint.constant.VIEW_MODEL_INIT_ERROR
import com.example.blueprint.repository.UserManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WelcomeViewModel(
    private val userManager: UserManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _userState: MutableLiveData<Boolean> = MutableLiveData()
    val userState: LiveData<Boolean>
        get() = _userState

    /**
     * Determine user state
     */
    fun checkUserState() {
        viewModelScope.launch {
            val user = withContext(ioDispatcher) {
                userManager.getSignedUser()
            }
            _userState.value = user != null
        }
    }

    /**
     * Acknowledge navigation complete
     */
    fun onUserStateNavigationComplete() {
        _userState.value = null
    }

    companion object {
        /**
         * Factory for constructing WelcomeViewModel with parameter
         */
        class Factory(private val context: Context) : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(WelcomeViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return WelcomeViewModel(UserManager.getRepo(context)) as T
                }
                throw IllegalArgumentException(VIEW_MODEL_INIT_ERROR)
            }

        }
    }
}
