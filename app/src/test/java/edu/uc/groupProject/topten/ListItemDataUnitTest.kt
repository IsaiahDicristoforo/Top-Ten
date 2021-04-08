package edu.uc.groupProject.topten

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import edu.uc.groupProject.topten.dto.ListItem
import edu.uc.groupProject.topten.service.ListService
import edu.uc.groupProject.topten.ui.main.MainViewModel
import io.mockk.every
import io.mockk.mockk
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
    lateinit var badListItems: ListItem

    @Test
    fun SearchForTheAvengers_ReturnsTheAvengers() {
        givenAListOfMockItemsAreAvailable()
        whenSearchForMovies()
        thenResultContainsAvengers()
    }

    @Test
    fun ToStringReturnsTitle() {
        var toStringTest = mockk<ListItem>()
        toStringTest = ListItem(0, "Avengers", "A movie about Batman", 100)
        assertTrue(toStringTest.toString() == "Avengers")
    }

    @Test
    fun ensureCorrectVoteCounts() {
        // Code should ensure the counts are correct before they are added
        try {
            badListItems = ListItem(0, "Avengers", "A movie about Batman", -100)
        } catch (ex: Exception) {
            assertTrue(ex.message == "Votes must be non-negative")
        }
    }

    @Test
    fun ensureMovieHasName() {
        // Code should ensure that all movies have a name
        try {
            badListItems = ListItem(0, "", "A movie about Batman", 100)
        } catch (ex: Exception) {
            assertTrue(ex.message == "Name required")
        }
    }

    private fun givenAListOfMockItemsAreAvailable() {
        createMockData()
    }

    private fun whenSearchForMovies() {
        mvm.fetchListServiceList("Top Ten Superheros")
    }

    private fun thenResultContainsAvengers() {
        var avengersFound = false
        mvm.list.observeForever {
            assertNotNull(it)
            assertTrue(it.size > 0)
            it.forEach {
                if (it.title == "Avengers") {
                    avengersFound = true
                }
            }
        }
        assertTrue(avengersFound)
    }

    private fun createMockData() {
        var allListItemsLiveData = MutableLiveData<ArrayList<ListItem>>()
        var allListItems = ArrayList<ListItem>()

        var m1 = ListItem(0, "Avengers", "A movie about Batman", 100)
        var m2 = ListItem(1, "The Return of the King", "A movie about a ring and some eagles", 150)
        var m3 = ListItem(
            2,
            "The Empire Strikes Back",
            "A movie about some light wands and parent issues",
            200
        )
        var m4 = ListItem(3, "The Godfather", "n/a", 24)
        var m5 = ListItem(4, "The Avengers", "They all team up to fight bad guys", 231)
        var m6 = ListItem(5, "Inception", "", 12)
        var m7 = ListItem(6, "E.T", "", 124)
        var m8 = ListItem(7, "The Matrix", "", 42)

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