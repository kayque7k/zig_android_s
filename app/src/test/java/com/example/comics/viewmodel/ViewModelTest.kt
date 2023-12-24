package com.example.comics.viewmodel

import com.example.comics.CoroutinesTestRule
import com.example.comics.repository.DataModel
import com.example.comics.repository.IRepository
import com.example.comics.repository.ItemModel
import com.example.comics.repository.toListItemVO
import com.example.comics.view.ViewModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock

@ExperimentalCoroutinesApi
class ViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    @Mock
    private val iRepository: IRepository = mockk(relaxed = true)

    @Mock
    private val event: (Any) -> Unit = mockk(relaxed = true)

    private lateinit var viewModel: ViewModel

    @Before
    fun setup() {
        viewModel = ViewModel(iRepository)
        viewModel.event = event
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when execute getComics return mock success SetupList`() = runTest {
        val mockResult = ItemModel(data = DataModel(results = listOf()))
        coEvery { iRepository.getComics() } returns mockResult

        viewModel.getComics()

        verify {
            event.invoke(ViewModel.Event.Empty)
            event.invoke(ViewModel.Event.LoadingIndicator)
            event.invoke(any<ViewModel.Event.SetupList>())
            event.invoke(ViewModel.Event.CloseIndicator)
        }
    }

    @Test
    fun `when execute getComics return mock success UpdateList`() = runTest {
        val mockResult = ItemModel(data = DataModel(results = listOf()))
        coEvery { iRepository.getComics() } returns mockResult

        viewModel.getComics()

        verify {
            event.invoke(ViewModel.Event.Empty)
            event.invoke(ViewModel.Event.LoadingIndicator)
            event.invoke(any<ViewModel.Event.UpdateList>())
            event.invoke(ViewModel.Event.CloseIndicator)
        }
    }

    @Test
    fun `when execute api getComics return mock error`() = runBlocking {
        coEvery { iRepository.getComics() } throws Exception(MOCK_EXCEPTION)

        viewModel.getComics()

        verify {
            event.invoke(ViewModel.Event.Empty)
            event.invoke(ViewModel.Event.LoadingIndicator)
            event.invoke(any<ViewModel.Event.Error>())
            event.invoke(ViewModel.Event.CloseIndicator)
        }
    }

    private companion object  {
        const val MOCK_EXCEPTION = "Error mockk"
    }
}