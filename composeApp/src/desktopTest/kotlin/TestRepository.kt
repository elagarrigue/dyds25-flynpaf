
import edu.dyds.movies.data.external.RemoteMoviesSource
import edu.dyds.movies.data.local.LocalMoviesSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals


class TestRepository {
    class LocalMoviesSourceSourceImpl : LocalMoviesSource {
        var cache: MutableList<Movie> = mutableListOf()

        override fun searchMovie(id: Int): Movie? = cache.find { it.id == id }

        override fun initializeMovieCache(popularMovies: List<Movie>): List<Movie> {
            cache.clear()
            cache.addAll(popularMovies)
            return popularMovies
        }
        override fun cacheMovie(movie: Movie) { cache.add(movie) }
        override fun isEmpty(): Boolean { return cache.isEmpty() }
        override fun getMovieList(): List<Movie> { return cache }

    }

    class RemoteMoviesSourceImpl():RemoteMoviesSource {
        val MovieExample1 = Movie(1, "Example title 1", "Example overview 1", "Example releaseDate 1", "Example poster 1", "Example backdrop 1", "Example originalTitle 1", "Example original Language 1", 0.0, 0.0)
        val MovieExample2 = Movie(2, "Example title 2", "Example overview 2", "Example releaseDate 2", "Example poster 2", "Example backdrop 2", "Example originalTitle 2", "Example original Language 2", 0.0, 0.0)
        val MovieExample3 = Movie(3, "Example title 3", "Example overview 3", "Example releaseDate 3", "Example poster 3", "Example backdrop 3", "Example originalTitle 3", "Example original Language 3", 0.0, 0.0)

        override suspend fun getMovieByIdRemote(id: Int): Movie? {
            return when (id){
                1 -> MovieExample1
                2 -> MovieExample2
                3 -> MovieExample3
                else -> throw Exception("Pelicula No Encontrada")
            }
        }

        override suspend fun getPopularMoviesRemote(): List<Movie> {
            return listOf(MovieExample1,MovieExample2,MovieExample3)
        }
    }

    class MoviesRepositoryImpl(
        private val localMoviesSource: LocalMoviesSource,
        private val remoteMoviesSource: RemoteMoviesSource
    ) : MoviesRepository {

        override suspend fun getMovieDetailById(id: Int): Movie? = remoteMoviesSource.getMovieByIdRemote(id)

        override suspend fun getPopularMovies(): List<Movie> =
            if (localMoviesSource.isEmpty()) remoteMoviesSource.getPopularMoviesRemote()
            else localMoviesSource.getMovieList()
    }

    private lateinit var moviesRepositoryFake: MoviesRepository
    private lateinit var localMoviesSourceFake : LocalMoviesSource
    private lateinit var remoteMoviesSourceFake : RemoteMoviesSource

    val MovieExample1 = Movie(  1, "Example title 1",
                                "Example overview 1", "Example releaseDate 1",
                                "Example poster 1", "Example backdrop 1",
                                "Example originalTitle 1", "Example original Language 1",
                                0.0, 0.0)
    val MovieExample2 = Movie(  2, "Example title 2",
                                "Example overview 2", "Example releaseDate 2",
                                "Example poster 2", "Example backdrop 2",
                                "Example originalTitle 2", "Example original Language 2",
                                0.0, 0.0)
    val MovieExample3 = Movie(  3, "Example title 3",
                                "Example overview 3", "Example releaseDate 3",
                                "Example poster 3", "Example backdrop 3",
                                "Example originalTitle 3", "Example original Language 3",
                                0.0, 0.0)

    @BeforeTest
    fun setUp(){
        localMoviesSourceFake = LocalMoviesSourceSourceImpl()
            localMoviesSourceFake.initializeMovieCache(listOf(MovieExample1,MovieExample2,MovieExample3))
        remoteMoviesSourceFake = RemoteMoviesSourceImpl()
        moviesRepositoryFake = MoviesRepositoryImpl(localMoviesSourceFake, remoteMoviesSourceFake)

    }

    @Test
    fun `Test de getPopularMovies() Local`()= runTest{
        //arrange

        //act
        val result = moviesRepositoryFake.getPopularMovies()

        // assert
        assertEquals(result,listOf(MovieExample1,MovieExample2,MovieExample3))
    }

    @Test
    fun `Test de getPopularMovies() Remoto`()= runTest{
        //arrange
        localMoviesSourceFake.initializeMovieCache(emptyList())
        //act
        val result = moviesRepositoryFake.getPopularMovies()

        // assert
        assertEquals(result,listOf(MovieExample1,MovieExample2,MovieExample3))
    }

    @Test
    fun `Test de getMovieById() Remoto`()= runTest{
        //arrange

        //act
        val result = moviesRepositoryFake.getMovieDetailById(1)

        // assert
        assertEquals(result,MovieExample1)
    }

    @Test
    fun `Test de getMovieById() Remoto id erroneo`()= runTest{
        //arrange

        //act
        try {
            val result = moviesRepositoryFake.getMovieDetailById(4)
            assertEquals(result,null)
        }catch(e : Exception){

        }
        // assert
    }



}