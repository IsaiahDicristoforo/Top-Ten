package edu.uc.groupProject.topten.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.uc.groupProject.topten.R

class PrivateListFragment : Fragment() {


    companion object {
        fun newInstance() = PrivateListFragment()
    }

    private lateinit var viewModel: PrivateListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.private_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PrivateListViewModel::class.java)


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //If the condition is true, the user is coming to this fragment because they clicked the share button on the main activity, and want to save a private list.
        if(arguments?.getString("ListTitle") != null){
            var listTitle:String = arguments!!.getString("ListTitle").toString()
            //Now you can retrieve the list items from the database and firestore service class using the list title passed from the other fragment
        }
    }
}