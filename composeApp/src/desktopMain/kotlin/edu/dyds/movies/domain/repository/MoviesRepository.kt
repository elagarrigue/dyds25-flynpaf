package edu.dyds.movies.domain.repository

import edu.dyds.movies.domain.entity.Movie


interface MoviesRepository {
    suspend fun getMovieDetailByid(id: Int) : Movie?

    suspend fun getPopularMovies() : List<Movie>

}