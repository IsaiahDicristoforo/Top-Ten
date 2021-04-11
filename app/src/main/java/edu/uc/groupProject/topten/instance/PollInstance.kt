package edu.uc.groupProject.topten.instance

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
* Retrofit instance manages HTTP requests
*/
object PollInstance {
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://polls.apiblueprint.org/"

    val retrofitInstance: Retrofit? get() {
        // HttpLogging for Logcat
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client) // Client for Logcat (to be commented out with logger)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return retrofit
    }
}