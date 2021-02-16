package edu.uc.groupProject.topten.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.uc.groupProject.topten.DTO.ListItem
import edu.uc.groupProject.topten.Service.ListService

class MainViewModel : ViewModel() {

    var listItems:MutableLiveData<ArrayList<ListItem>> = MutableLiveData<ArrayList<ListItem>>()

    var ListService: ListService = ListService()


    fun fetchList(ListTitle: String) {

       listItems = ListService.fetchList(ListTitle)

    }



}