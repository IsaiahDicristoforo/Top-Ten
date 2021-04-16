package edu.uc.groupProject.topten.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import edu.uc.groupProject.topten.R
import edu.uc.groupProject.topten.SpotifyIntegration.Song
import edu.uc.groupProject.topten.SpotifyIntegration.SongService
import edu.uc.groupProject.topten.SpotifyIntegration.VolleyCallback


class ApiListFragment : Fragment() {

    private var spotifyAppRemote: SpotifyAppRemote? = null
    public val clientId = "b935841758ee436db43f7cfba5faf6f1"
    val redirectUri = "edu.uc.topten://callback"
    public val request_code = 1000
    lateinit var songService:SongService



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        songService = SongService(context!!)

        populateDropdown()


        var loggedInUserView = view!!.findViewById<TextView>(R.id.txt_loggedInUser).setText(activity!!.getSharedPreferences("SPOTIFY",0).getString("username","No Account Linked"))

        var authorizeSpotifyButton = view!!.findViewById<Button>(R.id.btn_connectToSpotify)
        authorizeSpotifyButton.setOnClickListener(){
            connect()

            val connectionParams = ConnectionParams.Builder(clientId)
                .setRedirectUri(redirectUri)
                .showAuthView(false)
                .build()

            SpotifyAppRemote.connect(context, connectionParams, object : Connector.ConnectionListener {
                override fun onConnected(appRemote: SpotifyAppRemote) {

                    spotifyAppRemote = appRemote
                    spotifyAppRemote!!.playerApi.play("spotify:playlist:37i9dQZF1DX1lVhptIYRda")
                    Log.d("MainActivity", "Connected! Yay!")

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

    fun populateDropdown(){
        val items = listOf("My Recently Played Songs","Search By Artist","Top Tracks")
        val adapter = ArrayAdapter(requireContext(), R.layout.spotify_drop_down,items)
        (view!!.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.spotifyDropDown).editText as?AutoCompleteTextView)?.setAdapter(adapter)

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
            }
        })
    }



    }






