package com.optic.deliverykotlinudemy.routes


import com.optic.deliverykotlinudemy.models.ResponseHttp
import com.optic.deliverykotlinudemy.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UsersRoutes {

    @POST("users/create")
    fun register(@Body user: User): Call<ResponseHttp>

}