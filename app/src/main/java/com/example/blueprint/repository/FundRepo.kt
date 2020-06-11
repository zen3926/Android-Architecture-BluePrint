package com.example.blueprint.repository

import android.content.Context
import com.example.blueprint.file.FundStore
import com.example.blueprint.file.FundStoreProtocol
import com.example.blueprint.widget.WidgetFundUpdateObserver

class FundRepo(
    private val protocol: FundStoreProtocol,
    private val fundObserver: FundUpdateObserver
) {

    suspend fun checkAvailableFund(): Int = protocol.checkFund()
    suspend fun addFund(amount: Int): Int {
        fundObserver.act()
        return protocol.addFund(amount)
    }

    suspend fun deductFund(amount: Int): Boolean {
        return protocol.removeFund(amount).also {
            if (it) fundObserver.act()
        }
    }

    companion object {
        /**
         * Get an instance of FundRepo
         */
        fun getRepo(context: Context) =
            FundRepo(FundStore(context), WidgetFundUpdateObserver(context))
    }
}

interface FundUpdateObserver {
    fun act()
}
