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
    lateinit var spinnerList : Spinner //spinner variable

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
        viewModel.firestoreService.fetchListNames()
        viewModel.firestoreService.listOfLists.observe(this, Observer{

            spinnerList = view!!.findViewById<Spinner>(R.id.spn_listNames)
            adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, viewModel.firestoreService.listOfLists.value!!)


        })
        //adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, viewModel.firestoreService.listOfLists.value!!)

        viewModel.firestoreService.list.observe(this, Observer {

            activity?.runOnUiThread(

                Runnable {
                }
            )
        })
    }

}