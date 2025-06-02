package edu.dyds.movies.data.external

import edu.dyds.movies.domain.entity.RemoteMovie
import edu.dyds.movies.domain.entity.RemoteResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class RemoteMovies (
    private val tmdbHttpClient: HttpClient,
    ){

    suspend fun getRemoteMovieById(id: Int): RemoteMovie? = try {
        getTMDBMovieDetails(id)
    } catch (e: Exception) {
        null
    }

    suspend fun getRemotePopularMovies(): List<RemoteMovie> = try {
        getTMDBPopularMovies().results
    } catch (e: Exception) {
        emptyList()
    }

    private suspend fun getTMDBMovieDetails(id: Int): RemoteMovie =
        tmdbHttpClient.get("/3/movie/$id").body()

    private suspend fun getTMDBPopularMovies(): RemoteResult =
        tmdbHttpClient.get("/3/discover/movie?sort_by=popularity.desc").body()

}