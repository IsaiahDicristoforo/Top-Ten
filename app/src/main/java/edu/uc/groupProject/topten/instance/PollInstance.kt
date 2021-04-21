package edu.uc.groupProject.topten.instance

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
* Retrofit instance manages HTTP requests
*/
object PollInstance {
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://polls.apiblueprint.org/"

    val retrofitInstance: Retrofit? get() {

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return retrofit
    }
}