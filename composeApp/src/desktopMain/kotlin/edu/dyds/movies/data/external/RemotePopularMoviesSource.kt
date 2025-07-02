package edu.dyds.movies.data.external

import edu.dyds.movies.domain.entity.Movie

interface RemotePopularMoviesSource {
    suspend fun getPopularMoviesRemote(): List<Movie>
}