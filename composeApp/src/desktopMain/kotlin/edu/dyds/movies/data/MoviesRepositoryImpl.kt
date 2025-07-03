package edu.dyds.movies.data

import edu.dyds.movies.data.external.MovieExternalSource
import edu.dyds.movies.data.external.MoviesExternalSource
import edu.dyds.movies.data.local.LocalMoviesSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository


class MoviesRepositoryImpl(
    private val localMoviesSource: LocalMoviesSource,
    private val moviesDetailSource: MovieExternalSource,
    private val moviesPopularSource: MoviesExternalSource
) : MoviesRepository {

    override suspend fun getMovieDetailByTitle(title: String): Movie? = try {
        moviesDetailSource.getMovieByTitleRemote(title)
    } catch (e: Exception) {
        null
    }

    override suspend fun getPopularMovies(): List<Movie> = if (localMoviesSource.isEmpty()) getMovies()
    else localMoviesSource.getMovieList()

    private suspend fun getMovies(): List<Movie> = try {
        getMoviesFromRemoteSourceAndSaveToLocalSource()
    } catch (e: Exception) {
        emptyList()
    }

    private suspend fun getMoviesFromRemoteSourceAndSaveToLocalSource(): List<Movie> {
        val movies = getMoviesFromRemoteSource()
        saveMoviesToLocalSource(movies)
        return movies
    }

    private suspend fun getMoviesFromRemoteSource(): List<Movie> = moviesPopularSource.getPopularMoviesRemote()

    private fun saveMoviesToLocalSource(movies: List<Movie>) {
        localMoviesSource.addMovies(movies)
    }

}