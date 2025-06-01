package edu.dyds.movies.data.local

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.RemoteMovie
import edu.dyds.movies.domain.repository.MoviesRepository

class CacheMovies {
    private val cacheMovies: MutableList<Movie> = mutableListOf()


    fun getCacheMovies(): MutableList<Movie>{
        return cacheMovies
    }
    fun saveMovie(newMovie: Movie){
        cacheMovies.add(newMovie)
    }

    fun searchMovie(id: Int): Movie?{
        val foundMovie = cacheMovies.find { it.id == id }
        return foundMovie
    }

    fun isEmpty(): Boolean{
        return cacheMovies.isEmpty()
    }

}