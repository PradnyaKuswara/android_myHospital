package com.example.ugd3_kelompok15.api

class UserProfilApi {

    companion object{
        //sesuaikan ipv4 masing-masing
        val BASE_URL = "http://10.113.73.98/UGD_Kelompok15/public/api/"

        val GET_BY_ID_URL = BASE_URL + "user/"
        val UPDATE_URL = BASE_URL + "user/"
        val DELETE_URL = BASE_URL + "user/"

        val REGISTER = BASE_URL + "register"
        val LOGIN = BASE_URL + "checklogin"
    }

}