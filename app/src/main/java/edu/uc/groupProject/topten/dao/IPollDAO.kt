package edu.uc.groupProject.topten.dao

import edu.uc.groupProject.topten.dto.Poll
import edu.uc.groupProject.topten.dto.PollChoice
import edu.uc.groupProject.topten.dto.PollResponse
import retrofit2.Call
import retrofit2.http.*

interface IPollDAO {
    @Headers("Accept: application/json")
    @GET("questions/{question_id}")
    fun getPoll (@Path("question_id") question_id: Int?): Call<PollResponse>

    @Headers("Accept: application/json")
    @POST("questions/{question_id}/choices/{choice_id}")
    fun castVote(@Path("question_id") question_id: Int?, @Path("choice_id") choice_id: Int?): Call<PollChoice>

    @Headers("Accept: application/json")
    @POST("questions?page=1")
    fun createPoll(@Body poll: Poll): Call<PollResponse>


}