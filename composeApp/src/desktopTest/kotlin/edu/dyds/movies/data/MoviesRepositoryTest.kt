package edu.dyds.movies.data

import edu.dyds.movies.data.external.MovieExternalSource
import edu.dyds.movies.data.external.MoviesExternalSource
import edu.dyds.movies.data.local.LocalMoviesSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

private fun getMovieExample1() = Movie(
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

private fun getMovieExample2() = Movie(
    2,
    "Example title 2",
    "Example overview 2",
    "Example releaseDate 2",
    "Example poster 2",
    "Example backdrop 2",
    "Example originalTitle 2",
    "Example originalLanguage 2",
    2.0,
    2.0
)

private fun getMovieExample3() = Movie(
    3,
    "Example title 3",
    "Example overview 3",
    "Example releaseDate 3",
    "Example poster 3",
    "Example backdrop 3",
    "Example originalTitle 3",
    "Example originalLanguage 3",
    3.0,
    3.0
)

class MoviesRepositoryTest {
    class LocalMoviesSourceFake : LocalMoviesSource {
        var cache: MutableList<Movie> = mutableListOf()

        override fun addMovies(popularMovies: List<Movie>) {
            cache.clear()
            cache.addAll(popularMovies)
        }

        override fun isEmpty(): Boolean {
            return cache.isEmpty()
        }

        override fun getMovieList(): List<Movie> {
            return cache
        }
    }

    class MoviesExternalSourceFake() : MoviesExternalSource {
        private var resourceAvailable = true

        fun disableResource() {
            resourceAvailable = false
        }

        override suspend fun getPopularMoviesRemote(): List<Movie> {
            if (!resourceAvailable) throw Exception("error message")
            return listOf(getMovieExample1(), getMovieExample2(), getMovieExample3())
        }
    }

    class MovieExternalSourceFake() : MovieExternalSource {
        override suspend fun getMovieByTitleRemote(title: String): Movie {
            return when (title) {
                "Example title 1" -> getMovieExample1()
                "Example title 2" -> getMovieExample2()
                "Example title 3" -> getMovieExample3()
                else -> throw Exception("error message")
            }
        }

    }

    private lateinit var moviesRepository: MoviesRepository
    private lateinit var localMoviesSourceFake: LocalMoviesSource
    private lateinit var moviesExternalSourceFake: MoviesExternalSourceFake
    private lateinit var movieExternalSourceFake: MovieExternalSource

    @BeforeTest
    fun setUp() {
        localMoviesSourceFake = LocalMoviesSourceFake()
        moviesExternalSourceFake = MoviesExternalSourceFake()
        movieExternalSourceFake = MovieExternalSourceFake()
        moviesRepository = MoviesRepositoryImpl(
            localMoviesSourceFake, movieExternalSourceFake, moviesExternalSourceFake
        )
    }

    @Test
    fun `test getPopularMovies returns from local source when local source isn't empty`() = runTest {
        //Arrange
        val listOfPopularMovies = listOf(getMovieExample1(), getMovieExample2(), getMovieExample3())
        localMoviesSourceFake.addMovies(listOfPopularMovies)
        val expected = listOf(getMovieExample1(), getMovieExample2(), getMovieExample3())

        //Act
        val result = moviesRepository.getPopularMovies()

        //Assert
        assertEquals(expected, result)
    }

    @Test
    fun `test getPopularMovies returns from remote source when local source is empty and side effect is executed`() =
        runTest {
            //Arrange
            val expected = listOf(getMovieExample1(), getMovieExample2(), getMovieExample3())

            //Act
            val result = moviesRepository.getPopularMovies()

            //Assert
            assertEquals(expected, result)
            assertEquals(expected, localMoviesSourceFake.getMovieList())

        }

    @Test
    fun `test getPopularMovies remote returns empty list when resource is unavailable`() = runTest {
        //Arrange
        moviesExternalSourceFake.disableResource()
        val expected = emptyList<Movie>()

        //Act
        val result = localMoviesSourceFake.getMovieList()

        //Assert
        assertEquals(expected, result)
    }


    @Test
    fun `test getMovieById with valid id returns the correct movie`() = runTest {
        //Arrange
        val expected = getMovieExample1()

        //Act
        val result = moviesRepository.getMovieDetailByTitle("Example title 1")

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `test getMovieById returns null when RemoteMoviesSource throws exception because of invalid id`() = runTest {
        //Arrange
        val expected = null

        //Act
        val result = moviesRepository.getMovieDetailByTitle("Example title 4")

        //Assert
        assertEquals(expected, result)
    }
}