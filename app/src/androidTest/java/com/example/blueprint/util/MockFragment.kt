package com.example.blueprint.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

class MockFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val id = arguments?.getInt(LAYOUT_ID)
        return id?.run { inflater.inflate(id, container, false) }
    }

    companion object {
        const val LAYOUT_ID = "LAYOUT_ID"
        fun newbundle(@LayoutRes layoutId: Int) = bundleOf(
            LAYOUT_ID to layoutId
        )
    }
}
