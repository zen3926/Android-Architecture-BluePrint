package com.example.blueprint.ui.registration

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.blueprint.R
import com.example.blueprint.databinding.FragmentRegistrationBinding
import com.example.blueprint.worker.SimpleWorker
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import timber.log.Timber

class RegistrationFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding
    private val registrationViewModel: RegistrationViewModel by viewModels {
        RegistrationViewModel.Companion.Factory(requireContext(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configView()
        configAction()
        configObserver()
    }

    private fun configView() {
        val adapter = ArrayAdapter<CharSequence>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            requireContext().resources.getStringArray(R.array.gender_array)
        )
        binding.editGender.setAdapter(adapter)
    }

    private fun configAction() {
        binding.actionSignUp.setOnClickListener {
            val selectedGenderIndex = when (binding.editGender.text?.toString()) {
                "Male" -> 0
                "Female" -> 1
                else -> null
            }
            registrationViewModel.signUp(
                binding.editUserName.text?.toString(),
                binding.editPassword.text?.toString(),
                selectedGenderIndex
            )
        }
        binding.actionDob.setOnClickListener { showDatePicker() }
    }

    private fun configObserver() {
        registrationViewModel.dob.observe(viewLifecycleOwner, Observer {
            it?.run { binding.actionDob.text = this }
        })
        registrationViewModel.inputError.observe(viewLifecycleOwner, Observer {
            if (it == true) showAlert()
        })
        registrationViewModel.complete.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                showSimpleWorker()
                findNavController().navigate(R.id.action_global_nav_home)
                registrationViewModel.onRegistrationComplete()
            }
        })
    }

    private fun showSimpleWorker() {
        val workRequest: WorkRequest = OneTimeWorkRequestBuilder<SimpleWorker>().build()
        WorkManager.getInstance(requireContext()).enqueue(workRequest)
    }

    private fun showDatePicker() {
        val today = MaterialDatePicker.thisMonthInUtcMilliseconds()
        val calenderConstraint = CalendarConstraints.Builder()
            .setEnd(today)
            .build()
        with(MaterialDatePicker.Builder.datePicker()) {
            setSelection(today)
            setCalendarConstraints(calenderConstraint)
            setTitleText(R.string.hint_dob)
        }.build().run {
            addOnCancelListener { Timber.d("Date picker was cancelled") }
            addOnNegativeButtonClickListener { Timber.d("Cancel button was clicked") }
            addOnPositiveButtonClickListener { registrationViewModel.updateDob(it) }
            show(this@RegistrationFragment.parentFragmentManager, "DOB")
        }
    }

    private fun showAlert() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.error_incomplete_registration_info)
            .setPositiveButton(R.string.hint_continue) { dialogInterface: DialogInterface, _: Int ->
                registrationViewModel.clearUserError()
                dialogInterface.dismiss()
            }
            .create()
        dialog.show()
    }
}
