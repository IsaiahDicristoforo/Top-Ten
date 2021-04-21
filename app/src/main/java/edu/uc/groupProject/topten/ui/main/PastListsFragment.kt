package edu.uc.groupProject.topten.ui.main

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import edu.uc.groupProject.topten.R
import edu.uc.groupProject.topten.dto.ListItem

//Handles the past_lists_fragment
class PastListsFragment : Fragment() {
    private lateinit var viewModel: PastListsViewModel
    private lateinit var adapter : PastListAdapter
    var testList = ArrayList<ListItem>()
    lateinit var recyclerView: RecyclerView
    lateinit var spinnerList : Spinner //spinner variable
    lateinit var thing : String


    companion object {
        fun newInstance() = PastListsFragment()
    }

    /**
     * Creates the view.
     * @param inflater The layout inflater
     * @param container the main view that contains sub-views
     * @param savedInstanceState The current instance.
     * @return The layout of the application's UI.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.past_lists_fragment, container, false)
    }

    /*
     * onActivityCreated function.
     * Responsible for populating the past-list tab's drop-down spinner item.
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PastListsViewModel::class.java)

        recyclerView = view!!.findViewById<RecyclerView>(R.id.rec_pastList)
        spinnerList = view!!.findViewById<Spinner>(R.id.spn_listNames)
        recyclerView.layoutManager =  LinearLayoutManager(this.context)
        (recyclerView.getItemAnimator() as SimpleItemAnimator).supportsChangeAnimations = false
        viewModel.loadNextList("Top Ten Car Brands")
        adapter = PastListAdapter(viewModel, testList, context!!)
        recyclerView.adapter = adapter

        viewModel.firestoreService.fetchListNames() //fetches all the list names

        //Observer loop. This is where the drop-down box gets populated.
        viewModel.firestoreService.listOfLists.observe(this, Observer{
            var listOfListVariable = viewModel.firestoreService.arrayOfLists //may cause issues with observe function

            //Mini-adapter
            var spinAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOfListVariable)
            spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerList.adapter = spinAdapter

            spinnerList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener  {

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val text = parent?.getItemAtPosition(position).toString()
                    thing = text
                    viewModel.firestoreService.pastListSelected = thing
                    //recyclerView.layoutManager!!.onRestoreInstanceState(recyclerViewState)
                    viewModel.loadNextList(thing)
                    recyclerView.adapter?.notifyDataSetChanged()
                    recyclerView.startLayoutAnimation()

                }

            }


        })

        //Observer loop. This is where the recyclerview gets populated.
        viewModel.firestoreService.list.observe(this, Observer {
            super.onActivityCreated(savedInstanceState)
            viewModel = ViewModelProvider(this).get(PastListsViewModel::class.java)

            activity?.runOnUiThread(
                Runnable{
                    val recyclerViewState: Parcelable? = recyclerView.layoutManager!!.onSaveInstanceState()
                    adapter.setItemList(viewModel.firestoreService.list.value!!)
                    recyclerView.layoutManager!!.onRestoreInstanceState(recyclerViewState)
                    adapter.notifyDataSetChanged()
                })
        })
    }
}