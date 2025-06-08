package edu.dyds.movies.data

import edu.dyds.movies.data.external.RemoteMoviesSource
import edu.dyds.movies.data.local.LocalMoviesSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository


class MoviesRepositoryImpl(
    private val localMoviesSource: LocalMoviesSource,
    private val remoteMoviesSource: RemoteMoviesSource
) : MoviesRepository {

    override suspend fun getMovieDetailById(id: Int): Movie? = try {
        remoteMoviesSource.getMovieByIdRemote(id)
    } catch (e: Exception) {
        null
    }

    override suspend fun getPopularMovies(): List<Movie> =
            if (localMoviesSource.isEmpty()) getMovies()
            else localMoviesSource.getMovieList()

    private suspend fun getMovies(): List<Movie> =
        try {
            getMoviesFromRemoteSourceAndSaveToLocalSource()
        } catch (e: Exception) {
            emptyList()
        }

    private suspend fun getMoviesFromRemoteSourceAndSaveToLocalSource(): List<Movie> {
        val movies= getMoviesFromRemoteSource()
        saveMoviesToLocalSource(movies)
        return movies
    }

    private suspend fun getMoviesFromRemoteSource(): List<Movie> = remoteMoviesSource.getPopularMoviesRemote()

    private fun saveMoviesToLocalSource(movies: List<Movie>) {
        localMoviesSource.initializeMovieCache(movies)
    }

}