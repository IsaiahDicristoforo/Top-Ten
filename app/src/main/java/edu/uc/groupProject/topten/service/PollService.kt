package edu.uc.groupProject.topten.service

import androidx.lifecycle.MutableLiveData
import edu.uc.groupProject.topten.dao.IPollDAO
import edu.uc.groupProject.topten.dto.Poll
import edu.uc.groupProject.topten.dto.PollChoice
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
    fun getPoll(id: Int?): MutableLiveData<PollResponse>? {
        var pollResponse = MutableLiveData<PollResponse>()
        val service = PollInstance.retrofitInstance?.create(IPollDAO::class.java)
        val call = service?.getPoll(id)

        call?.enqueue(object : Callback<PollResponse> {
            override fun onFailure(call: Call<PollResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<PollResponse>?, response: Response<PollResponse>?) {
                pollResponse.value = response?.body()
            }
        })

        return pollResponse
    }

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
                    pollResponse.value = response?.body()

            }
        })

       // Return the strawpoll
        return pollResponse
    }

    fun castVote(question_id: Int?, choice_id: Int?): MutableLiveData<PollChoice>? {
        var pollResponse = MutableLiveData<PollChoice>()
        val service = PollInstance.retrofitInstance?.create(IPollDAO::class.java)
        val call = service?.castVote(question_id, choice_id)

        call?.enqueue(object : Callback<PollChoice> {
            override fun onFailure(call: Call<PollChoice>, t: Throwable) {
            }

            override fun onResponse(call: Call<PollChoice>?, response: Response<PollChoice>?) {
                pollResponse.value = response?.body()
            }
        })

        return pollResponse
    }
}