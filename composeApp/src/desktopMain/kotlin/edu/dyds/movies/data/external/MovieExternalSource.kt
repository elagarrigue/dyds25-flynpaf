package edu.dyds.movies.data.external

import edu.dyds.movies.domain.entity.Movie

interface MovieExternalSource {
    suspend fun getMovieByTitleRemote(title: String): Movie?
}