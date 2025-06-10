package edu.dyds.movies.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.dyds.movies.data.MoviesRepositoryImpl
import edu.dyds.movies.data.external.RemoteMoviesSourceImpl
import edu.dyds.movies.data.local.LocalMoviesSourceSourceImpl
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

object MoviesDependencyInjector {

    private val tmdbHttpClient =
        HttpClient {
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

    private var repository: MoviesRepository = MoviesRepositoryImpl(
        LocalMoviesSourceSourceImpl(),
        RemoteMoviesSourceImpl(tmdbHttpClient)
    )

    @Composable
    fun getHomeScreenViewModel(): HomeScreenViewModel{
        val getPopularMoviesUseCase:GetPopularMoviesUseCase = GetPopularMoviesUseCaseImpl(repository)
        return viewModel { HomeScreenViewModel(getPopularMoviesUseCase)}
    }

    @Composable
    fun getDetailScreenViewModel(): DetailScreenViewModel {
        val getMovieDetailsUseCase:GetMovieDetailsUseCase = GetMovieDetailsUseCaseImpl(repository)
        return viewModel { DetailScreenViewModel( getMovieDetailsUseCase)}
    }

}