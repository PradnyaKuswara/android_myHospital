package com.example.ugd3_kelompok15.api

class JanjiTemuApi  {
    companion object {
        //sesuaikan ipv4 masing-masing/backend api
        val BASE_URL = "https://myhospital.my.id/api/"

        val GET_ALL = BASE_URL + "janji"
        val GET_BY_ID_URL = BASE_URL + "janji/"
        val ADD_URL = BASE_URL + "janji"
        val UPDATE_URL = BASE_URL + "janji/"
        val DELETE_URL = BASE_URL + "janji/"
    }
}