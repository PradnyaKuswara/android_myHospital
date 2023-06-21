package com.example.ugd3_kelompok15.api

class UserProfilApi {

    companion object{
        //sesuaikan ipv4 masing-masing/backend api
        val BASE_URL = "https://myhospital.my.id/api/"

        val GET_BY_ID_URL = BASE_URL + "user/"
        val UPDATE_URL = BASE_URL + "user/"
        val DELETE_URL = BASE_URL + "user/"

        val REGISTER = BASE_URL + "register"
        val LOGIN = BASE_URL + "checklogin"
    }

}