package edu.dyds.movies.data.local

import edu.dyds.movies.domain.entity.Movie

interface LocalMoviesSource {
    fun addMovies(popularMovies: List<Movie>)

    fun isEmpty(): Boolean
    fun getMovieList(): List<Movie>
}

class LocalMoviesSourceImpl : LocalMoviesSource {
    private var cache: MutableList<Movie> = mutableListOf()

    override fun addMovies(popularMovies: List<Movie>) {
        cache.clear()
        cache.addAll(popularMovies)
    }

    override fun isEmpty(): Boolean {
        return cache.isEmpty()
    }

    override fun getMovieList(): List<Movie> {
        return cache
    }

}