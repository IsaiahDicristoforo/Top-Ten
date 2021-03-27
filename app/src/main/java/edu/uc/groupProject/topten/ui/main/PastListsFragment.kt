package edu.uc.groupProject.topten.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.uc.groupProject.topten.R
import edu.uc.groupProject.topten.dto.ListItem

//Handles the past_lists_fragment
class PastListsFragment : Fragment() {

    private lateinit var viewModel: PastListsViewModel
    private lateinit var adapter : PastListAdapter
    lateinit var recyclerView:RecyclerView
    var testList = ArrayList<ListItem>()
    private var isCanceled = false
    lateinit var spinnerList : Spinner //spinner variable
    var listOfListNames: MutableList<String> = ArrayList()
    //var listOfListNames = viewModel.loadLists()

    companion object {
        fun newInstance() = PastListsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item, listOfListNames
        )

        return inflater.inflate(R.layout.past_lists_fragment, container, false)\
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PastListsViewModel::class.java)

        recyclerView = view!!.findViewById<RecyclerView>(R.id.rec_pastList)
        //var listTitleLabel = view!!.findViewById<Spinner>(R.id.spn_listNames)
        spinnerList = view!!.findViewById<Spinner>(R.id.spn_listNames)

        recyclerView.layoutManager =  LinearLayoutManager(this.context)

        adapter = PastListAdapter(viewModel, testList)
        recyclerView.adapter = adapter


        viewModel.firestoreService.list.observe(this, Observer {

            activity?.runOnUiThread(

                Runnable {
                    val recyclerViewState: Parcelable? =
                        recyclerView.layoutManager!!.onSaveInstanceState()


                    adapter.setItemList(viewModel.firestoreService.list.value!!)
                    //listTitleLabel.text = viewModel.firestoreService.currentList
                    listOfListNames = viewModel.loadLists()
                    adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, listOfListNames)


                    recyclerView.layoutManager!!.onRestoreInstanceState(recyclerViewState)
                }
            )
        })


        isCanceled = true
    }

}