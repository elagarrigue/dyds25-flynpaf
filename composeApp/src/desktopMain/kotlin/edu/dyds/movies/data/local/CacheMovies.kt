package edu.dyds.movies.data.local

import edu.dyds.movies.domain.entity.RemoteMovie


class CacheMovies {
    private val cacheMovies: MutableList<RemoteMovie> = mutableListOf()


    fun getCacheMovies(): MutableList<RemoteMovie>{
        return cacheMovies
    }
    fun saveMovie(newMovie: RemoteMovie){
        cacheMovies.add(newMovie)
    }

    fun searchMovie(id: Int): RemoteMovie?{
        val foundMovie = cacheMovies.find { it.id == id }
        return foundMovie
    }

    fun isEmpty(): Boolean{
        return cacheMovies.isEmpty()
    }

}