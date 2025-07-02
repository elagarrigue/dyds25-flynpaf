package edu.dyds.movies.data.external.tmdb

import edu.dyds.movies.data.external.RemoteMovieDetailSource
import edu.dyds.movies.data.external.RemotePopularMoviesSource
import edu.dyds.movies.data.external.RemoteResult
import edu.dyds.movies.domain.entity.Movie
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class TMDBMovieExternalSource(
    private val tmdbHttpClient: HttpClient,
    ): RemoteMovieDetailSource, RemotePopularMoviesSource {

    override suspend fun getMovieByTitleRemote(title: String): Movie =
        getTMDBMovieDetails(title).apply { println(this) }.results.first().toDomainMovie()


    override suspend fun getPopularMoviesRemote(): List<Movie> =
        remoteResultToMovieList(getTMDBPopularMovies())


    private fun remoteResultToMovieList(remoteResult: RemoteResult): List<Movie> = remoteResult.results.map { it.toDomainMovie() }

    private suspend fun getTMDBMovieDetails(title: String): RemoteResult =
        tmdbHttpClient.get("/3/search/movie?query=$title").body()

    private suspend fun getTMDBPopularMovies(): RemoteResult =
        tmdbHttpClient.get("/3/discover/movie?sort_by=popularity.desc").body()

}