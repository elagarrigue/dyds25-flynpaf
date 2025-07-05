package edu.dyds.movies.data.external.broker

import edu.dyds.movies.data.external.MovieExternalSource
import edu.dyds.movies.domain.entity.Movie

class MovieExternalBroker(
    private val tmdbMovieSource: MovieExternalSource, private val omdbMovieSource: MovieExternalSource
) : MovieExternalSource {

    override suspend fun getMovieByTitleRemote(title: String): Movie? {
        val tmdb = getTMDBMovieByTitle(title)

        val omdb = getOMDBMovieByTitle(title)

        return when {
            (tmdb != null && omdb != null) -> buildMovie(tmdb, omdb)
            (tmdb != null) -> tmdb
            (omdb != null) -> omdb
            else -> null
        }
    }

    private suspend fun getOMDBMovieByTitle(title: String): Movie? = try {
        omdbMovieSource.getMovieByTitleRemote(title)
    } catch (e: Exception) {
        null
    }

    private suspend fun getTMDBMovieByTitle(title: String): Movie? = try {
        tmdbMovieSource.getMovieByTitleRemote(title)
    } catch (e: Exception) {
        null
    }

    private fun buildMovie(
        tmdbMovie: Movie, omdbMovie: Movie
    ): Movie {
        return Movie(
            id = tmdbMovie.id,
            title = tmdbMovie.title,
            overview = "${tmdbMovie.overview}\n\n${omdbMovie.overview}",
            releaseDate = tmdbMovie.releaseDate,
            poster = tmdbMovie.poster,
            backdrop = tmdbMovie.backdrop,
            originalTitle = tmdbMovie.originalTitle,
            originalLanguage = tmdbMovie.originalLanguage,
            popularity = (tmdbMovie.popularity + omdbMovie.popularity) / 2.0,
            voteAverage = (tmdbMovie.voteAverage + omdbMovie.voteAverage) / 2.0,
        )
    }

}