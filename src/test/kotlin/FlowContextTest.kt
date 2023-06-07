import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.Test
import java.lang.Exception

class FlowContextTest {

    private fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

    private fun simple(): Flow<Int> = flow {
        log("flow를 시작합니다.")
        for (i in 1..10) {
            emit(i)
        }
    }

    @Test
    fun `SimpleTest`() = runBlocking<Unit> {
        launch(Dispatchers.IO) {
            simple()
                    .collect { value -> log("${value}를 받음.") }
        }
    }

    // 다른 컨텍스트로 옮겨갈 수 없는 플로우
    private fun simple2(): Flow<Int> = flow {
        withContext(Dispatchers.Default) {
            for (i in 1..10) {
                delay(100L)
                emit(i)
            }
        }
    }

    @Test
    fun `다른 컨텍스트로 옮겨갈 수 없는 플로우`() = runBlocking<Unit> {
        launch(Dispatchers.IO) {
            simple2()
                    .collect { value -> log("${value}를 받음.") }
        }
        // Execution failed for task ':test'.
        //
    }

    // flowOn 연산자를 통해 컨텍스트를 올바르게 바꿀 수 있다.
    private fun simple3(): Flow<Int> = flow {
        for (i in 1..10) {
            delay(100L)
            log("값 ${i}를 emit합니다.")
            emit(i)
        } // 업스트림 // Dispatchers.Default
    }.flowOn(Dispatchers.Default) // 위치
            .map { // 다운스트림 (업이냐 다운이냐는 상대적)
                it * 2
            }

    private fun simple4(): Flow<Int> = flow {
        for (i in 1..10) {
            delay(100L)
            log("값 ${i}를 emit합니다.")
            emit(i)
        } // 업스트림 // Dispatchers.IO
    }.flowOn(Dispatchers.IO) // 위치
            .map { // Dispatchers.Default
                it * 2
            }.flowOn(Dispatchers.Default)

    @Test
    fun `flowOn 연산자`() = runBlocking<Unit> {
        simple4().collect { value -> // 다운스트림
            log("${value}를 받음.")
        }
    }
}