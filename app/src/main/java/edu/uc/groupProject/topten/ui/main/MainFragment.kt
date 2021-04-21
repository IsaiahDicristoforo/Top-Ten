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
import androidx.appcompat.app.AppCompatActivity
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
import edu.uc.groupProject.topten.service.FirestoreService
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
    var isVotedSharedPreference = activity?.getSharedPreferences("HasVoted", Context.MODE_PRIVATE)
    var firestoreService : FirestoreService =  FirestoreService()
    var totalPoints = 0
    val main = MainActivity()



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
       // isVotedSharedPreference?.edit()?.putBoolean("HasVoted", false)?.apply()


        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.firestoreService.listIncrementTime = countdownTime


        recyclerView = view!!.findViewById<RecyclerView>(R.id.rec_currentList)
        var userName = view!!.findViewById<TextView>(R.id.txt_username)
        var userPoints = view!!.findViewById<TextView>(R.id.txt_points)
        timerTextView  = view!!.findViewById(R.id.tv_mainListTimer)
        var listTitleLabel = view!!.findViewById<TextView>(R.id.tv_mainListTitle)
        //userName.text = viewModel.getUserName()
        //userPoints.text = viewModel.getUserPoints()


    //    viewModel.firestoreService.resetExpirationDateOnAllLists(countdownTime.toInt())

        createShareListFunctionality()


        recyclerView.layoutManager =  LinearLayoutManager(this.context)
        (recyclerView.getItemAnimator() as SimpleItemAnimator).supportsChangeAnimations = false

        viewModel.loadNextList(false)
        adapter = CurrentListAdapter(viewModel, testList, context!!)
        recyclerView.adapter = adapter


        viewModel.firestoreService.list.observe(this, Observer {

            activity?.runOnUiThread(

                Runnable {
                    val recyclerViewState: Parcelable? =
                        recyclerView.layoutManager!!.onSaveInstanceState()


                    if (!activity?.getSharedPreferences("HasVoted", Context.MODE_PRIVATE)!!
                            .getBoolean(
                                "HasVoted",
                                false
                            )
                    ) {

                        var randomizedItems = viewModel.firestoreService.list.value
                        randomizedItems!!.shuffle()
                        adapter.setItemList(randomizedItems)

                    } else {
                        adapter.setItemList(viewModel.firestoreService.list.value!!)

                    }
                    adapter.setItemList(viewModel.firestoreService.list.value!!)
                    listTitleLabel.text = viewModel.firestoreService.currentList
                    previousListTitle = viewModel.firestoreService.currentList
                    winningItem = viewModel.firestoreService.list.value!![0].title

                    recyclerView.layoutManager!!.onRestoreInstanceState(recyclerViewState)
                }
            )


            //Stops the animation from playing each time the recycler view is updated/a vote changes
            if (viewModel.playAnimation) {
                recyclerView.startLayoutAnimation()
                viewModel.playAnimation = false

                getTimeRemainingOnCurrentList()
            }
        })
        isCanceled = true
        viewModel.fetchStrawpoll(1)
    }

    private fun createShareListFunctionality() {
        var shareButton:ImageButton = view!!.findViewById<ImageButton>(R.id.btn_shareList)
        shareButton.setOnClickListener {
            var possibleList = viewModel.firestoreService.list

            if (possibleList != null) {
                var bundle: Bundle = Bundle()
                var newListToAddTitle: String = viewModel.firestoreService.currentList
                var privateListFragment: PrivateListFragment = PrivateListFragment()

                bundle.putString("ListTitle", newListToAddTitle)
                privateListFragment.arguments = bundle
                activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.container, privateListFragment).commit()
            }
        }
    }

    fun startCountdownTimer(totalTimeInMilli: Long){

     countDownTimer =   object : CountDownTimer(totalTimeInMilli, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hours = (millisUntilFinished / 1000 / 3600)
                val minutes = (millisUntilFinished / 1000 / 60 % 60)
                val seconds = (millisUntilFinished / 1000 % 60)
                timerTextView.setText("${hours} hrs  ${minutes} min  ${seconds} sec")

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


                    var votedOnItem = isVotedSharedPreference!!.getString("VotedOnTitle", "")
                    var positionOfYourItem =  viewModel.firestoreService.list.value!!.indexOfFirst{ item->item.title == votedOnItem } + 1


                    if(positionOfYourItem == 1){
                        totalPoints = 200
                    }
                    else if(positionOfYourItem == 2){
                        totalPoints = 100
                    }else if(positionOfYourItem == 3){
                        totalPoints = 50
                    }else if(positionOfYourItem >= 5){
                        totalPoints = 25
                    }else{
                        totalPoints += 5
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
                    val firestore = FirestoreService()

                    firestore.updatePoints(totalPoints)

                    viewModel.loadNextList(true)

                    adapter.notifyDataSetChanged()

                    recyclerView.startLayoutAnimation()

                    viewModel.playAnimation = false

                    startCountdownTimer(countdownTime)
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


                result = (TimeUnit.MILLISECONDS.convert(
                    expiryDate.time - Date().time,
                    TimeUnit.MILLISECONDS
                ))

                startCountdownTimer(result)
            }.addOnFailureListener { exception ->
                Log.d("error", "get failed with ", exception)
            }

        return result



   }

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