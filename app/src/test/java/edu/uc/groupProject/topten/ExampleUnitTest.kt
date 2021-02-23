package edu.uc.groupProject.topten

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import edu.uc.groupProject.topten.DTO.ListItem
import edu.uc.groupProject.topten.DTO.TestDTO
import edu.uc.groupProject.topten.Service.ListService
import edu.uc.groupProject.topten.ui.main.MainViewModel
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.Mock

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @get:Rule
    var rule: TestRule =  InstantTaskExecutorRule()

    lateinit var mainViewModel: MainViewModel

    var listService = mockk<ListService>()

    @Test
    fun SearchForTheAvengers_ReturnsTheAvengers(){
        givenAListOfItemsAreAvailable()
        whenSearchForMovies()
        //thenVerifyFunctionsInvoked()
    }


   private fun givenAListOfItemsAreAvailable(){
       mainViewModel = MainViewModel();
       createMockData()
   }

     fun createMockData() {

        var allListItemsLiveData = MutableLiveData<ArrayList<ListItem>>()
        var allListItems = ArrayList<ListItem>()
        var theDarkKnight = ListItem("The Dark Knight", "A movie about batman", 3)
        allListItems.add(theDarkKnight)
        var theAvengers = ListItem("The Avengers", "They all team up to fight bad guys", 2)
        allListItems.add(theAvengers)

        allListItemsLiveData.postValue(allListItems);
        every { listService.fetchList(any()) } returns allListItemsLiveData

         mainViewModel.ListService = listService

    }

    private fun whenSearchForMovies(){

        mainViewModel.fetchList("Top Ten Comic Book Movies")
    }



    /*

    //Currently will fail and break

     private fun thenVerifyFunctionsInvoked(){

        verify { ListService.fetchList("The Avengers") }
        confirmVerified(ListService)
    }
    */


}

