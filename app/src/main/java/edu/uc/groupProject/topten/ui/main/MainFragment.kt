package edu.uc.groupProject.topten.ui.main

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.firestoreService.listIncrementTime = countdownTime


        recyclerView = view!!.findViewById<RecyclerView>(R.id.rec_currentList)
        var userName = view!!.findViewById<TextView>(R.id.username)
        var userPoints = view!!.findViewById<TextView>(R.id.points)
        timerTextView  = view!!.findViewById(R.id.tv_mainListTimer)
        var listTitleLabel = view!!.findViewById<TextView>(R.id.tv_mainListTitle)
        //userName.text = viewModel.getUserName()
        //userPoints.text = viewModel.getUserPoints()


    //    viewModel.firestoreService.resetExpirationDateOnAllLists(countdownTime.toInt())


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


        adapter = CurrentListAdapter(viewModel, testList)
        recyclerView.adapter = adapter



        viewModel.firestoreService.list.observe(this, Observer {

            activity?.runOnUiThread(

                Runnable {
                    val recyclerViewState: Parcelable? =
                        recyclerView.layoutManager!!.onSaveInstanceState()

                    adapter.setItemList(viewModel.firestoreService.list.value!!)

                    listTitleLabel.text = viewModel.firestoreService.currentList

                    recyclerView.layoutManager!!.onRestoreInstanceState(recyclerViewState)
                }
            )

            // adapter = CurrentListAdapter(viewModel, viewModel.firestoreService.list.value!!)

            //adapter = CurrentListAdapter(viewModel, viewModel.firestoreService.list.value!!)
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

                    viewModel.loadNextList(true)

                    adapter.notifyDataSetChanged()

                    recyclerView.startLayoutAnimation()

                    viewModel.playAnimation = false

                    startCountdownTimer(countdownTime)

                }

                }





        }.start()


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