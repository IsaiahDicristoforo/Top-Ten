package edu.uc.groupProject.topten.Service

import androidx.lifecycle.MutableLiveData
import edu.uc.groupProject.topten.DTO.ListItem

class ListService {


   fun  fetchList(listName: MutableLiveData<ArrayList<ListItem>>): MutableLiveData<ArrayList<ListItem>> {
       return MutableLiveData<ArrayList<ListItem>>()
    }




}