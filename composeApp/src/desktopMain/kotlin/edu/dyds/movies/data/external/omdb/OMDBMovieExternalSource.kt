package edu.dyds.movies.data.external.omdb

import edu.dyds.movies.data.external.MovieExternalSource
import edu.dyds.movies.domain.entity.Movie
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*


class OMDBMovieExternalSource(
    private val omdbHttpClient: HttpClient
) : MovieExternalSource {

    override suspend fun getMovieByTitleRemote(title: String): Movie? =
        getOMDBMovieDetails(title).toDomainMovie()


    private suspend fun getOMDBMovieDetails(title: String): RemoteMovie =
        omdbHttpClient.get("/?t=$title").body()

}
