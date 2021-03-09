package edu.uc.groupProject.topten

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import edu.uc.groupProject.topten.DTO.ListItem
import edu.uc.groupProject.topten.service.ListService
import edu.uc.groupProject.topten.ui.main.MainViewModel
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ListItemDataUnitTest {
    @get:Rule
    var rule: TestRule =  InstantTaskExecutorRule()
    lateinit var mvm: MainViewModel

//    var listService = mockk<ListService>()
//
//    @Before
//    fun givenAListOfMockItemsAreAvailable(){
//        mvm = MainViewModel()
//        createMockData()
//    }
//    private fun createMockData() {
//        var allListItemsLiveData = MutableLiveData<ArrayList<ListItem>>()
//        var allListItems = ArrayList<ListItem>()
//
//        var theDarkKnight = ListItem("The Dark Knight", "The best batman movie", 2)
//        var theAvengers = ListItem("The Avengers", "They all team up to fight bad guys", 4)
//        allListItems.add(theDarkKnight)
//        allListItems.add(theAvengers)
//
//        allListItemsLiveData.postValue(allListItems)
//        every { listService.fetchList(any<String>()) } returns allListItemsLiveData
//        mvm.listService = listService
//    }
//
//    @Test
//    fun SearchForMovies(){
//        mvm.fetchList("Top Ten Comic Book Movies")
//    }
//
//    @Test
//    fun ResultContainsAvengers(){
//        var avengersFound = false
//        mvm.list.observeForever{
//            assertNotNull(it)
//            assertTrue(it.size > 0)
//            it.forEach{
//                if(it.title == "The Avengers" &&  it.description == "They all team up to fight bad guys" && it.totalVotes == 4){
//                    avengersFound = true
//                }
//            }
//        }
//        assertTrue(avengersFound)
//    }
//
//    @Test
//    fun VerifyFunctionsInvoked() {
//        verify { listService.fetchList("Top Ten Comic Book Movies") }
//        verify (exactly = 0) { listService.fetchList("Top Ten Romance-Comedy Movies")}
//       confirmVerified(listService)
//    }

    @Test
    fun getFightClub(){
        mvm = MainViewModel()
        mvm.fetchFirebaseList()
        mvm.list.observeForever{
            it.forEach{
                println(it.title)
                println(it.description)
                println(it.totalVotes)
            }
        }
    }
}

