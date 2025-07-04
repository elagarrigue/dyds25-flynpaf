package edu.dyds.movies.data.external.broker

import edu.dyds.movies.data.external.MovieExternalSource
import edu.dyds.movies.domain.entity.Movie

class Broker(
    private val tmdbMovieSource: MovieExternalSource,
    private val omdbMovieSource: MovieExternalSource
) : MovieExternalSource {

    override suspend fun getMovieByTitleRemote(title: String): Movie? {
        val tmdb = try {
            tmdbMovieSource.getMovieByTitleRemote(title)
        } catch (e: Exception) {
            null
        }

        val omdb = try {
            omdbMovieSource.getMovieByTitleRemote(title)
        } catch (e: Exception) {
            null
        }

        return when {
            (tmdb != null && omdb != null) -> buildMovie(tmdb, omdb)
            (tmdb != null) -> tmdb
            (omdb != null) -> omdb
            else -> null
        }
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