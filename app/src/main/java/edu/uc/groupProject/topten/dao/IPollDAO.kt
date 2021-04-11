package edu.uc.groupProject.topten.dao

import edu.uc.groupProject.topten.dto.Poll
import edu.uc.groupProject.topten.dto.PollResponse
import retrofit2.Call
import retrofit2.http.*

interface IPollDAO {
    @Headers("Content-Type: application/json", "Accept:application/json")
    @GET("questions/{question_id}")
    fun getPoll (@Path("question_id") question_id: Int?): Call<PollResponse>

    @Headers("Content-Type: application/json", "Accept:application/json")
    @POST("questions?page=1")
    fun createPoll(@Body poll: Poll): Call<PollResponse>
}