package edu.dyds.movies.presentation.detail

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.usecase.GetMovieDetailsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class TestDetailViewModel {

    val testDispatcher = UnconfinedTestDispatcher()
    val testScope = CoroutineScope(testDispatcher)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private val getMovieDetailsUseCase = object : GetMovieDetailsUseCase {
        override suspend fun getMovieDetails(id: Int): Movie {
            return Movie(
                id,
                "Movie $id",
                "the movie $id overview",
                "21/10/2023",
                "poster url",
                "backdrop url",
                "Original Movie $id",
                "en",
                10.0,
                8.0
            )
        }
    }

    @Test
    fun `get movie should emit loading and data states`() = runTest {
        // Arrange
        val detailViewModel = DetailScreenViewModel(getMovieDetailsUseCase)

        val events: ArrayList<DetailScreenViewModel.MovieDetailUiState> = arrayListOf()
        testScope.launch {
            detailViewModel.movieDetailStateFlow.collect { state ->
                events.add(state)
            }
        }

        // Act
        detailViewModel.getMovieDetail(1)

        // Assert
        assertEquals(
            expected = DetailScreenViewModel.MovieDetailUiState(isLoading = true, movie = null),
            actual = events[0]
        )
        assertEquals(
            expected = DetailScreenViewModel.MovieDetailUiState(
                isLoading = false,
                movie = Movie(
                    1,
                    "Movie 1",
                    "the movie 1 overview",
                    "21/10/2023",
                    "poster url",
                    "backdrop url",
                    "Original Movie 1",
                    "en",
                    10.0,
                    8.0
                )
            ),
            actual = events[1]
        )
    }
}