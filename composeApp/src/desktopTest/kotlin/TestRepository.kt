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
    class LocalMoviesSourceImpl : LocalMoviesSource {
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

    class RemoteMoviesSourceImpl() : RemoteMoviesSource {
        val MovieExample1 = Movie(
            1, "Example title 1",
            "Example overview 1", "Example releaseDate 1",
            "Example poster 1", "Example backdrop 1",
            "Example originalTitle 1", "Example originalLanguage 1",
            1.0, 1.0
        )
        val MovieExample2 = Movie(
            2, "Example title 2",
            "Example overview 2", "Example releaseDate 2",
            "Example poster 2", "Example backdrop 2",
            "Example originalTitle 2", "Example originalLanguage 2",
            2.0, 2.0
        )
        val MovieExample3 = Movie(
            3, "Example title 3",
            "Example overview 3", "Example releaseDate 3",
            "Example poster 3", "Example backdrop 3",
            "Example originalTitle 3", "Example originalLanguage 3",
            3.0, 3.0
        )

        override suspend fun getMovieByIdRemote(id: Int): Movie? {
            return when (id) {
                1 -> MovieExample1
                2 -> MovieExample2
                3 -> MovieExample3
                else -> null
            }
        }

        override suspend fun getPopularMoviesRemote(): List<Movie> {
            return listOf(MovieExample1, MovieExample2, MovieExample3)
        }
    }

    private lateinit var moviesRepositoryFake: MoviesRepository
    private lateinit var localMoviesSourceFake: LocalMoviesSource
    private lateinit var remoteMoviesSourceFake: RemoteMoviesSource

    val MovieExample1 = Movie(
        1, "Example title 1",
        "Example overview 1", "Example releaseDate 1",
        "Example poster 1", "Example backdrop 1",
        "Example originalTitle 1", "Example originalLanguage 1",
        1.0, 1.0
    )
    val MovieExample2 = Movie(
        2, "Example title 2",
        "Example overview 2", "Example releaseDate 2",
        "Example poster 2", "Example backdrop 2",
        "Example originalTitle 2", "Example originalLanguage 2",
        2.0, 2.0
    )
    val MovieExample3 = Movie(
        3, "Example title 3",
        "Example overview 3", "Example releaseDate 3",
        "Example poster 3", "Example backdrop 3",
        "Example originalTitle 3", "Example originalLanguage 3",
        3.0, 3.0
    )

    @BeforeTest
    fun setUp() {
        val listOfPopularMovies = listOf(MovieExample1, MovieExample2, MovieExample3)
        localMoviesSourceFake = LocalMoviesSourceImpl()
        localMoviesSourceFake.initializeMovieCache(listOfPopularMovies)

        remoteMoviesSourceFake = RemoteMoviesSourceImpl()

        moviesRepositoryFake = MoviesRepositoryImpl(
            localMoviesSourceFake,
            remoteMoviesSourceFake
        )
    }

    @Test
    fun `Test de getPopularMovies() Local`() = runTest {
        //arrange
        val expected = listOf(MovieExample1, MovieExample2, MovieExample3)
        //act
        val result = moviesRepositoryFake.getPopularMovies()
        // assert
        assertEquals(expected, result)
    }

    @Test
    fun `Test de getPopularMovies() Remoto`() = runTest {
        //arrange
        localMoviesSourceFake.initializeMovieCache(emptyList())
        val expected = listOf(MovieExample1, MovieExample2, MovieExample3)
        //act
        val result = moviesRepositoryFake.getPopularMovies()
        // assert
        assertEquals(expected, result)
    }

    @Test
    fun `Test de getMovieById() Remoto`() = runTest {
        //arrange
        val expected = MovieExample1
        //act
        val result = moviesRepositoryFake.getMovieDetailById(1)
        // assert
        assertEquals(expected, result)
    }

    @Test
    fun `Test de getMovieById() Remoto id erroneo`() = runTest {
        //arrange
        val expected = null
        //act
        val result = moviesRepositoryFake.getMovieDetailById(4)
        // assert
        assertEquals(expected, result)
    }


}