package edu.dyds.movies.data.external.broker

import edu.dyds.movies.data.external.MovieExternalSource
import edu.dyds.movies.domain.entity.Movie

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
    "Movie 1".hashCode(),
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

    private lateinit var broker: MovieExternalSource


    /* TODO: Test ambos servicios funcionan, inicializar el broker con los fakes correctos y fijarse que lo que devuelve es igual a getCombinedMovieExample() */

    /* TODO: Test solo tmdb funciona, incializar el broker con el fake tmdb correcto y el unavailable en el campo de omdb, fijarse que lo que devuelve es igual a getTMDBMovieExample() */

    /* TODO: Test solo omdb funciona, incializar el broker con el fake omdb correcto y el unavailable en el campo de tmdb, fijarse que lo que devuelve es igual a getOMDBMovieExample() */

    /* TODO: Test ninguno de los servicios funciona, inicializar el broker con el fake unavailable en ambos campos y fijarse que devuelve null */

}

