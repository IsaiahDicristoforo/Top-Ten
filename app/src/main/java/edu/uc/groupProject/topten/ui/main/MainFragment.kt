package edu.uc.groupProject.topten.ui.main

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.firebase.firestore.FirebaseFirestore
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
    var isVotedSharedPreference = activity?.getSharedPreferences("HasVoted",Context.MODE_PRIVATE)
    var firestoreService : FirestoreService =  FirestoreService()



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


        isVotedSharedPreference = activity?.getSharedPreferences("HasVoted",Context.MODE_PRIVATE)
       // isVotedSharedPreference?.edit()?.putBoolean("HasVoted", false)?.apply()


        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.firestoreService.listIncrementTime = countdownTime



        recyclerView = view!!.findViewById<RecyclerView>(R.id.rec_currentList)
        var userName = view!!.findViewById<TextView>(R.id.txt_username)
        var userPoints = view!!.findViewById<TextView>(R.id.txt_points)
        timerTextView  = view!!.findViewById(R.id.tv_mainListTimer)
        var listTitleLabel = view!!.findViewById<TextView>(R.id.tv_mainListTitle)
        //userName.text = firestoreService.getUserName()
      //  userPoints.text = firestoreService.getUserPoints()


    //    viewModel.firestoreService.resetExpirationDateOnAllLists(countdownTime.toInt())

        createShareListFunctionality()


        recyclerView.layoutManager =  LinearLayoutManager(this.context)
        (recyclerView.getItemAnimator() as SimpleItemAnimator).supportsChangeAnimations = false

        viewModel.loadNextList(false)

     /*   var listItemsToAdd = ArrayList<ListItem>()
        listItemsToAdd.add(ListItem(0,"Cinnamon Toast Crunch","",0))
        listItemsToAdd.add(ListItem(1,"Lucky Charms","",0))
        listItemsToAdd.add(ListItem(2,"Froot Loops","",0))
        listItemsToAdd.add(ListItem(3,"Cheerios","",0))
        listItemsToAdd.add(ListItem(4,"Frosted Flakes","",0))
        listItemsToAdd.add(ListItem(5,"Honeycomb","",0))
        listItemsToAdd.add(ListItem(6,"Cap'n Crunch","",0))
        listItemsToAdd.add(ListItem(6,"Reese's Puffs","",0))

        var list: TopTenList = TopTenList(
            1,
            "Top Ten Cereals",
            "A list of favorite cereals",
            false,
            "Food",
            Date(),
            Date()
        )
        list.listItems = listItemsToAdd
        viewModel.firestoreService.writeListToDatabase(list)
      */


        adapter = CurrentListAdapter(viewModel, testList, context!!)
        recyclerView.adapter = adapter



        viewModel.firestoreService.list.observe(this, Observer {

            activity?.runOnUiThread(

                Runnable {
                    val recyclerViewState: Parcelable? =
                        recyclerView.layoutManager!!.onSaveInstanceState()


                    if(!activity?.getSharedPreferences("HasVoted",Context.MODE_PRIVATE)!!.getBoolean("HasVoted", false)){

                        var randomizedItems = viewModel.firestoreService.list.value
                        randomizedItems!!.shuffle()
                        adapter.setItemList(randomizedItems)

                    }else{
                        adapter.setItemList(viewModel.firestoreService.list.value!!)

                    }
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
        shareButton.setOnClickListener({

            var possibleList = viewModel.firestoreService.list
            if(possibleList != null){
                var privateListFragment:PrivateListFragment = PrivateListFragment()

                var newListToAddTitle:String = "Test123"

                var bundle:Bundle = Bundle()
                bundle.putString("ListTitle",newListToAddTitle)
                privateListFragment.arguments = bundle
                activity!!.supportFragmentManager.beginTransaction().replace(R.id.container, privateListFragment).commit()
            }

        })
    }


    fun startCountdownTimer(totalTimeInMilli: Long){

     countDownTimer =   object : CountDownTimer(totalTimeInMilli, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                val hours = (millisUntilFinished / 1000 / 3600)
                val minutes = (millisUntilFinished / 1000 / 60 % 60)
                val seconds = (millisUntilFinished / 1000 % 60)
                timerTextView.setText("${hours} hrs  ${minutes} min  ${seconds} sec")
            }

            override fun onFinish() {

                if(isCanceled){
                    timerTextView.setText("Voting is Closed On The List!")

                    var votedOnItem = isVotedSharedPreference!!.getString("VotedOnTitle","")
                    var positionOfYourItem =  viewModel.firestoreService.list.value!!.indexOfFirst{ item->item.title == votedOnItem } + 1

                    var totalPoints = 0
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

                    var pastPointTotal = isVotedSharedPreference!!.getInt("TotalPoints",0)



                    viewModel.loadNextList(true)

                    adapter.notifyDataSetChanged()
                    recyclerView.startLayoutAnimation()
                    viewModel.playAnimation = false
                    startCountdownTimer(countdownTime)
                    launchListResultsDialog(votedOnItem!!,positionOfYourItem,totalPoints,pastPointTotal)

                    isVotedSharedPreference!!.edit().putInt("TotalPoints",pastPointTotal + totalPoints)

                    isVotedSharedPreference?.edit()?.putString("ListName",viewModel.firestoreService.currentList )?.apply()
                    isVotedSharedPreference?.edit()?.putBoolean("HasVoted", false)?.apply()

                    adapter.lastClickedListItemTitle = ""
                    adapter.newList = true

                }

                }

        }.start()


    }

    fun launchListResultsDialog(selectedItem:String, finalPosition:Int, pointsEarned:Int, totalPoints:Int ){

    try{

      dialogToDisplay = ListExpirationDialog()

      if(this.fragmentManager != null && this.isVisible()){
        var infoForDialog:Bundle = Bundle()
        infoForDialog.putString("ListTitle", previousListTitle)
        infoForDialog.putString("FirstPlace",winningItem)
          infoForDialog.putString("SelectedItem",selectedItem)
          infoForDialog.putInt("FinalPosition",finalPosition)
          infoForDialog.putInt("TotalPoints", totalPoints)
          infoForDialog.putInt("PointsEarned", pointsEarned)


        dialogToDisplay.setArguments(infoForDialog)
          dialogToDisplay.show(this.fragmentManager!!, "List Expiration Pop Up Dialog")
      }

    }catch(e: Exception){



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


}