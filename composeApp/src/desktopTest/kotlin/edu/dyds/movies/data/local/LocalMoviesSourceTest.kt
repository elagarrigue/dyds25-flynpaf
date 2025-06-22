package edu.dyds.movies.data.local

import edu.dyds.movies.domain.entity.Movie
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.BeforeTest

class LocalMoviesSourceTest {
    lateinit var localDataSource: LocalMoviesSourceImpl

    @BeforeTest
    fun setUp() {
        localDataSource = LocalMoviesSourceImpl()
    }

    @Test
    fun `test movies are added and retrieved correctly`() = runTest {
        //Arrange

        //Act
        localDataSource.addMovies(
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
            ), actual = localDataSource.getMovieList()
        )
    }

    @Test
    fun `test isEmpty returns false when the list is not empty`() = runTest {
        //Arrange
        localDataSource.addMovies(
            listOf(
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
        )
        //Act
        val emptyFlag = localDataSource.isEmpty()

        //Assert
        assertEquals(
            expected = false, actual = emptyFlag
        )
    }

    @Test
    fun `test isEmpty returns true when the list is empty`() = runTest {
        //Arrange

        //Act
        val emptyFlag = localDataSource.isEmpty()

        //Assert
        assertEquals(
            expected = true, actual = emptyFlag
        )
    }
}