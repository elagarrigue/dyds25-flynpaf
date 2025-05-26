package edu.dyds.movies.domain.usecase

import edu.dyds.movies.domain.entity.RemoteMovie
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

public class GetMovieDetailsUseCase(
    private val tmdbHttpClient: HttpClient
){
    suspend fun getMovieDetails(id: Int) =
        try {
            getTMDBMovieDetails(id)
        } catch (e: Exception) {
            null
        }

    private suspend fun getTMDBMovieDetails(id: Int): RemoteMovie =
        tmdbHttpClient.get("/3/movie/$id").body()
}