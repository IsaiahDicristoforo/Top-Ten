package edu.uc.groupProject.topten

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import edu.uc.groupProject.topten.dto.ListItem
import edu.uc.groupProject.topten.service.ListService
import edu.uc.groupProject.topten.ui.main.MainViewModel
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

/**
 * Example local unit test, which will execute on the development machine.
 */
class ListItemDataUnitTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    var listService = mockk<ListService>()
    private val mvm: MainViewModel = MainViewModel()

    @Test
    fun SearchForTheAvengers_ReturnsTheAvengers() {
        givenAListOfMockItemsAreAvailable()
        whenSearchForMovies()
        thenResultContainsAvengers()
        thenVerifyFunctionsInvoked()
    }

    private fun givenAListOfMockItemsAreAvailable() {
        createMockData()
    }

    private fun whenSearchForMovies() {
        mvm.fetchListServiceList("Top Ten Comic Book Movies")
    }

    private fun thenResultContainsAvengers() {
        var avengersFound = false
        mvm.list.observeForever {
            assertNotNull(it)
            assertTrue(it.size > 0)
            it.forEach {
                if (it.title == "The Avengers") {
                    avengersFound = true
                }
            }
        }
        assertTrue(avengersFound)
    }

    private fun thenVerifyFunctionsInvoked() {
        verify { listService.fetchList("Top Ten Comic Book Movies") }
        verify(exactly = 0) { listService.fetchList("Top Ten Romance-Comedy Movies") }
        confirmVerified(listService)
    }

    private fun createMockData() {
        var allListItemsLiveData = MutableLiveData<ArrayList<ListItem>>()
        var allListItems = ArrayList<ListItem>()

        var m1 = ListItem("The Dark Knight", "A movie about Batman", "100")
        var m2 = ListItem("The Return of the King", "A movie about a ring and some eagles", "150")
        var m3 = ListItem("The Empire Strikes Back", "A movie about some light wands and parent issues", "200")
        var m4 = ListItem("The Godfather", "n/a", "24")
        var m5 = ListItem("The Avengers", "They all team up to fight bad guys", "231")
        var m6 = ListItem("Inception", "", "12")
        var m7 = ListItem("E.T", "", "124")
        var m8 = ListItem("The Matrix", "", "42")

        allListItems.add(m1)
        allListItems.add(m2)
        allListItems.add(m3)
        allListItems.add(m4)
        allListItems.add(m5)
        allListItems.add(m6)
        allListItems.add(m7)
        allListItems.add(m8)

        allListItemsLiveData.postValue(allListItems)
        every { listService.fetchList(any<String>()) } returns allListItemsLiveData
        mvm.listService = listService
    }
}

