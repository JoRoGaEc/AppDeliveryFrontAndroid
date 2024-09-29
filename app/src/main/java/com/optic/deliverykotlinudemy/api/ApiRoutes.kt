package com.optic.deliverykotlinudemy.api

import com.optic.deliverykotlinudemy.routes.UsersRoutes

class ApiRoutes {
    val API_URL="http://192.168.1.10:3000/api/"
    val retrofit = RetrofitClient()

    fun getUsersRoutes(): UsersRoutes{
        return retrofit.getClient(API_URL).create(UsersRoutes::class.java)
    }
}