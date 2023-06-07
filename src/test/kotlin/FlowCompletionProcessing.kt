import kotlinx.coroutines.flow.*
import kotlinx.coroutines.*
import org.junit.Test

class FlowCompletionProcessing {
    private fun simple(): Flow<Int> = (1..3).asFlow()

    /**
     * 완료를 처리하는 방법 중의 하나는 명령형의 방식으로 finally 블록을 이용하는 것
     * 예외가 발생하든 발생하지 않든 finally로 이동한다.
     */
    @Test
    fun `flow finally`() = runBlocking<Unit> {
        try {
            simple().collect { value -> println(value) }
        } finally {
            println("Done")
        }
    }

    /**
     * onCompletion 연산자를 선언해서 완료를 처리할 수 있다.
     */
    @Test
    fun `flow onComplete`() = runBlocking<Unit> {
        simple()
                .map {
                    if (it > 2) {
                        throw IllegalStateException()
                    }
                    it + 1
                }
                .catch { e -> emit(-99) }
                .onCompletion { println("Done") }
                .collect { println(it) }
    }

    private fun simple2(): Flow<Int> = flow {
        emit(1)
        throw RuntimeException()
    }

    /**
     * onCompletion은 종료 처리를 할 때 예외가 발생되었는지 여부를 알 수 있다.
     * try - finally에선 finally에서의 문제를 알 수 없다.
     */
    @Test
    fun `onCompletion의 장점`() = runBlocking<Unit> {
        simple2()
                .onCompletion { cause ->
                    if (cause != null) {
                        println("Flow completed exceptionally")
                    } else {
                        println("Flow completed.")
                    }
                }
                .catch { cause -> println("Caught exception") }
                .collect { value -> println(value) }
    }
}