package edu.uc.groupProject.topten.DAO

import edu.uc.groupProject.topten.DTO.Strawpoll
import retrofit2.Call
import retrofit2.http.GET

interface IStrawpollDAO {
    @GET("/polls/")
    fun getStrawpoll(id: Int): Call<Strawpoll>
}