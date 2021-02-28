package edu.uc.groupProject.topten

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import edu.uc.groupProject.topten.DTO.ListItem
import edu.uc.groupProject.topten.Service.ListService
import edu.uc.groupProject.topten.ui.main.MainViewModel
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @get:Rule
    var rule: TestRule =  InstantTaskExecutorRule()
    lateinit var mvm: MainViewModel

    var listService = mockk<ListService>()

    @Test
    fun SearchForTheAvengers_ReturnsTheAvengers(){
        givenAListOfMockItemsAreAvailable()
        whenSearchForMovies()
        thenResultContainsAvengers()
    }

    private fun givenAListOfMockItemsAreAvailable(){
        mvm = MainViewModel();
        createMockData()
    }

    private fun whenSearchForMovies(){

        mvm.fetchList("Top Ten Comic Book Movies")
    }

    private fun thenResultContainsAvengers(){
        var avengersFound = false
        mvm.list.observeForever{
            assertNotNull(it)
            assertTrue(it.size > 0)
            it.forEach{
                if(it.title.equals("The Avengers")){
                    avengersFound = true
                }
            }
        }
        assertTrue(avengersFound)
    }

    private fun createMockData() {
        var allListItemsLiveData = MutableLiveData<ArrayList<ListItem>>()
        var allListItems = ArrayList<ListItem>()

        var theDarkKnight = ListItem("The Dark Knight", "The best batman movie", 2)
        var theAvengers = ListItem("The Avengers", "They all team up to fight bad guys", 4)
        var inception = ListItem("Inception", "Yes.", 5)
        allListItems.add(theDarkKnight)
        allListItems.add(theAvengers)
        allListItems.add(inception)

        allListItemsLiveData.postValue(allListItems)
        every { listService.fetchList(any<String>()) } returns allListItemsLiveData
        mvm.listService = listService
    }
}

