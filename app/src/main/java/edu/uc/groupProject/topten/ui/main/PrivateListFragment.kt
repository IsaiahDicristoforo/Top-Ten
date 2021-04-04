package edu.uc.groupProject.topten.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
        // TODO: Use the ViewModel
    }

}