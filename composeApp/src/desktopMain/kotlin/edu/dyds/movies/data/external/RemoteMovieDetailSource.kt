package edu.dyds.movies.data.external

import edu.dyds.movies.domain.entity.Movie

interface RemoteMovieDetailSource {
    suspend fun getMovieByTitleRemote(title: String): Movie?
}