package edu.dyds.movies.domain.usecase

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.fakes.FakeMovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GetPopularMoviesUseCaseTest {

    val movieExample1 = Movie(
        1,
        "Example title 1",
        "Example overview 1",
        "Example releaseDate 1",
        "Example poster 1",
        "Example backdrop 1",
        "Example originalTitle 1",
        "Example originalLanguage 1",
        1.0,
        1.0
    )
    val movieExample2 = Movie(
        2,
        "Example title 2",
        "Example overview 2",
        "Example releaseDate 2",
        "Example poster 2",
        "Example backdrop 2",
        "Example originalTitle 2",
        "Example originalLanguage 2",
        2.0,
        6.0
    )
    val movieExample3 = Movie(
        3,
        "Example title 3",
        "Example overview 3",
        "Example releaseDate 3",
        "Example poster 3",
        "Example backdrop 3",
        "Example originalTitle 3",
        "Example originalLanguage 3",
        3.0,
        8.0
    )

    private val fakeMovieList = listOf(movieExample1, movieExample2, movieExample3)
    private val getFakeMovieRepository = FakeMovieRepository(fakeMovieList)

    @Test
    fun `test getPopularMovies returns movie list ordered by vote average`() = runTest {
        //Arrange
        val getPopularMovieUseCase = GetPopularMoviesUseCaseImpl(getFakeMovieRepository)

        //Act
        val result = getPopularMovieUseCase.getPopularMovies()

        //Asset
        assertEquals(3, result.size)
        assertEquals(3, result[0].movie.id)
        assertEquals(2, result[1].movie.id)
        assertEquals(1, result[2].movie.id)
    }

    @Test
    fun `test getPopularMovies qualifies movies correctly`() = runTest {
        //Arrange
        val getPopularMovieUseCase = GetPopularMoviesUseCaseImpl(getFakeMovieRepository)

        //Act
        val result = getPopularMovieUseCase.getPopularMovies()

        //Asset
        assertTrue(result[0].isGoodMovie)
        assertTrue(result[1].isGoodMovie)
        assertFalse(result[2].isGoodMovie)
    }
}
