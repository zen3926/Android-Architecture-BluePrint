package com.example.blueprint.ui.login

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.blueprint.R
import com.example.blueprint.databinding.FragmentLoginBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModel.Companion.Factory(requireContext())
    }

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
            loginViewModel.authenticateUserSuccess()
        }

        binding.actionToRegistration.setOnClickListener { findNavController().navigate(R.id.registrationFragment) }
    }

    private fun configViewModelObservers() {
        loginViewModel.hasUser.observe(viewLifecycleOwner, Observer { hasUser ->
            binding.actionBiometric.isEnabled = hasUser == true
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
