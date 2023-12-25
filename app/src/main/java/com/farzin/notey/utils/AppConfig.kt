package com.farzin.notey.utils

import com.farzin.notey.viewmodel.DataStoreViewModel


fun appConfig(dataStoreViewModel:DataStoreViewModel){

    getDataStoreVariables(dataStoreViewModel)

}

fun getDataStoreVariables(dataStoreViewModel: DataStoreViewModel) {

    Constants.USER_LANG = dataStoreViewModel.getUserLang().toString()
    dataStoreViewModel.saveUserLang(Constants.USER_LANG)

}

