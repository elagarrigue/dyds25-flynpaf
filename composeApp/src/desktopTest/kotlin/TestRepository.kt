package edu.dyds.movies.data

import edu.dyds.movies.data.external.RemoteMoviesSource
import edu.dyds.movies.data.local.LocalMoviesSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals


class TestRepository {
    class LocalMoviesSourceFake : LocalMoviesSource {
        var cache: MutableList<Movie> = mutableListOf()

        override fun searchMovie(id: Int): Movie? = cache.find { it.id == id }

        override fun initializeMovieCache(popularMovies: List<Movie>): List<Movie> {
            cache.clear()
            cache.addAll(popularMovies)
            return popularMovies
        }

        override fun isEmpty(): Boolean {
            return cache.isEmpty()
        }

        override fun getMovieList(): List<Movie> {
            return cache
        }
    }

    class RemoteMoviesSourceFake() : RemoteMoviesSource {
        val movieExample1 = Movie(
            1, "Example title 1",
            "Example overview 1", "Example releaseDate 1",
            "Example poster 1", "Example backdrop 1",
            "Example originalTitle 1", "Example originalLanguage 1",
            1.0, 1.0
        )
        val movieExample2 = Movie(
            2, "Example title 2",
            "Example overview 2", "Example releaseDate 2",
            "Example poster 2", "Example backdrop 2",
            "Example originalTitle 2", "Example originalLanguage 2",
            2.0, 2.0
        )
        val movieExample3 = Movie(
            3, "Example title 3",
            "Example overview 3", "Example releaseDate 3",
            "Example poster 3", "Example backdrop 3",
            "Example originalTitle 3", "Example originalLanguage 3",
            3.0, 3.0
        )

        override suspend fun getMovieByIdRemote(id: Int): Movie {
            return when (id) {
                1 -> movieExample1
                2 -> movieExample2
                3 -> movieExample3
                else -> throw Exception("error message")
            }
        }

        override suspend fun getPopularMoviesRemote(): List<Movie> {
            return listOf(movieExample1, movieExample2, movieExample3)
        }
    }

    private lateinit var moviesRepository: MoviesRepository
    private lateinit var localMoviesSourceFake: LocalMoviesSource
    private lateinit var remoteMoviesSourceFake: RemoteMoviesSource

    val movieExample1 = Movie(
        1, "Example title 1",
        "Example overview 1", "Example releaseDate 1",
        "Example poster 1", "Example backdrop 1",
        "Example originalTitle 1", "Example originalLanguage 1",
        1.0, 1.0
    )
    val movieExample2 = Movie(
        2, "Example title 2",
        "Example overview 2", "Example releaseDate 2",
        "Example poster 2", "Example backdrop 2",
        "Example originalTitle 2", "Example originalLanguage 2",
        2.0, 2.0
    )
    val movieExample3 = Movie(
        3, "Example title 3",
        "Example overview 3", "Example releaseDate 3",
        "Example poster 3", "Example backdrop 3",
        "Example originalTitle 3", "Example originalLanguage 3",
        3.0, 3.0
    )

    @BeforeTest
    fun setUp() {
        val listOfPopularMovies = listOf(movieExample1, movieExample2, movieExample3)
        localMoviesSourceFake = LocalMoviesSourceFake()
        localMoviesSourceFake.initializeMovieCache(listOfPopularMovies)

        remoteMoviesSourceFake = RemoteMoviesSourceFake()

        moviesRepository = MoviesRepositoryImpl(
            localMoviesSourceFake,
            remoteMoviesSourceFake
        )
    }

    @Test
    fun `test getPopularMovies returns from local source when local source isn't empty`() = runTest {
        //arrange
        val expected = listOf(movieExample1, movieExample2, movieExample3)
        //act
        val result = moviesRepository.getPopularMovies()
        // assert
        assertEquals(expected, result)
    }

    @Test
    fun `test getPopularMovies returns from remote source when local source is empty`() = runTest {
        //arrange
        localMoviesSourceFake.initializeMovieCache(emptyList())
        val expected = listOf(movieExample1, movieExample2, movieExample3)
        //act
        val result = moviesRepository.getPopularMovies()
        // assert
        assertEquals(expected, result)
    }

    @Test
    fun `test getMovieById returns the correct movie using existing id`() = runTest {
        //arrange
        val expected = movieExample1
        //act
        val result = moviesRepository.getMovieDetailById(1)
        // assert
        assertEquals(expected, result)
    }

    @Test
    fun `test getMovieById returns null using missing id`() = runTest {
        //arrange
        val expected = null
        //act
        val result = moviesRepository.getMovieDetailById(4)
        // assert
        assertEquals(expected, result)
    }
}