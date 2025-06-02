package edu.dyds.movies.data

import edu.dyds.movies.data.external.RemoteMovies
import edu.dyds.movies.data.local.CacheMovies
import edu.dyds.movies.domain.entity.RemoteMovie
import edu.dyds.movies.domain.repository.MoviesRepository
import io.ktor.client.HttpClient

class MoviesRepositoryImpl() : MoviesRepository {
    private val cacheMovies = CacheMovies()
    private val remoteMovies = RemoteMovies()

    override suspend fun getMovieDetailByid(id: Int) : RemoteMovie {
        var foundMovie = cacheMovies.searchMovie(id)
        if(foundMovie == null){
            foundMovie = remoteMovies.getRemoteMovieById(id)
            cacheMovies.saveMovie(foundMovie)
        }
        return foundMovie
    }

    override suspend fun getPopularMovies() : List<RemoteMovie>{
        var popularMovies = listOf<RemoteMovie>()
        if(!cacheMovies.isEmpty()){
            popularMovies = cacheMovies.getCacheMovies()
        }else{
            try {
                popularMovies = remoteMovies.getRemotePopularMovies().results
            }catch (e: Exception){
                popularMovies = emptyList()
            }
        }
        return  popularMovies
    }
}