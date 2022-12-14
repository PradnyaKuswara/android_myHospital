package com.example.ugd3_kelompok15.api

class JanjiTemuApi  {
    companion object {
        val BASE_URL = "http://192.168.0.112/UGD_Kelompok15/public/api/"

        val GET_ALL = BASE_URL + "janji"
        val GET_BY_ID_URL = BASE_URL + "janji/"
        val ADD_URL = BASE_URL + "janji"
        val UPDATE_URL = BASE_URL + "janji/"
        val DELETE_URL = BASE_URL + "janji/"
    }
}