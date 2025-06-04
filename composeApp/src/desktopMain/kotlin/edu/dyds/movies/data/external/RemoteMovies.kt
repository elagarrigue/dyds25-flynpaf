package edu.dyds.movies.data.external

import edu.dyds.movies.domain.entity.Movie
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface RemoteMovies{
    suspend fun getRemoteMovieByIdRemote(id: Int): Movie?
    suspend fun getRemotePopularMoviesRemote(): List<Movie>

}

class RemoteMoviesImpl(
    private val tmdbHttpClient: HttpClient,
    ):RemoteMovies {

    override suspend fun getRemoteMovieByIdRemote(id: Int): Movie? = try {
        toDomainMovie(getTMDBMovieDetails(id))
    } catch (e: Exception) {
        null
    }

    override suspend fun getRemotePopularMoviesRemote(): List<Movie> = try {
        remoteResultToListRemoteMovies(getTMDBPopularMovies())
    } catch (e: Exception) {
        emptyList()
    }

    private fun remoteResultToListRemoteMovies(remoteResult: RemoteResult ): List<Movie>
    {
        val resultList:MutableList<Movie> = mutableListOf()
        resultList.apply { remoteResult.results.forEach { this.add(toDomainMovie(it)) } }
        return resultList
    }

    private suspend fun getTMDBMovieDetails(id: Int): RemoteMovie =
        tmdbHttpClient.get("/3/movie/$id").body()

    private suspend fun getTMDBPopularMovies(): RemoteResult =
        tmdbHttpClient.get("/3/discover/movie?sort_by=popularity.desc").body()

}