package edu.uc.groupProject.topten.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.uc.groupProject.topten.R

//Handles the past_lists_fragment
class PastListsFragment : Fragment() {
    private lateinit var viewModel: PastListsViewModel

    //Not used (yet?)
    //private lateinit var adapter : PastListAdapter

    lateinit var spinnerList : Spinner //spinner variable


    companion object {
        fun newInstance() = PastListsFragment()
    }

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
        viewModel.firestoreService.fetchListNames() //fetches all the list names

        var i = 0
        //Observer loop. This is where the drop-down box gets populated.
        viewModel.firestoreService.listOfLists.observe(this, Observer{
            spinnerList = view!!.findViewById<Spinner>(R.id.spn_listNames)
            var listOfListVariable = viewModel.firestoreService.arrayOfLists

            //Mini-adapter!
            var spinAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOfListVariable)
            spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            i++
            spinnerList.adapter = spinAdapter;
        })

        viewModel.firestoreService.list.observe(this, Observer {
            activity?.runOnUiThread(Runnable {})
        })
    }
}