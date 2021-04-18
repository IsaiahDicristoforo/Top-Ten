package edu.uc.groupProject.topten.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import edu.uc.groupProject.topten.R
import edu.uc.groupProject.topten.SpotifyIntegration.*
import edu.uc.groupProject.topten.dto.TopTenList
import edu.uc.groupProject.topten.service.FirestoreService
import java.util.*
import kotlin.collections.ArrayList


class ApiListFragment :RecentlyPlayedSongsAdapter.ItemListener, Fragment()  {

    private var spotifyAppRemote: SpotifyAppRemote? = null
    public val clientId = "b935841758ee436db43f7cfba5faf6f1"
    val redirectUri = "edu.uc.topten://callback"
    public val request_code = 1000
    lateinit var songService:SongService
    lateinit var spotifyPlayer: SpotifyAppRemote
    lateinit var adapter:RecentlyPlayedSongsAdapter




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }





    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        songService = SongService(context!!)


        var uploadListButton = view!!.findViewById<AppCompatImageButton>(R.id.btn_uploadSpotifyList).setOnClickListener(){
            var spotifyListTitle = view!!.findViewById<EditText>(R.id.txt_SpotifyListTitle).text.toString()

            var topTenList = TopTenList(Random().nextInt(),spotifyListTitle,"",false,"",Date(), Date())
            topTenList.listItems = adapter.itemsToAddToDatabase
            FirestoreService().writeListToDatabase(topTenList)
        }



        var authorizeSpotifyButton = view!!.findViewById<Button>(R.id.btn_connectToSpotify)
        authorizeSpotifyButton.setOnClickListener(){
            connect()

            val connectionParams = ConnectionParams.Builder(clientId)
                .setRedirectUri(redirectUri)
                .showAuthView(false)
                .build()

            SpotifyAppRemote.connect(context, connectionParams, object : Connector.ConnectionListener {
                override fun onConnected(appRemote: SpotifyAppRemote) {

                    spotifyPlayer = appRemote

                   // spotifyAppRemote = appRemote
                    //spotifyAppRemote!!.playerApi.play("spotify:album:7yDyRk7Wvvw7JM1kqV4tJf")

                    getTracks()
                }

                override fun onFailure(throwable: Throwable) {
                    // Something went wrong when attempting to connect! Handle errors here
                }
            })




        }
    }

    override fun onStop(){
        super.onStop()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_api_list, container, false)
    }

       companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ApiListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ApiListFragment().apply {

            }
    }

        fun connect() {

            var authRequest:AuthenticationRequest.Builder = AuthenticationRequest.Builder(clientId, AuthenticationResponse.Type.TOKEN, redirectUri)

            authRequest.setScopes(arrayOf("streaming", "app-remote-control","user-read-recently-played","user-library-modify","user-read-email","user-read-private"))

            var finalRequest:AuthenticationRequest = authRequest.build()
            AuthenticationClient.openLoginActivity(requireActivity(), request_code, finalRequest)

        }

    private fun getTracks() {

        var recentlyPlayedTracks:ArrayList<Song> = ArrayList<Song>()
        songService.getRecentlyPlayedTracks(object:VolleyCallback{
            override fun onSuccess() {
                recentlyPlayedTracks = songService.songs
                var l = recentlyPlayedTracks[0].name
                fillRecyclerView(recentlyPlayedTracks)
            }
        })
    }

    private fun fillRecyclerView(songs:ArrayList<Song>) {

        var recyclerViewToFill = view!!.findViewById<RecyclerView>(R.id.spotifyRecyclerView)

        adapter = RecentlyPlayedSongsAdapter(songs, this)
        recyclerViewToFill.adapter = adapter

        var layoutManager:GridLayoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL, false)
        recyclerViewToFill.layoutManager = layoutManager



    }

    override fun onItemClick(item: Song?) {

        var song = item
        spotifyPlayer.playerApi.play(song!!.uri)


        }



}






