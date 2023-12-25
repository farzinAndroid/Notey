package com.farzin.notey.data.data_store


interface DataStoreRepo {

    suspend fun putString(value:String,key:String)
    suspend fun getString(key:String) : String?

}