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
import edu.uc.groupProject.topten.MainActivity
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
    private var countdownTime:Long = 60000  //The countdown time is 1 minute (60000 milliseconds)
    private lateinit var countDownTimer:CountDownTimer
    private var isCanceled = false
    lateinit var previousListTitle:String
    lateinit var winningItem:String
    var isVotedSharedPreference = activity?.getSharedPreferences("HasVoted", Context.MODE_PRIVATE)
    var totalPoints = 0
    val main = MainActivity()
    private lateinit var adapter : CurrentListAdapter



    //The main UI components on the main fragment are the timer, the current list recycler view, and the pop up dialog that displays when the countdown timer reaches zero.
    lateinit var timerTextView:TextView
    lateinit var recyclerView:RecyclerView
    lateinit var dialogToDisplay:ListExpirationDialog
    lateinit var listTitleLabel:TextView


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
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    /**
     * Responsible for displaying the list onto the application's recyclerview. Connects to the
     * MainViewModel.
     * @param savedInstanceState The current instance.
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        isVotedSharedPreference = activity?.getSharedPreferences("HasVoted", Context.MODE_PRIVATE)
        isCanceled = true

        //Instantiate the view model
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.firestoreService.listIncrementTime = countdownTime //Currently the countdown time on the list is 60000 milliseconds (1 min)

        //Find and store the ui components
        recyclerView = view!!.findViewById<RecyclerView>(R.id.rec_currentList)
        timerTextView  = view!!.findViewById(R.id.tv_mainListTimer)
        listTitleLabel = view!!.findViewById<TextView>(R.id.tv_mainListTitle)

        recyclerView.layoutManager =  LinearLayoutManager(this.context)
        (recyclerView.getItemAnimator() as SimpleItemAnimator).supportsChangeAnimations = false
        adapter = CurrentListAdapter(viewModel, ArrayList<ListItem>(), context!!) //Initially the recycler view adapter wil have an empty array list of list items passed to it. That changes when retrieve our data from firebase to populate the top ten list.

        viewModel.loadNextList(false)
        recyclerView.adapter = adapter

        observeTopTenListData() //Important function responsible for handling changes in the top ten list data and populating the recycler view.
        createShareListFunctionality()

    }

    /**
     * Observes the top ten list data being pulled from firestore, and populates/updates the recycler view when data is retrieved from firestore.
     */
    private fun observeTopTenListData() {
        viewModel.firestoreService.list.observe(this, Observer {

            activity?.runOnUiThread(

                Runnable {
                    val recyclerViewState: Parcelable? =
                        recyclerView.layoutManager!!.onSaveInstanceState()


                    //Checking to see if the user has not already voted.
                    if (!activity?.getSharedPreferences("HasVoted", Context.MODE_PRIVATE)!!
                            .getBoolean("HasVoted", false)
                    ) {
                        //If the user hasn't voted yet, the list remains randomized, and the vote counts hidden
                        var randomizedItems = viewModel.firestoreService.list.value
                        randomizedItems!!.shuffle()
                        adapter.setItemList(randomizedItems)

                    } else {
                        //If the user has voted, set the contents of the recycler view to be the unrandomized list, sorted ascending by vote total.
                        adapter.setItemList(viewModel.firestoreService.list.value!!)

                    }

                    adapter.setItemList(viewModel.firestoreService.list.value!!)
                    listTitleLabel.text = viewModel.firestoreService.currentList
                    previousListTitle = viewModel.firestoreService.currentList
                    winningItem = viewModel.firestoreService.list.value!![0].title //The winning list item will be the item with the most number of votes, aka position 0 in the list since items are sorted ascending by vote total.

                    recyclerView.layoutManager!!.onRestoreInstanceState(recyclerViewState)
                }
            )


            //Stops the animation from playing each time the recycler view is updated/a vote changes. We only want to play the animation when the user loads it for the first time.
            if (viewModel.playAnimation) {
                recyclerView.startLayoutAnimation()
                viewModel.playAnimation = false
                getTimeRemainingOnCurrentList()
            }
        })
    }

    /**
     * When the user clicks the share button, the current list data will be transferred over to the private tab.
     */
    private fun createShareListFunctionality() {
        var shareButton:ImageButton = view!!.findViewById<ImageButton>(R.id.btn_shareList)

        //Handling the share button click logic
        shareButton.setOnClickListener {
            var possibleList = viewModel.firestoreService.list

            if (possibleList != null) { //Sometimes the user may click the share button when the current list hasn't been populated yet.
                var bundle: Bundle = Bundle()
                var newListToAddTitle: String = viewModel.firestoreService.currentList
                var privateListFragment: PrivateListFragment = PrivateListFragment()

                bundle.putString("ListTitle", newListToAddTitle)
                privateListFragment.arguments = bundle
                activity!!.supportFragmentManager.beginTransaction() //Switching fragments from the current list to private list fragment.
                    .replace(R.id.container, privateListFragment).commit()
            }
        }
    }

    /**
     * Starts the countdown timer, and handles the logic when the countdown timer finishes.
     */
    fun startCountdownTimer(totalTimeInMilli: Long){

     countDownTimer =   object : CountDownTimer(totalTimeInMilli, 1000) { //The countdown timer counts down every 1 sec (1000 milliseconds)
            override fun onTick(millisUntilFinished: Long) {
                val hours = (millisUntilFinished / 1000 / 3600)
                val minutes = (millisUntilFinished / 1000 / 60 % 60)
                val seconds = (millisUntilFinished / 1000 % 60)

                if(seconds.toInt() == 10){ //Display a notification when the countdown timer reaches 10 seconds
                    if(this@MainFragment.fragmentManager != null && this@MainFragment.isVisible){ //Fixes a bug where our app was crashing if the user was on another fragment when this logic executed.
                        val notificationManager = NotificationManagerCompat.from(activity!!)
                        createNotificationChannel()

                        val notification = NotificationCompat.Builder(activity!!, channelId)
                            .setContentTitle("Top Ten List Expiring in 10 seconds!")
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


                    var votedOnItem = isVotedSharedPreference!!.getString("VotedOnTitle", "")
                    var positionOfYourItem =  viewModel.firestoreService.list.value!!.indexOfFirst{ item->item.title == votedOnItem } + 1


                    if(positionOfYourItem == 0){
                        totalPoints = 0
                    }
                    else if(positionOfYourItem == 1){
                        totalPoints = 200
                    }
                    else if(positionOfYourItem == 2){
                        totalPoints = 100
                    }else if(positionOfYourItem == 3){
                        totalPoints = 50
                    }else if(positionOfYourItem >= 5){
                        totalPoints = 25
                    }else{
                        totalPoints = 5
                    }

                    if(this@MainFragment.fragmentManager != null && this@MainFragment.isVisible){

                        var userPoints = activity?.findViewById<TextView>(R.id.txt_points)

                        if(userPoints?.text != "") {
                            val points: Int = Integer.parseInt(userPoints?.text as String)
                            val userTotal = totalPoints + points
                            userPoints?.text = userTotal.toString()
                            userPoints?.invalidate()
                            userPoints?.requestLayout()
                        }

                    }

                    var pastPointTotal = isVotedSharedPreference!!.getInt("TotalPoints", 0)

                    viewModel.loadNextList(true)

                    adapter.notifyDataSetChanged()

                    recyclerView.startLayoutAnimation()
                    viewModel.playAnimation = false

                    startCountdownTimer(countdownTime) //When the countdown timer finishes, start it again
                    launchListResultsDialog(
                        votedOnItem!!,
                        positionOfYourItem,
                        totalPoints,
                        pastPointTotal
                    )

                    isVotedSharedPreference!!.edit().putInt(
                        "TotalPoints",
                        pastPointTotal + totalPoints
                    )

                    isVotedSharedPreference?.edit()?.putString(
                        "ListName",
                        viewModel.firestoreService.currentList
                    )?.apply()
                    isVotedSharedPreference?.edit()?.putBoolean("HasVoted", false)?.apply()

                    adapter.lastClickedListItemTitle = ""
                    adapter.newList = true
                }
            }
        }.start()
    }


    /**
     * Launches the results dialog when the current list expires.
     */
    fun launchListResultsDialog(
        selectedItem: String,
        finalPosition: Int,
        pointsEarned: Int,
        totalPoints: Int
    ){

    try{

      dialogToDisplay = ListExpirationDialog()

      if(this.fragmentManager != null && this.isVisible()){
            var infoForDialog:Bundle = Bundle()
            infoForDialog.putString("ListTitle", previousListTitle)
            infoForDialog.putString("FirstPlace", winningItem)
            infoForDialog.putString("SelectedItem", selectedItem)
            infoForDialog.putInt("FinalPosition", finalPosition)
            infoForDialog.putInt("TotalPoints", totalPoints)
            infoForDialog.putInt("PointsEarned", pointsEarned)

            dialogToDisplay.setArguments(infoForDialog)
            dialogToDisplay.show(this.fragmentManager!!, "List Expiration Pop Up Dialog")
      }

    }catch (e: Exception){

        Log.e("List Expiration Pop-Up Dialog Error", e.toString())
    }

    }

    /**
     * Gets the time remaining on the current list from firebase. If all devices retrieve the time remaining on the current list from firebase, the devices can have their countdown timers synced up.
     */
    private fun getTimeRemainingOnCurrentList(): Long {
        val db = FirebaseFirestore.getInstance()
        var expiryDate:Date
        var path:String = "lists/" + viewModel.firestoreService.currentList
        var result:Long= 0
        var validList:Boolean = false


            db.document(path).get().addOnSuccessListener {

                expiryDate = it.getDate("expireDate")!!


                result = (TimeUnit.MILLISECONDS.convert( //The time remaining on the current list is the expiry datatime in the database minus the current datetime
                    expiryDate.time - Date().time,
                    TimeUnit.MILLISECONDS
                ))

                startCountdownTimer(result)
            }.addOnFailureListener { exception ->
                Log.d("error", "get failed with ", exception)
            }

        return result
   }

    /**
     * Creates a notification channel to display notifications when a new top ten list is loaded, and when a top ten list is about to expire.
     */
    private fun createNotificationChannel(){
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply{
            lightColor = Color.CYAN
            enableLights(true)
        }

        val manager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}