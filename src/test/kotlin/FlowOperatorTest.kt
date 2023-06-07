import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

class FlowOperatorTest {

    @Test
    fun `transform 테스트`() = runBlockingTest {
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

    @Test
    fun `drop 연산자 테스트`() = runBlocking {
        (1..20).asFlow().transform {
            emit(it)
        }.drop(5).collect {
            println(it)
        }
    }

    @Test
    fun `reduce 종단 연산자 테스트`() = runBlocking {
        val value = (1..10)
                .asFlow()
                .reduce { a, b ->
                    a + b
                }
        println(value)
    }

    @Test
    fun `fold 연산자 테스트`() = runBlocking {
        val value = (1..10)
                .asFlow()
                .fold(10) { a, b ->
                    a + b
                }
        println(value) // 65
    }

    @Test
    fun `count 연산자 테스트`() = runBlocking {
        val counter = (1..10)
                .asFlow()
//                .filter { // 중간 연산자. 결과 X
//                    (it % 2) == 0
//                }.collect {
//                    print("$it ")
//                }
                .count {// 종단 연산자, terminal operator. 특정 값. 컬렉션. 결과
                    (it % 2) == 0
                }
        println(counter)
    }

    private suspend fun someCal10(i: Int): Int {
        delay(10L)
        return i * 2
    }

    private suspend fun someCal(i: Int): Int {
        delay(100L)
        return i + 1
    }
}
