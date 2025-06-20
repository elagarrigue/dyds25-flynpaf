package edu.dyds.movies.data.external

import edu.dyds.movies.domain.entity.Movie
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface RemoteMoviesSource{
    suspend fun getMovieByIdRemote(id: Int): Movie
    suspend fun getPopularMoviesRemote(): List<Movie>

}

class RemoteMoviesSourceImpl(
    private val tmdbHttpClient: HttpClient,
    ):RemoteMoviesSource {

    override suspend fun getMovieByIdRemote(id: Int): Movie = getTMDBMovieDetails(id).toDomainMovie()


    override suspend fun getPopularMoviesRemote(): List<Movie> =
        remoteResultToMovieList(getTMDBPopularMovies())


    private fun remoteResultToMovieList(remoteResult: RemoteResult ): List<Movie> = remoteResult.results.map { it.toDomainMovie() }

    private suspend fun getTMDBMovieDetails(id: Int): RemoteMovie =
        tmdbHttpClient.get("/3/movie/$id").body()

    private suspend fun getTMDBPopularMovies(): RemoteResult =
        tmdbHttpClient.get("/3/discover/movie?sort_by=popularity.desc").body()

}