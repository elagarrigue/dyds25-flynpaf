package edu.dyds.movies.domain.repository


import edu.dyds.movies.domain.entity.RemoteMovie


interface MoviesRepository {
    suspend fun getMovieDetailByid(id: Int) : RemoteMovie?

    suspend fun getPopularMovies() : List<RemoteMovie>

}