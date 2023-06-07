import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

class FlowTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun `transform 테스트`() = testScope.runBlockingTest {
        (1..20).asFlow().transform {
            emit(it)
            emit(someCal(it))
        }.collect {
            println(it)
        }
    }

    @Test
    fun `transform take 테스트`() = runBlocking {
        (1..20).asFlow().transform {
            emit(it)
            emit(someCal(it))
        }.take(5).collect {
            println(it)
        }
    }

    @Test
    fun `takeWhile 연산자 테스트`() = runBlocking {
        (1..20).asFlow().transform {
            emit(it)
            emit(someCal(it))
        }.takeWhile {
            it < 15
        }.collect {
            println(it)
        }
    }

    private suspend fun someCal(i: Int): Int {
        delay(100L)
        return i + 1
    }
}
