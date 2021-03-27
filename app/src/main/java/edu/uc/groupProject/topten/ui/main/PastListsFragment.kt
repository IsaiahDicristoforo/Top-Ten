package edu.uc.groupProject.topten.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.Observer
import edu.uc.groupProject.topten.R

//Handles the past_lists_fragment
class PastListsFragment : Fragment() {

    private lateinit var viewModel: PastListsViewModel
    private lateinit var adapter : ArrayAdapter<String>
    //lateinit var recyclerView:RecyclerView
    //var testList = ArrayList<ListItem>()
    lateinit var spinnerList : Spinner //spinner variable
    //var listOfListNames: MutableList<String> = ArrayList()
    //var viewModel.fireStoreService.listOfListNames = viewModel.loadLists()

    companion object {
        fun newInstance() = PastListsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.past_lists_fragment, container, false)
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PastListsViewModel::class.java)

        //recyclerView = view!!.findViewById<RecyclerView>(R.id.rec_pastList)

        //spinnerList.adapter = adapter
        //recyclerView.layoutManager =  LinearLayoutManager(this.context)
        //adapter = PastListAdapter(viewModel, testList)
        //recyclerView.adapter = adapter
        viewModel.firestoreService.fetchListNames()
        viewModel.firestoreService.listOfLists.observe(this, Observer{

            spinnerList = view!!.findViewById<Spinner>(R.id.spn_listNames)
            adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, viewModel.firestoreService.listOfLists.value!!)


        })
        //adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, viewModel.firestoreService.listOfLists.value!!)

        viewModel.firestoreService.list.observe(this, Observer {

            activity?.runOnUiThread(

                Runnable {
//                    val recyclerViewState: Parcelable? =
//                        recyclerView.layoutManager!!.onSaveInstanceState()


                    //adapter.setItemList(viewModel.firestoreService.list.value!!)
                    //listTitleLabel.text = viewModel.firestoreService.currentList



                    //recyclerView.layoutManager!!.onRestoreInstanceState(recyclerViewState)
                }
            )
        })
    }

}