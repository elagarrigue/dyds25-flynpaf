package edu.dyds.movies.data.local

import edu.dyds.movies.domain.entity.Movie

interface CacheMovies {
    fun initializeMovieCache(popularMovies: List<Movie>): List<Movie>
    fun searchMovie(id: Int): Movie?
    fun cacheMovie(movie: Movie)
    fun isEmpty():Boolean
    fun getMovieList(): List<Movie>
}




class CacheMoviesImpl: CacheMovies {
    var cache:MutableList<Movie> = mutableListOf()

    override fun searchMovie(id: Int): Movie? = cache.find { it.id == id }

    override fun initializeMovieCache(popularMovies: List<Movie>): List<Movie> {
        cache.clear()
        cache.addAll(popularMovies)
        return popularMovies
    }

    override fun cacheMovie(movie: Movie){
        cache.add(movie)
    }

    override fun isEmpty():Boolean {
        return cache.isEmpty()
    }

    override fun getMovieList(): List<Movie> {
        return cache
    }

}