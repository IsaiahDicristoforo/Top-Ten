package edu.uc.groupProject.topten.SpotifyIntegration

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class SongService(var context:Context){

    var songs:ArrayList<Song>
    var sharedPreferences: SharedPreferences
    var queue: RequestQueue

init{

    sharedPreferences = context.getSharedPreferences("SPOTIFY",0)
    queue = Volley.newRequestQueue(context)
    songs = ArrayList<Song>()

}

    fun getRecentlyPlayedTracks(callBack:VolleyCallback):ArrayList<Song>{
        var endpoint:String = "https://api.spotify.com/v1/me/player/recently-played"
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET,endpoint, null,
            Response.Listener { response: JSONObject ->
                val gson = Gson()
                var jsonArray: JSONArray? = response.optJSONArray("items")

                var counter:Int = 0
                while(counter < jsonArray!!.length()){

                    try {

                        var jObject: JSONObject? = jsonArray.getJSONObject(counter)
                        jObject = jObject?.optJSONObject("track")

                        var song = gson.fromJson(jObject.toString(),Song::class.java)
                        songs.add(song)

                    }catch(exception:Exception){

                        Log.d(TAG, "getRecentlyPlayedTracks: " + exception)

                    }


                    counter++
                }


                callBack.onSuccess()
            },
            Response.ErrorListener { error: VolleyError? ->

                Log.d("TESTING", "getRecentlyPlayedTracks: " + error)

             }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                val token: String = sharedPreferences.getString("token", "")!!
                val auth = "Bearer $token"
                headers["Authorization"] = auth
                return headers
            }
        }

        queue.add(jsonObjectRequest)

        return songs
    }

}