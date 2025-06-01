package edu.dyds.movies.domain.repository

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.RemoteMovie
import edu.dyds.movies.domain.entity.RemoteResult
import kotlin.collections.listOf

interface MoviesRepository {
    suspend fun getMovieDetailByid(id: Int) : Movie

    suspend fun getPopularMovies() : List<RemoteMovie>

}