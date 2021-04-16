package edu.uc.groupProject.topten.SpotifyIntegration

import android.content.SharedPreferences
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import org.json.JSONObject


class UserService(val requestQueue: RequestQueue, val sharedPrefs:SharedPreferences) {
    private val endpoint:String = "https://api.spotify.com/v1/me"
    lateinit var user:User

    fun getUserInfo(callBack:VolleyCallback){

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(endpoint, null,
            Response.Listener { response: JSONObject ->
                val gson = Gson()
                user = gson.fromJson(
                    response.toString(),
                    User::class.java
                )
                callBack.onSuccess()
            },
            Response.ErrorListener { error: VolleyError? ->  {} }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                val token: String = sharedPrefs.getString("token", "")!!
                val auth = "Bearer $token"
                headers["Authorization"] = auth
                return headers
            }

        }

        requestQueue.add(jsonObjectRequest)
        }



    }






