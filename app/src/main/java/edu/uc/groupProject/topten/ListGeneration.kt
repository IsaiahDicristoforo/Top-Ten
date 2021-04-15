package edu.uc.groupProject.topten

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import edu.uc.groupProject.topten.ui.main.SectionsPagerAdapter
import edu.uc.groupProject.topten.ui.main.Test1

class ListGeneration : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_generation)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

    }








    override fun onStart() {
        super.onStart()

        SpotifyService.connect(this){
            val intent = Intent(this, Test1::class.java)
            startActivity(intent)
        }





    }

    object SpotifyService {

        private val clientId = "b935841758ee436db43f7cfba5faf6f1"
        private val redirectUri = "edu.uc.topten://callback"

        private var spotifyAppRemote: SpotifyAppRemote? = null
        private var connectionParams: ConnectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()


        fun connect(context: Context, handler: (connected: Boolean) -> Unit) {
            if (spotifyAppRemote?.isConnected == true) {
                handler(true)
                return
            }
            val connectionListener = object : Connector.ConnectionListener {
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    this@SpotifyService.spotifyAppRemote = spotifyAppRemote
                    handler(true)
                }
                override fun onFailure(throwable: Throwable) {
                    Log.e("SpotifyService", throwable.message, throwable)
                    handler(false)
                }
            }
            SpotifyAppRemote.connect(context, connectionParams, connectionListener)
        }

    }


}