package edu.dyds.movies.data

import edu.dyds.movies.data.external.RemoteMovies
import edu.dyds.movies.data.local.CacheMovies
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository


class MoviesRepositoryImpl(
    private val cacheMovies: CacheMovies,
    private val remoteMoviesImpl: RemoteMovies
) : MoviesRepository {

    override suspend fun getMovieDetailByid(id: Int) : Movie? = cacheMovies.searchMovie(id) ?: getMovieDetailExternal(id)

    private suspend fun getMovieDetailExternal(id: Int): Movie?{
        val foundMovie = remoteMoviesImpl.getRemoteMovieByIdRemote(id)
        foundMovie?.apply { cacheMovies.cacheMovie(this) }
        return foundMovie
    }

    override suspend fun getPopularMovies() : List<Movie> {
        if(cacheMovies.isEmpty()) getPopularMoviesExternal()
        return cacheMovies.getMovieList()
    }


    private suspend fun getPopularMoviesExternal() {
        val popularMovies= remoteMoviesImpl.getRemotePopularMoviesRemote().ifEmpty { null }
        popularMovies?.apply {cacheMovies.initializeMovieCache(this)}
    }
}