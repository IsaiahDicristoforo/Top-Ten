package edu.uc.groupProject.topten.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import edu.uc.groupProject.topten.R
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;
import edu.uc.groupProject.topten.ListGeneration


class ApiListFragment : Fragment() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var authorizeSpotifyButton = view!!.findViewById<Button>(R.id.btn_connectToSpotify)
        authorizeSpotifyButton.setOnClickListener(){
            SpotifyService.connect(activity!!.applicationContext){

            }

        }
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
                    SpotifyService.spotifyAppRemote!!.playerApi.play("spotify:playlist:37i9dQZF1DX1lVhptIYRda")
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