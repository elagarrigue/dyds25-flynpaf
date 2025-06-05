package edu.dyds.movies.data

import edu.dyds.movies.data.external.RemoteMoviesSource
import edu.dyds.movies.data.local.LocalMoviesSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository


class MoviesRepositoryImpl(
    private val localMoviesSource: LocalMoviesSource,
    private val remoteMoviesSourceImpl: RemoteMoviesSource
) : MoviesRepository {

    override suspend fun getMovieDetailById(id: Int) : Movie? = try {
        remoteMoviesSourceImpl.getRemoteMovieByIdRemote(id)
    } catch (e: Exception) {
        null
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