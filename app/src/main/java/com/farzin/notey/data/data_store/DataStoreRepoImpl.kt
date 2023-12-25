package com.farzin.notey.data.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.farzin.notey.utils.Constants
import kotlinx.coroutines.flow.first
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.DATASTORE_NAME)

class DataStoreRepoImpl @Inject constructor(private val context: Context) : DataStoreRepo {

    override suspend fun putString(value: String, key: String) {
        val preferenceKey = stringPreferencesKey(key)
        context.dataStore.edit { it[preferenceKey] = value }
    }

    override suspend fun getString(key: String): String? {
        return try {
            val preferenceKey = stringPreferencesKey(key)
            val preferences = context.dataStore.data.first()

            preferences[preferenceKey]

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


}