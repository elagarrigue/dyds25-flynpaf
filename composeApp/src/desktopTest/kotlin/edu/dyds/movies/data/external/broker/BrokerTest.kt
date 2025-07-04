package edu.dyds.movies.data.external.broker

import edu.dyds.movies.data.external.MovieExternalSource
import edu.dyds.movies.domain.entity.Movie
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

private fun getTMDBMovieExample(): Movie = Movie(
    1,
    "Movie 1",
    "TMDB: the movie 1 overview",
    "21/10/2023",
    "poster url",
    "backdrop url",
    "Original Movie 1",
    "en",
    10.0,
    8.0
)

private fun getOMDBMovieExample(): Movie = Movie(
    "Movie OMDB".hashCode(),
    "Movie 1",
    "OMDB: the movie 1 overview",
    "2023",
    "poster url",
    "backdrop url",
    "Original Movie 1",
    "en",
    8.0,
    6.0
)

private fun getCombinedMovieExample(): Movie = Movie(
    1,
    "Movie 1",
    "TMDB: the movie 1 overview\n\n OMDB: the movie 1 overview",
    "21/10/2023",
    "poster url",
    "backdrop url",
    "Original Movie 1",
    "en",
    9.0,
    7.0
)



class BrokerTest{
    private val tmdbMovieExternalSourceFake = object : MovieExternalSource {
        override suspend fun getMovieByTitleRemote(title: String): Movie {
            return getTMDBMovieExample()
        }
    }
    private val omdbMovieExternalSourceFake = object: MovieExternalSource {
        override suspend fun getMovieByTitleRemote(title: String): Movie {
            return getOMDBMovieExample()
        }
    }
    private val movieExternalSourceResourceUnavailableFake = object: MovieExternalSource{
        override suspend fun getMovieByTitleRemote(title: String): Movie {
            throw Exception("error message")
        }
    }

    @Test
    fun `test both APIs work`() = runTest{
        //Arrange
        val brokerSource = Broker(
            tmdbMovieExternalSourceFake,
            omdbMovieExternalSourceFake
        )
        //Act
        val expected = getCombinedMovieExample()
        val result = brokerSource.getMovieByTitleRemote("Movie 1")
        //Assert
        assertEquals(expected,result)
    }

    @Test
    fun `test only TMDB is working`() = runTest {
        //Arrange
        val brokerSource = Broker(
            tmdbMovieExternalSourceFake,
            movieExternalSourceResourceUnavailableFake
        )
        //Act
        val expected = getTMDBMovieExample()
        val result = brokerSource.getMovieByTitleRemote("Movie 1")
        //Assert
        assertEquals(expected,result)
    }

    @Test
    fun `test only OMDB is working`() = runTest {
        //Arrange
        val brokerSource = Broker(
            movieExternalSourceResourceUnavailableFake,
            omdbMovieExternalSourceFake
        )
        //Act
        val expected = getOMDBMovieExample()
        val result = brokerSource.getMovieByTitleRemote("Movie 1")
        //Assert
        assertEquals(expected,result)
    }

    @Test
    fun `test none of the APIs work`() = runTest {
        //Arrange
        val brokerSource = Broker(
            movieExternalSourceResourceUnavailableFake,
            movieExternalSourceResourceUnavailableFake
        )
        //Act
        val expected = null
        val result = null
        //Assert
        assertEquals(expected,result)
    }

}

