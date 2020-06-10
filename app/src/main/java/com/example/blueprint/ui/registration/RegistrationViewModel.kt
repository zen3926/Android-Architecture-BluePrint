package com.example.blueprint.ui.registration

import android.content.Context
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.example.blueprint.constant.VIEW_MODEL_INIT_ERROR
import com.example.blueprint.domain.model.User
import com.example.blueprint.repository.UserManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class RegistrationViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val userManager: UserManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    init {
        savedStateHandle.get<Long>(DOB)?.run {
            updateDobText(this)
        }
    }

    private val _dob = MutableLiveData<String>()
    val dob: LiveData<String>
        get() = _dob

    private val _inputError = MutableLiveData<Boolean>()
    val inputError: LiveData<Boolean>
        get() = _inputError

    private val _complete = MutableLiveData<Boolean>()
    val complete: LiveData<Boolean>
        get() = _complete


    /**
     * Store dob as long
     */
    fun updateDob(dob: Long) {
        savedStateHandle[DOB] = dob
        updateDobText(dob)
    }

    /**
     * Authenticate UserInput
     */
    fun signUp(userName: String?, password: String?, gender: Int?) {
        val dob = savedStateHandle.get<Long>(DOB)
        if (userName.isNullOrBlank()
            || password.isNullOrBlank()
            || gender !in listOf(0, 1)
            || dob == null
        ) {
            _inputError.value = true
            return
        }

        val user = User(
            userName = userName,
            password = password,
            gender = gender == 0,
            dob = Date(dob)
        )

        viewModelScope.launch {
            withContext(ioDispatcher) {
                userManager.insertUser(user)
            }

            _complete.value = true
        }
    }

    /**
     * Clear user input error
     */
    fun clearUserError() {
        _inputError.value = null
    }

    /**
     * Registration Complete
     */
    fun onRegistrationComplete() {
        _complete.value = null
    }


    private fun updateDobText(dob: Long) {
        _dob.value = SimpleDateFormat("MM/dd/yyyy", Locale.US).run {
            timeZone = TimeZone.getTimeZone("UTC")
            format(Date(dob))
        }
    }

    companion object {
        private const val DOB = "dob"

        /**
         * Factory for constructing RegistrationViewModel with parameter
         */
        class Factory(private val context: Context, owner: SavedStateRegistryOwner) :
            AbstractSavedStateViewModelFactory(owner, null) {
            override fun <T : ViewModel?> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return RegistrationViewModel(handle, UserManager.getRepo(context)) as T
                }

                throw IllegalArgumentException(VIEW_MODEL_INIT_ERROR)
            }
        }
    }
}
