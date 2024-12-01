import androidx.lifecycle.Observer
import com.devs.imgur.data.repository.ImageRepository
import com.devs.imgur.data.repository.modal.Gallery
import com.devs.imgur.data.repository.resource.NetworkError
import com.devs.imgur.data.repository.resource.Resource
import com.devs.imgur.ui.search.SearchViewModel
import com.devs.imgur.util.TestExecutorAndDispatchersRule
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    // To handle LiveData updates synchronously during tests
    @get:Rule
    val rule = TestExecutorAndDispatchersRule() // To test LiveData

    private val imageRepository: ImageRepository = mockk<ImageRepository>()
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        viewModel = SearchViewModel(imageRepository)
    }

    @After
    fun tearDown() { }

    @Test
    fun `searchImage should emit loading and success states when network is available`() = runTest {
        // Given
        val query = "test"
        val gallery = mockk<Gallery>()
        val resource = Resource.success(gallery)

        coEvery { imageRepository.searchImage(query) } returns resource

        val observer: Observer<Resource<Gallery>> = mockk(relaxed = true)
        viewModel.imageState.observeForever(observer)

        // When
        viewModel.searchImage(query)
        advanceUntilIdle() // Wait for coroutine to finish

        // Then
        verifySequence {
            observer.onChanged(Resource.loading(null))
            observer.onChanged(resource)
        }

        viewModel.imageState.removeObserver(observer)

    }

    @Test
    fun `searchImage should emit error state when offline`() {
        // Given
        viewModel.isOffline = true
        val observer: Observer<Resource<Gallery>> = mockk(relaxed = true)
        viewModel.imageState.observeForever(observer)

        // When
        viewModel.searchImage("test")

        // Then
        verify {
            observer.onChanged(
                Resource.error(null, networkError = NetworkError.NO_CONNECTIVITY)
            )
        }

        viewModel.imageState.removeObserver(observer)
    }

    @Test
    fun `searchImage should emit loading and error states when repository returns an error`() = runTest {
        // Given
        val query = "test"
        val resource = Resource.error<Gallery>(null, msg = "Something went wrong")
        coEvery { imageRepository.searchImage(query) } returns resource

        val observer: Observer<Resource<Gallery>> = mockk(relaxed = true)
        viewModel.imageState.observeForever(observer)

        // When
        viewModel.searchImage(query)
        advanceUntilIdle() // Wait for coroutine to finish

        // Then
        verifySequence {
            observer.onChanged(Resource.loading(null))
            observer.onChanged(resource)
        }

        viewModel.imageState.removeObserver(observer)
    }
}
