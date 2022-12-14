package com.example.ugd3_kelompok15.api

class VaksinApi {
    companion object {
        val BASE_URL = "http://192.168.0.112/UGD_Kelompok15/public/api/"

        val GET_ALL = BASE_URL + "vaksin"
        val GET_BY_ID_URL = BASE_URL + "vaksin/"
        val ADD_URL = BASE_URL + "vaksin"
        val UPDATE_URL = BASE_URL + "vaksin/"
        val DELETE_URL = BASE_URL + "vaksin/"
    }
}