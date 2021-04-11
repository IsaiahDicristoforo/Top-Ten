package edu.uc.groupProject.topten.service

import androidx.lifecycle.MutableLiveData
import edu.uc.groupProject.topten.dao.IPollDAO
import edu.uc.groupProject.topten.dto.Poll
import edu.uc.groupProject.topten.dto.PollResponse
import edu.uc.groupProject.topten.instance.PollInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Handles fetching strawpoll
 *
 * Makes use of the Retrofit instance
 */
class PollService {
    /*fun getStrawpoll(id: Int): MutableLiveData<Strawpoll>? {
        var strawpoll = MutableLiveData<Strawpoll>()
        val service = PollInstance.retrofitInstance?.create(IPollDAO::class.java)
        val call = service?.getStrawpoll(id)

        call?.enqueue(object : Callback<Strawpoll> {
            override fun onFailure(call: Call<Strawpoll>, t: Throwable) {
            }

            override fun onResponse(call: Call<Strawpoll>?, response: Response<Strawpoll>?) {
                strawpoll.value = response?.body()
            }
        })

        return strawpoll
    }*/

    fun createPoll(question: String, choices: ArrayList<String>): MutableLiveData<PollResponse>?{
        // Create our response object and poll instance
        var pollResponse = MutableLiveData<PollResponse>()
        val service = PollInstance.retrofitInstance?.create(IPollDAO::class.java)

        // Create our poll post object
        var poll = Poll(question=question, choices=choices)

        // Call our POST endpoint
        val call = service?.createPoll(poll)

        // Enqueue the call
        call?.enqueue(object : Callback<PollResponse> {
            override fun onFailure(bigcall: Call<PollResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<PollResponse>?, response: Response<PollResponse>?) {
                if(response?.code() == 200){
                    pollResponse.value = response?.body()
                }

            }
        })

       // Return the strawpoll
        return pollResponse
    }
}