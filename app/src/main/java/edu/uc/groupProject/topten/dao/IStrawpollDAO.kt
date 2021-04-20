package edu.uc.groupProject.topten.dao

import edu.uc.groupProject.topten.dto.Strawpoll
import retrofit2.Call
import retrofit2.http.*

interface IStrawpollDAO {
    @GET("polls/{id}")
    fun getStrawpoll(@Path("id") id: Int): Call<Strawpoll>

    @Headers("Content-Type: application/json", "Accept:application/json")
    @POST("polls")
    fun createStrawpoll(@Body poll: Strawpoll): Call<Strawpoll>
}