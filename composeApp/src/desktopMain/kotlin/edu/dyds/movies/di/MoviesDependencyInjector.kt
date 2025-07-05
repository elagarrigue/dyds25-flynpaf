package edu.dyds.movies.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.dyds.movies.data.MoviesRepositoryImpl
import edu.dyds.movies.data.external.broker.MovieExternalBroker
import edu.dyds.movies.data.external.omdb.OMDBMovieExternalSource
import edu.dyds.movies.data.external.tmdb.TMDBMovieExternalSource
import edu.dyds.movies.data.local.LocalMoviesSourceImpl
import edu.dyds.movies.domain.repository.MoviesRepository
import edu.dyds.movies.domain.usecase.GetMovieDetailsUseCase
import edu.dyds.movies.domain.usecase.GetMovieDetailsUseCaseImpl
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCase
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCaseImpl
import edu.dyds.movies.presentation.detail.DetailScreenViewModel
import edu.dyds.movies.presentation.home.HomeScreenViewModel
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

private const val API_KEY = "d18da1b5da16397619c688b0263cd281"
private const val OMDB_API_KEY = "ad349a55"

object MoviesDependencyInjector {

    private val omdbHttpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(DefaultRequest) {
            url {
                protocol = URLProtocol.HTTPS
                host = "www.omdbapi.com/"
                parameters.append("apikey", OMDB_API_KEY)
            }
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 5000
        }
    }

    private val tmdbHttpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(DefaultRequest) {
            url {
                protocol = URLProtocol.HTTPS
                host = "api.themoviedb.org"
                parameters.append("api_key", API_KEY)
            }
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 5000
        }
    }

    private var tmdbSource = TMDBMovieExternalSource(tmdbHttpClient)

    private var omdbSource = OMDBMovieExternalSource(omdbHttpClient)

    private var movieExternalBrokerSource = MovieExternalBroker(tmdbSource,omdbSource)

    private var repository: MoviesRepository = MoviesRepositoryImpl(
        localMoviesSource = LocalMoviesSourceImpl(),
        moviesDetailSource = movieExternalBrokerSource,
        moviesPopularSource = tmdbSource,
    )

    @Composable
    fun getHomeScreenViewModel(): HomeScreenViewModel {
        val getPopularMoviesUseCase: GetPopularMoviesUseCase = GetPopularMoviesUseCaseImpl(repository)
        return viewModel { HomeScreenViewModel(getPopularMoviesUseCase) }
    }

    @Composable
    fun getDetailScreenViewModel(): DetailScreenViewModel {
        val getMovieDetailsUseCase: GetMovieDetailsUseCase = GetMovieDetailsUseCaseImpl(repository)
        return viewModel { DetailScreenViewModel(getMovieDetailsUseCase) }
    }

}