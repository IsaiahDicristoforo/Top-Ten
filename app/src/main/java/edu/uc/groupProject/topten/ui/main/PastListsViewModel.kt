package edu.uc.groupProject.topten.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.uc.groupProject.topten.dto.ListItem
import edu.uc.groupProject.topten.dto.Strawpoll
import edu.uc.groupProject.topten.service.FirestoreService
import edu.uc.groupProject.topten.service.ListService
import edu.uc.groupProject.topten.service.StrawpollService

class PastListsViewModel : ViewModel() {
    var list : MutableLiveData<ArrayList<ListItem>> = MutableLiveData<ArrayList<ListItem>>()
    var listService : ListService = ListService()
    var firestoreService : FirestoreService =  FirestoreService()
    var lists : MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()
    //var listsString = firestoreService.listOfLists

    fun loadNextList(generateNewList:Boolean){
        list = firestoreService.fetchList(generateNewList)
    }

    fun loadLists(): MutableLiveData<ArrayList<String>> {
        lists = firestoreService.listOfLists //potentially a problem, could be lists.value?
        return lists
    }

    fun fetchFirestoreListItem(){
        list = firestoreService.fetchDocument()
    }

    fun fetchListServiceList(listName: String) {
        list = listService.fetchList(listName)
    }

    fun fetchStrawpoll(id: Int): MutableLiveData<Strawpoll>? {
        val service = StrawpollService()
        return service.getStrawpoll(id)
    }
}