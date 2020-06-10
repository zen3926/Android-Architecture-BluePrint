package com.example.blueprint.file

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FundStore(context: Context) : FundStoreProtocol {
    private val preference: SharedPreferences by lazy {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        EncryptedSharedPreferences.create(
            FILE_NAME,
            masterKeyAlias,
            context.applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override suspend fun addFund(amount: Int) {
        withContext(Dispatchers.IO) {
            val oldAmount = checkFund()
            preference.edit { putInt(KEY_FUND, oldAmount + amount) }
        }
    }

    override suspend fun checkFund(): Int {
        return withContext(Dispatchers.IO) {
            preference.getInt(KEY_FUND, 0)
        }
    }

    override suspend fun removeFund(amount: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val oldAmount = checkFund()
            when {
                amount > oldAmount -> false
                else -> {
                    preference.edit { putInt(KEY_FUND, oldAmount - amount) }
                    true
                }
            }
        }
    }

    companion object {
        private const val FILE_NAME = "fund"
        private const val KEY_FUND = "amount"
    }
}
