package edu.dyds.movies.domain.repository

import edu.dyds.movies.domain.entity.Movie


interface MoviesRepository {
    suspend fun getMovieDetailByTitle(title: String) : Movie?

    suspend fun getPopularMovies() : List<Movie>

}