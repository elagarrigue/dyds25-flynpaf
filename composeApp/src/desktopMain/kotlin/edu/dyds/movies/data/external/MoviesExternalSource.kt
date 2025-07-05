package edu.dyds.movies.data.external

import edu.dyds.movies.domain.entity.Movie

interface MoviesExternalSource {
    suspend fun getPopularMoviesRemote(): List<Movie>
}