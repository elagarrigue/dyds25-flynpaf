package edu.dyds.movies.data

import edu.dyds.movies.data.external.RemoteMoviesSource
import edu.dyds.movies.data.local.LocalMoviesSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository


class MoviesRepositoryImpl(
    private val localMoviesSource: LocalMoviesSource,
    private val remoteMoviesSourceImpl: RemoteMoviesSource
) : MoviesRepository {

    override suspend fun getMovieDetailById(id: Int) : Movie? = localMoviesSource.searchMovie(id) ?: getMovieDetailExternal(id)

    private suspend fun getMovieDetailExternal(id: Int): Movie?{
        val foundMovie = remoteMoviesSourceImpl.getRemoteMovieByIdRemote(id)
        foundMovie?.apply { localMoviesSource.cacheMovie(this) }
        return foundMovie
    }

    override suspend fun getPopularMovies() : List<Movie> {
        if(localMoviesSource.isEmpty()) getPopularMoviesExternal()
        return localMoviesSource.getMovieList()
    }


    private suspend fun getPopularMoviesExternal() {
        val popularMovies= remoteMoviesSourceImpl.getRemotePopularMoviesRemote().ifEmpty { null }
        popularMovies?.apply {localMoviesSource.initializeMovieCache(this)}
    }
}