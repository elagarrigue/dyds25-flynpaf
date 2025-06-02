package edu.dyds.movies.data.local

import edu.dyds.movies.domain.entity.RemoteMovie


class CacheMovies: ArrayList<RemoteMovie>() {
    fun searchMovie(id: Int): RemoteMovie? = this.find { it.id == id }

    fun initializeMovieCache(popularMovies: List<RemoteMovie>): List<RemoteMovie> {
        this.clear()
        this.addAll(popularMovies)
        return popularMovies
    }
}