package edu.dyds.movies.domain.repository

interface MoviesRepository {
    suspend fun getMovieByid(id: Int)

}