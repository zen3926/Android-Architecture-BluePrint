package com.example.blueprint.ui.login

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.blueprint.R
import com.example.blueprint.databinding.FragmentLoginBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.concurrent.Executor

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModel.Companion.Factory(requireContext())
    }

    private val handler = Handler()
    private val executor = Executor { command -> handler.post(command) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configViewActions()
        configViewModelObservers()
    }

    private fun configViewActions() {
        binding.actionSignIn.setOnClickListener {
            loginViewModel.authenticateUser(
                binding.editUserName.text?.toString(),
                binding.editPassword.text?.toString()
            )
        }
        binding.editUserName.doAfterTextChanged {
            binding.inputUserName.error = null
            loginViewModel.clearUserNameInputError()
        }
        binding.editPassword.doAfterTextChanged {
            binding.inputPassword.error = null
            loginViewModel.clearPasswordInputError()
        }

        binding.actionBiometric.setOnClickListener {
            showBiometric()
        }

        binding.actionToRegistration.setOnClickListener { findNavController().navigate(R.id.registrationFragment) }
    }

    private fun configViewModelObservers() {
        loginViewModel.hasUser.observe(viewLifecycleOwner, Observer { hasUser ->
            binding.actionBiometric.isEnabled = hasUser == true && isBioMetricEnabled()
        })

        loginViewModel.userState.observe(viewLifecycleOwner, Observer {
            it?.let { signedIn ->
                when {
                    signedIn -> findNavController().navigate(R.id.action_global_nav_home)
                    else -> showAlert()
                }
                loginViewModel.onUserStateNavigationComplete()
            }
        })

        loginViewModel.userNameInputError.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.inputUserName.error = getString(R.string.error_empty_user_name)
            }
        })

        loginViewModel.passwordInputError.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.inputPassword.error = getString(R.string.error_empty_password)
            }
        })
    }

    private fun showBiometric() {
        val prompt = with(BiometricPrompt.PromptInfo.Builder()) {
            setTitle(getString(R.string.message_biometric))
            setNegativeButtonText(getString(R.string.hint_cancel))
            setConfirmationRequired(true)
        }.build()

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                loginViewModel.authenticateUserSuccess()
            }
        }
        BiometricPrompt(this, executor, callback).authenticate(prompt)
    }

    private fun isBioMetricEnabled(): Boolean =
        BiometricManager.from(requireContext().applicationContext)
            .canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS


    private fun showAlert() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.error_sign_in)
            .setPositiveButton(R.string.hint_continue) { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
            .create()
        dialog.show()
    }
}
