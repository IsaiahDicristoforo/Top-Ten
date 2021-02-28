package edu.uc.groupProject.topten.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import edu.uc.groupProject.topten.DAO.CurrentListAdapter
import edu.uc.groupProject.topten.R



class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter : CurrentListAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.list.observe(this, Observer {

            //it ->  view!!.findViewById<Spinner>(R.id.spn_spinner).setAdapter(ArrayAdapter(context!!, R.layout.support_simple_spinner_dropdown_item, it))

            adapter = CurrentListAdapter(viewModel.list.value!!)

            view!!.findViewById<RecyclerView>(R.id.rec_currentList).layoutManager =  LinearLayoutManager(this.context)
            view!!.findViewById<RecyclerView>(R.id.rec_currentList).adapter = adapter

        })








    }

}