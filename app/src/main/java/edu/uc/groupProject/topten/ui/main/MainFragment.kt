package edu.uc.groupProject.topten.ui.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.firebase.firestore.FirebaseFirestore
import edu.uc.groupProject.topten.R
import edu.uc.groupProject.topten.dto.ListItem
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * MainFragment class.
 *
 * Responsible for creating the view, and additionally contains Observe functionality that
 * observes live data coming in from MainViewModel
 */
class MainFragment : Fragment() {
    //Variables to connect to the MainViewModel in the onActivityCreated() function.
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter : CurrentListAdapter
    private var countdownTime:Long = 30000
    lateinit var timerTextView:TextView
    lateinit var recyclerView:RecyclerView
    var testList = ArrayList<ListItem>()
    private lateinit var countDownTimer:CountDownTimer
    private var isCanceled = false
    lateinit var dialogToDisplay:ListExpirationDialog
    lateinit var previousListTitle:String
    lateinit var winningItem:String

    //Notification code
    private val channelId = "edu.uc.groupProject.topten"
    private val channelName = "Top Ten Channel"
    private var notificationId = 0

    /**
     * Creates the view.
     * @param inflater The layout inflater
     * @param container the main view that contains sub-views
     * @param savedInstanceState The current instance.
     * @return The layout of the application's UI.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    /**
     * Responsible for displaying the list onto the application's recyclerview. Connects to the
     * MainViewModel.
     * @param savedInstanceState The current instance.
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.firestoreService.listIncrementTime = countdownTime

        recyclerView = view!!.findViewById<RecyclerView>(R.id.rec_currentList)
        var userName = view!!.findViewById<TextView>(R.id.username)
        var userPoints = view!!.findViewById<TextView>(R.id.points)
        timerTextView  = view!!.findViewById(R.id.tv_mainListTimer)
        var listTitleLabel = view!!.findViewById<TextView>(R.id.tv_mainListTitle)

        createShareListFunctionality()

        recyclerView.layoutManager =  LinearLayoutManager(this.context)
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        viewModel.loadNextList(false)
        adapter = CurrentListAdapter(viewModel, testList)
        recyclerView.adapter = adapter

        viewModel.firestoreService.list.observe(this, Observer {
            activity?.runOnUiThread( Runnable {
                val recyclerViewState: Parcelable? = recyclerView.layoutManager!!.onSaveInstanceState()
                adapter.setItemList(viewModel.firestoreService.list.value!!)
                listTitleLabel.text = viewModel.firestoreService.currentList
                previousListTitle = viewModel.firestoreService.currentList
                winningItem = viewModel.firestoreService.list.value!![0].title

                recyclerView.layoutManager!!.onRestoreInstanceState(recyclerViewState)
            })

            if (viewModel.playAnimation) {
                recyclerView.startLayoutAnimation()
                viewModel.playAnimation = false

                getTimeRemainingOnCurrentList()
            }
        })

        isCanceled = true
    }

    private fun createShareListFunctionality() {
        var shareButton:ImageButton = view!!.findViewById<ImageButton>(R.id.btn_shareList)
        shareButton.setOnClickListener {
            var possibleList = viewModel.firestoreService.list

            if (possibleList != null) {
                var bundle: Bundle = Bundle()
                var newListToAddTitle: String = "Test123"
                var privateListFragment: PrivateListFragment = PrivateListFragment()

                bundle.putString("ListTitle", newListToAddTitle)
                privateListFragment.arguments = bundle
                activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.container, privateListFragment).commit()
            }
        }
    }

    fun startCountdownTimer(totalTimeInMilli: Long){
        countDownTimer = object : CountDownTimer(totalTimeInMilli, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hours = (millisUntilFinished / 1000 / 3600)
                val minutes = (millisUntilFinished / 1000 / 60 % 60)
                val seconds = (millisUntilFinished / 1000 % 60)
                timerTextView.text = "${hours} hrs  ${minutes} min  ${seconds} sec"

                if(seconds.toInt() == 10){
                    if(this@MainFragment.fragmentManager != null && this@MainFragment.isVisible){
                        val notificationManager = NotificationManagerCompat.from(activity!!)
                        createNotificationChannel()

                        val notification = NotificationCompat.Builder(activity!!, channelId)
                            .setContentTitle("Top Ten List Expiring in 10 secs !")
                            .setContentText("Last Chance to vote on your favorite List Item.")
                            .setSmallIcon(R.mipmap.ic_launcher_round)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .build()

                        notificationManager.notify(notificationId, notification)
                    }
                }

                timerTextView.text = "VOTING ENDS: ${hours} hrs  ${minutes} min  ${seconds} sec"
            }

            override fun onFinish() {
                if(isCanceled){
                    if(this@MainFragment.fragmentManager != null && this@MainFragment.isVisible){
                        val notificationManager = NotificationManagerCompat.from(activity!!)
                        createNotificationChannel()
                        val notification = NotificationCompat.Builder(activity!!, channelId)
                            .setContentTitle("New list is on its way...!")
                            .setContentText("Go vote on your favorite list item.")
                            .setSmallIcon(R.mipmap.ic_launcher_round)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .build()

                        notificationManager.notify(notificationId, notification)
                    }

                    timerTextView.text = "Voting is Closed On The List!"
                    viewModel.loadNextList(true)
                    adapter.notifyDataSetChanged()
                    recyclerView.startLayoutAnimation()
                    viewModel.playAnimation = false
                    startCountdownTimer(countdownTime)

                    launchListResultsDialog()
                }
            }
        }.start()
    }

    fun launchListResultsDialog(){
        try{
            dialogToDisplay = ListExpirationDialog()

            if(this.fragmentManager != null && this.isVisible){
                var infoForDialog:Bundle = Bundle()
                infoForDialog.putString("ListTitle", previousListTitle)
                infoForDialog.putString("FirstPlace",winningItem)
                dialogToDisplay.arguments = infoForDialog
                dialogToDisplay.show(this.fragmentManager!!, "List Expiration Pop Up Dialog")
            }
        }catch(e: Exception){
            Log.d("Exception", "Exception occurred!")
        }
    }

    private fun getTimeRemainingOnCurrentList(): Long {
        val db = FirebaseFirestore.getInstance()
        var expiryDate:Date
        var path:String = "lists/" + viewModel.firestoreService.currentList
        var result:Long= 0
        var validList:Boolean = false

        db.document(path).get().addOnSuccessListener {
            expiryDate = it.getDate("expireDate")!!
            result = (TimeUnit.MILLISECONDS.convert (expiryDate.time- Date().time, TimeUnit.MILLISECONDS))
            startCountdownTimer(result)
        }.addOnFailureListener { exception ->
            Log.d("error", "get failed with ", exception)
        }

        return result
    }

    private fun createNotificationChannel(){
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply{
            lightColor = Color.CYAN
            enableLights(true)
        }

        val manager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}