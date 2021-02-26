package edu.uc.groupProject.topten.ui.main

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
import edu.uc.groupProject.topten.DTO.ListItem
import edu.uc.groupProject.topten.DTO.ListUtils

class MainViewModel : ViewModel() {

    public var listItems:MutableLiveData<ArrayList<ListItem>> = MutableLiveData<ArrayList<ListItem>>()

    private lateinit var firestore : FirebaseFirestore

    init{
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        waitForListUpdate()
    }

    private fun waitForListUpdate() {
        firestore.collection("lists/Top Fifteen Movies/MyListItems").addSnapshotListener{
            snapshot, e ->
            if(e != null){
                Log.w(TAG, "Listen Failed", e)
                return@addSnapshotListener
            }
            if(snapshot != null){
                var allListItems = ArrayList<ListItem>()
                val documents = snapshot.documents
                documents.forEach{

                    //val listItem = it.toObject(ListItem::class.java)

                    val listItem :ListItem = ListItem(it.getString("title")!!, "Test", it.getLong("totalVotes")!!.toInt())
                    allListItems.add(listItem!!)
                }

                allListItems = ListUtils().sortListItems(allListItems)
                listItems.value = allListItems
            }
        }
    }






}