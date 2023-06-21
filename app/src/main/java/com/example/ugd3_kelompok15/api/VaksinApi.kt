package com.example.ugd3_kelompok15.api

class VaksinApi {
    companion object {
        //sesuaikan ipv4 masing-masing/backend api
        val BASE_URL = "https://myhospital.my.id/api/"

        val GET_ALL = BASE_URL + "vaksin"
        val GET_BY_ID_URL = BASE_URL + "vaksin/"
        val ADD_URL = BASE_URL + "vaksin"
        val UPDATE_URL = BASE_URL + "vaksin/"
        val DELETE_URL = BASE_URL + "vaksin/"
    }
}