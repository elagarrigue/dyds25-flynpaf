package edu.dyds.movies.data

import edu.dyds.movies.data.external.RemoteMovies
import edu.dyds.movies.data.local.CacheMovies
import edu.dyds.movies.domain.entity.RemoteMovie
import edu.dyds.movies.domain.repository.MoviesRepository
import io.ktor.client.HttpClient

class MoviesRepositoryImpl(
    tmdbHttpClient: HttpClient,
) : MoviesRepository {
    private val cacheMovies = CacheMovies()
    private val remoteMovies = RemoteMovies(tmdbHttpClient)

    override suspend fun getMovieDetailByid(id: Int) : RemoteMovie? = cacheMovies.searchMovie(id) ?: getMovieDetailExternalAndCache(id)

    private suspend fun getMovieDetailExternalAndCache(id: Int): RemoteMovie?{
        val foundMovie = remoteMovies.getRemoteMovieById(id)
        foundMovie?.apply { cacheMovies.add(this) }
        return foundMovie
    }

    override suspend fun getPopularMovies() : List<RemoteMovie> = cacheMovies.ifEmpty { getPopularMoviesExternalAndCache() }

    private suspend fun getPopularMoviesExternalAndCache(): List<RemoteMovie> {
        val popularMovies= remoteMovies.getRemotePopularMovies().ifEmpty { null }
        popularMovies?.apply {cacheMovies.initializeMovieCache(this)}
        return cacheMovies
    }
}