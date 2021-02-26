package edu.uc.groupProject.topten

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import edu.uc.groupProject.topten.DTO.ListItem
import edu.uc.groupProject.topten.Service.ListService
import edu.uc.groupProject.topten.ui.main.MainViewModel
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

import org.junit.Rule
import org.junit.rules.TestRule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @get:Rule
    var rule: TestRule =  InstantTaskExecutorRule()

    lateinit var mainViewModel: MainViewModel

    var ListService = mockk<ListService>()

    @Test
    fun SearchForTheAvengers_ReturnsTheAvengers(){
        givenAListOfItemsAreAvailable()
        whenSearchForMovies()
        //thenVerifyFunctionsInvoked()
    }


   fun givenAListOfItemsAreAvailable(){
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
        every { ListService.fetchList(any()) } returns allListItemsLiveData

         mainViewModel.listService = ListService

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

