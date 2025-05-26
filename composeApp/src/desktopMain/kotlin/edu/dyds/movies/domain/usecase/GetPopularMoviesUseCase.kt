package edu.dyds.movies.domain.usecase

import edu.dyds.movies.domain.entity.RemoteMovie
import edu.dyds.movies.domain.entity.RemoteResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

public class GetPopularMoviesUseCase(
    private val tmdbHttpClient: HttpClient
){
    private val cacheMovies: MutableList<RemoteMovie> = mutableListOf()

    suspend fun getPopularMovies() =
        if (cacheMovies.isNotEmpty()) {
            cacheMovies
        } else initializeMovieCache()


    private suspend fun initializeMovieCache(): List<RemoteMovie> =
        try {
            initializeMovieCacheUnsafe()
        } catch (e: Exception) {
            emptyList()
        }

    private suspend fun initializeMovieCacheUnsafe(): List<RemoteMovie> =
        getTMDBPopularMovies().results.apply {
            cacheMovies.clear()
            cacheMovies.addAll(this)
        }

    private suspend fun getTMDBPopularMovies(): RemoteResult =
        tmdbHttpClient.get("/3/discover/movie?sort_by=popularity.desc").body()

}