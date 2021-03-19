package edu.uc.groupProject.topten.ui.main

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Parcelable
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
import edu.uc.groupProject.topten.R
import edu.uc.groupProject.topten.dto.ListItem
import java.util.concurrent.TimeUnit


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

        var recyclerView = view!!.findViewById<RecyclerView>(R.id.rec_currentList)
        var userName = view!!.findViewById<TextView>(R.id.username)
        var userPoints = view!!.findViewById<TextView>(R.id.points)

        recyclerView.layoutManager =  LinearLayoutManager(this.context)
        (recyclerView.getItemAnimator() as SimpleItemAnimator).supportsChangeAnimations = false

        //userName.text = viewModel.getUserName()
        //userPoints.text = viewModel.getUserPoints()

        viewModel.loadNextList()

        var testList = ArrayList<ListItem>()
        adapter = CurrentListAdapter(viewModel, testList)
        recyclerView.adapter = adapter



        viewModel.firestoreService.list.observe(this, Observer {

            activity?.runOnUiThread(

                Runnable {
                    val recyclerViewState: Parcelable? =
                        recyclerView.layoutManager!!.onSaveInstanceState()
                    adapter.setItemList(viewModel.firestoreService.list.value!!)
                    recyclerView.layoutManager!!.onRestoreInstanceState(recyclerViewState)
                }
            )


            // adapter = CurrentListAdapter(viewModel, viewModel.firestoreService.list.value!!)

            //adapter = CurrentListAdapter(viewModel, viewModel.firestoreService.list.value!!)
            //Stops the animation from playing each time the recycler view is updated/a vote changes
            if (viewModel.playAnimation) {
                recyclerView.startLayoutAnimation()
                viewModel.playAnimation = false

            }


        })




        startCountdownTimer(300000)


        viewModel.fetchStrawpoll(1)
    }

    fun startCountdownTimer(totalTimeInMilli: Long){

        var timerTextView:TextView = view!!.findViewById(R.id.tv_mainListTimer)

        object : CountDownTimer(totalTimeInMilli, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                val hours = (millisUntilFinished / 1000 / 3600)
                val minutes = (millisUntilFinished / 1000 / 60 % 60)
                val seconds = (millisUntilFinished / 1000 % 60)
                timerTextView.setText("VOTING ENDS: ${hours} hrs  ${minutes} min  ${seconds} sec")
            }

            override fun onFinish() {
                timerTextView.setText("Voting is Closed On The List!")
                viewModel.loadNextList()

            }
        }.start()

    }


}