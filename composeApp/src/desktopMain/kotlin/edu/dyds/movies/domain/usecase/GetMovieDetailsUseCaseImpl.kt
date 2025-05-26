package edu.dyds.movies.domain.usecase

import edu.dyds.movies.domain.entity.RemoteMovie
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get


interface GetMovieDetailsUseCase{
    suspend fun getMovieDetails(id: Int): RemoteMovie?
}

class GetMovieDetailsUseCaseImpl(
    private val tmdbHttpClient: HttpClient
): GetMovieDetailsUseCase{
    override suspend fun getMovieDetails(id: Int) =
        try {
            getTMDBMovieDetails(id)
        } catch (e: Exception) {
            null
        }

    private suspend fun getTMDBMovieDetails(id: Int): RemoteMovie =
        tmdbHttpClient.get("/3/movie/$id").body()
}