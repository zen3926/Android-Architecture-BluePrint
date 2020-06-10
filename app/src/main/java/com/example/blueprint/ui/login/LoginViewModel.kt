package com.example.blueprint.ui.login

import android.content.Context
import androidx.lifecycle.*
import com.example.blueprint.constant.VIEW_MODEL_INIT_ERROR
import com.example.blueprint.repository.UserManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val userManager: UserManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    val hasUser: LiveData<Boolean> = Transformations.map(userManager.user) {
        it != null
    }

    private val _userState: MutableLiveData<Boolean> = MutableLiveData()
    val userState: LiveData<Boolean>
        get() = _userState

    private val _userNameInputError = MutableLiveData<Boolean>()
    val userNameInputError: LiveData<Boolean>
        get() = _userNameInputError

    private val _passwordInputError = MutableLiveData<Boolean>()
    val passwordInputError: LiveData<Boolean>
        get() = _passwordInputError

    /**
     * Authenticate user input
     */
    fun authenticateUser(userName: String?, password: String?) {
        if (userName.isNullOrBlank() || password.isNullOrBlank()) {
            if (userName.isNullOrBlank()) {
                _userNameInputError.value = true
            }
            if (password.isNullOrBlank()) {
                _passwordInputError.value = true
            }
            return
        }
        viewModelScope.launch {
            val user = withContext(ioDispatcher) {
                userManager.getSignedUser()
            }
            _userState.value = user?.userName == userName && user.password == password
        }
    }

    /**
     * Authenticate user input
     */
    fun authenticateUserSuccess() {
        _userState.value = true
    }

    /**
     * Acknowledge navigation complete
     */
    fun onUserStateNavigationComplete() {
        _userState.value = null
    }

    /**
     * Clear userName input error
     */
    fun clearUserNameInputError() {
        _userNameInputError.value = null
    }

    /**
     * Clear password input error
     */
    fun clearPasswordInputError() {
        _passwordInputError.value = null
    }

    companion object {
        /**
         * Factory for constructing LoginViewModel with parameter
         */
        class Factory(private val context: Context) : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return LoginViewModel(UserManager.getRepo(context)) as T
                }
                throw IllegalArgumentException(VIEW_MODEL_INIT_ERROR)
            }

        }
    }
}
