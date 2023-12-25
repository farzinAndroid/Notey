package com.farzin.notey.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farzin.notey.data.data_store.DataStoreRepoImpl
import com.farzin.notey.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@HiltViewModel
class DataStoreViewModel @Inject constructor(private val repo:DataStoreRepoImpl) : ViewModel(){

    companion object {
        const val LANGUAGE_KEY = "LANGUAGE_KEY"
    }




    fun saveUserLang(value:String){
        viewModelScope.launch(Dispatchers.IO) {
            repo.putString(value, LANGUAGE_KEY)
        }
    }

    fun getUserLang() : String? = runBlocking {
        repo.getString(LANGUAGE_KEY) ?: Constants.EN

    }


}