package edu.uc.groupProject.topten

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.material.tabs.TabLayout
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationResponse
import edu.uc.groupProject.topten.SpotifyIntegration.User
import edu.uc.groupProject.topten.SpotifyIntegration.UserService
import edu.uc.groupProject.topten.SpotifyIntegration.VolleyCallback
import edu.uc.groupProject.topten.ui.main.SectionsPagerAdapter

class ListGeneration : AppCompatActivity() {

    private lateinit var editor:SharedPreferences.Editor
    private lateinit var sharedPreferences:SharedPreferences
    protected lateinit var queue:RequestQueue
    private var spotifyAppRemote: SpotifyAppRemote? = null
    public val clientId = "b935841758ee436db43f7cfba5faf6f1"
    val redirectUri = "edu.uc.topten://callback"
    public val request_code = 1000


   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_generation)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

       sharedPreferences = getSharedPreferences("SPOTIFY",0)
       queue = Volley.newRequestQueue(this)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(request_code == requestCode){
            var response  = AuthenticationClient.getResponse(resultCode, data)




            if(response.type == AuthenticationResponse.Type.TOKEN){

                var value =  response.accessToken

                editor = getSharedPreferences("SPOTIFY",0).edit()
                editor.putString("token",response.accessToken).apply()

                waitForUserInfo()

            }else{

            }
        }

    }

    override fun onStart() {
        super.onStart()
    }

    fun waitForUserInfo(){

        var userService:UserService = UserService(queue,sharedPreferences)

        userService.getUserInfo(object : VolleyCallback {
            override fun onSuccess() {
                val user: User = userService.user
                editor = getSharedPreferences("SPOTIFY", 0).edit()
                editor.putString("userid", user.id)
                editor.putString("username", user.displayName)
                editor.commit()

            }
        })



    }


    }


