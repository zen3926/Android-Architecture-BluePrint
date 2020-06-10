package com.example.blueprint.file

interface FundStoreProtocol {
    /**
     * Add fund to existing amount
     */
    suspend fun addFund(amount: Int)

    /**
     * Check fund balance
     */
    suspend fun checkFund(): Int

    /**
     * Remove certain Amount, return true if sufficient fund, else false
     */
    suspend fun removeFund(amount: Int): Boolean
}
