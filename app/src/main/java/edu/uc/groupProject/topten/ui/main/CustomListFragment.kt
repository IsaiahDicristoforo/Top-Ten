package edu.uc.groupProject.topten.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.uc.groupProject.topten.R
import edu.uc.groupProject.topten.dto.ListItem
import edu.uc.groupProject.topten.dto.TopTenList
import edu.uc.groupProject.topten.service.FirestoreService
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CustomListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomListFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_custom_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var listToGenerate = ArrayList<ListItem>()
        listToGenerate.add(ListItem(0," ","",0))
        listToGenerate.add(ListItem(1," ","",0))
        listToGenerate.add(ListItem(2," ","",0))
        listToGenerate.add(ListItem(3," ","",0))

        var customListAdapter:CustomListGenerationAdapter = CustomListGenerationAdapter(listToGenerate)

        var customListGenerationRecyclerView:RecyclerView = view!!.findViewById<RecyclerView>(R.id.rec_customList)
        customListGenerationRecyclerView.layoutManager = LinearLayoutManager(this.context)
        customListGenerationRecyclerView.adapter = customListAdapter

        var addNewItemButton = view!!.findViewById<FloatingActionButton>(R.id.btn_addNewCustomListItem)
        addNewItemButton.setOnClickListener(){
            listToGenerate.add(ListItem(listToGenerate.size," ", " ", 0))
            customListAdapter.notifyItemInserted(customListAdapter.itemCount - 1)
        }

        var submitListButton = view!!.findViewById<ImageButton>(R.id.btn_submitCustomList).setOnClickListener(){

            var listTitle = view!!.findViewById<EditText>(R.id.txt_CustomListTitle).text.toString()

            var listToAddToDatabase: TopTenList = TopTenList(0, listTitle,"",false,"", Date(), Date())
            listToAddToDatabase.listItems = customListAdapter.listItems
            FirestoreService().writeListToDatabase(listToAddToDatabase)

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CustomListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CustomListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}