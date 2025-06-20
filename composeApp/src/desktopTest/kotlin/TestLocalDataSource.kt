package edu.dyds.movies.data.local

import edu.dyds.movies.domain.entity.Movie
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.BeforeTest

class TestLocalDataSource {
    lateinit var localDataSource: LocalMoviesSourceImpl

    @BeforeTest
    fun setUp() {
        localDataSource = LocalMoviesSourceImpl()
        localDataSource.cache.add(
            Movie(
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
        )
    }

    @Test
    fun `test initializeMovieCache caches the list correctly`() = runTest {
        //Arrange
        localDataSource = LocalMoviesSourceImpl()

        //Act
        localDataSource.initializeMovieCache(
            listOf(
                Movie(
                    2,
                    "Movie 2",
                    "the movie 2 overview",
                    "21/10/2022",
                    "poster url",
                    "backdrop url",
                    "Original Movie 2",
                    "en",
                    9.0,
                    7.0
                )
            )
        )

        //Assert
        assertEquals(
            expected = listOf(
                Movie(
                    2,
                    "Movie 2",
                    "the movie 2 overview",
                    "21/10/2022",
                    "poster url",
                    "backdrop url",
                    "Original Movie 2",
                    "en",
                    9.0,
                    7.0
                )
            ),
            actual = localDataSource.cache
        )
    }

    @Test
    fun `test getMovieList returns the correct list`() = runTest {
        //Arrange

        //Act
        val returnedList = localDataSource.getMovieList()

        //Assert
        assertEquals(
            expected = listOf(
                Movie(
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
            actual = returnedList
        )
    }

    @Test
    fun `test searchMovie with existing id returns correct movie`() = runTest {
        //Arrange

        //Act
        val returnedMovie = localDataSource.searchMovie(1)

        //Assert
        assertEquals(
            expected = Movie(
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
            ),
            actual = returnedMovie
        )
    }

    @Test
    fun `test searchMovie with missing id returns null`() = runTest {
        //Arrange

        //Act
        val returnedMovie = localDataSource.searchMovie(2)

        //Assert
        assertEquals(
            expected = null,
            actual = returnedMovie
        )
    }

    @Test
    fun `test isEmpty returns false when the list is not empty`() = runTest {
        //Arrange

        //Act
        val emptyFlag = localDataSource.isEmpty()

        //Assert
        assertEquals(
            expected = false,
            actual = emptyFlag
        )
    }

    @Test
    fun `test isEmpty returns true when the list is empty`() = runTest {
        //Arrange
        localDataSource.cache = mutableListOf()

        //Act
        val emptyFlag = localDataSource.isEmpty()

        //Assert
        assertEquals(
            expected = true,
            actual = emptyFlag
        )
    }
}