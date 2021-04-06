package edu.uc.groupProject.topten.service

import androidx.lifecycle.MutableLiveData
import edu.uc.groupProject.topten.dao.IStrawpollDAO
import edu.uc.groupProject.topten.dto.Strawpoll
import edu.uc.groupProject.topten.instance.StrawpollInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Handles fetching strawpoll
 *
 * Makes use of the Retrofit instance
 */
class StrawpollService {

    fun getStrawpoll(id: Int): MutableLiveData<Strawpoll>? {
        var strawpoll = MutableLiveData<Strawpoll>()
        val service = StrawpollInstance.retrofitInstance?.create(IStrawpollDAO::class.java)
        val call = service?.getStrawpoll(id)

        call?.enqueue(object : Callback<Strawpoll> {
            override fun onFailure(call: Call<Strawpoll>, t: Throwable) {
            }

            override fun onResponse(call: Call<Strawpoll>?, response: Response<Strawpoll>?) {
                strawpoll.value = response?.body()
            }
        })
        return strawpoll
    }

   fun createStrawpoll(title: String, options: ArrayList<String>, multi: Boolean = true): MutableLiveData<Strawpoll>?{
       // Create our response object and strawpoll instance
        var strawpoll = MutableLiveData<Strawpoll>()
        val service = StrawpollInstance.retrofitInstance?.create(IStrawpollDAO::class.java)

       // Create our strawpoll post object
        var poll = Strawpoll(title=title, options=options, multi=multi)

       // Call our POST endpoint
        val call = service?.createStrawpoll(poll)

       // Enqueue the call
        call?.enqueue(object : Callback<Strawpoll> {
            override fun onFailure(bigcall: Call<Strawpoll>, t: Throwable) {
                println(t.message)
            }

            override fun onResponse(call: Call<Strawpoll>?, response: Response<Strawpoll>?) {
                if(response?.code() == 200){
                    strawpoll.value = response?.body()
                }

            }
        })

       // Return the strawpoll
        return strawpoll
    }
}