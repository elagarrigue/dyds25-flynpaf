package edu.dyds.movies.data

import edu.dyds.movies.data.external.RemoteMoviesSource
import edu.dyds.movies.data.local.LocalMoviesSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository


class MoviesRepositoryImpl(
    private val localMoviesSource: LocalMoviesSource,
    private val remoteMoviesSourceImpl: RemoteMoviesSource
) : MoviesRepository {

    override suspend fun getMovieDetailById(id: Int): Movie? = try {
        remoteMoviesSourceImpl.getMovieByIdRemote(id)
    } catch (e: Exception) {
        null
    }

    override suspend fun getPopularMovies(): List<Movie> =
            if (localMoviesSource.isEmpty()) {
                getMovies()
            } else localMoviesSource.getMovieList()

    private suspend fun getMovies(): List<Movie> =
        try {
            getMoviesFromRemoteAndSaveToLocal()
        } catch (e: Exception) {
            emptyList()
        }

    private suspend fun getMoviesFromRemoteAndSaveToLocal(): List<Movie> {
        val movies= getMoviesFromRemoteSource()
        saveMoviesToLocalSource(movies)
        return movies

    }

    private suspend fun getMoviesFromRemoteSource(): List<Movie> = remoteMoviesSourceImpl.getPopularMoviesRemote()

    private fun saveMoviesToLocalSource(movies: List<Movie>) {
        localMoviesSource.initializeMovieCache(movies)
    }

}