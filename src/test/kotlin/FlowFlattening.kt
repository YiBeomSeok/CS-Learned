import org.junit.Test
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * 플로우에서는 3가지 유형의 flatMap을 지원한다.
 * flatMapConcat, flatMapMerge, flatMapLatest
 */
class FlowFlattening {

    // 값을 받아서 두 개의 emit을 만든다.
    private fun requestFlow(i: Int): Flow<String> = flow {
        emit("$i: First")
        delay(500) // wait 500 ms
        emit("$i: Second")
    }

    /**
     * flatMapConcat은 첫번째 요소에 대해 플레트닝을 하고 나서 두번째 요소를 적용한다.
     */
    @Test
    fun `flatMapConcat 플레트닝`() = runBlocking<Unit> {
        val startTime = System.currentTimeMillis() // remember the start time
        (1..10).asFlow().onEach { delay(100) }
                .flatMapConcat {
                    requestFlow(it)
                }
                .collect { value -> // collect and print
                    println("$value at ${System.currentTimeMillis() - startTime} ms from start")
                }

        println()

        (1..3).asFlow().onEach {
            delay(100)
            requestFlow(it)
        }.collect { value -> // collect and print
            println("$value at ${System.currentTimeMillis() - startTime} ms from start")
        }
    }

    /**
     * flatMapMerge는 첫 요소의 플레트닝을 시작하며 이어 다음 요소의 플레트닝을 시작한다.
     * Merge는 끝날때까지 기다리지 않고 계속 합친다.
     */
    @Test
    fun `flatMapMerge 플래트닝`() = runBlocking<Unit> {
        val startTime = System.currentTimeMillis()
        (1..10).asFlow().onEach { delay(100) }
                .flatMapMerge {
                    requestFlow(it)
                }
                .collect { value ->
                    println("$value at ${System.currentTimeMillis() - startTime} ms from start")
                }
    }

    /**
     * flatMapLatest는 다음 요소의 플레트닝을 시작하며 이전에 진행 중이던 플레트닝을 취소한다. (결국 마지막을 남긴다)
     */
    @Test
    fun `flatMapLatest 플래트닝`() = runBlocking<Unit> {
        val startTime = System.currentTimeMillis()
        (1..3).asFlow().onEach { delay(100L) }
                .flatMapLatest {    // requestFlow(1).cancel(), requestFlow(2).cancel(), requestFlow(3)
                    requestFlow(it)
                }
                .collect { value ->
                    println("$value at ${System.currentTimeMillis() - startTime} ms from start")
                }
    }
}