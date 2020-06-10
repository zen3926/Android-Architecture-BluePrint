package com.example.blueprint.repository

import android.content.Context
import com.example.blueprint.file.FundStore
import com.example.blueprint.file.FundStoreProtocol

class FundRepo(private val protocol: FundStoreProtocol) {

    suspend fun checkAvailableFund(): Int = protocol.checkFund()
    suspend fun addFund(amount: Int) = protocol.addFund(amount)
    suspend fun deductFund(amount: Int): Boolean = protocol.removeFund(amount)

    companion object {
        /**
         * Get an instance of FundRepo
         */
        fun getRepo(context: Context) = FundRepo(FundStore(context))
    }
}
