import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 *
 */
class FlowException {

    private fun simple(): Flow<Int> = flow {
        for (i in 1..3) {
            println("Emitting $i")
            emit(i)
        }
    }

    /**
     * 예외는 collect를 하는 수집기 측에서도 try-catch 식을 이용해 처리할 수 있다.
     */
    @Test
    fun `수집기 측에서 예외처리`() = runBlocking<Unit> {
        try {
            simple().collect { value ->
                println(value)
                check(value <= 1) { "Collected $value" }
            }
        } catch (e: Throwable) {
            println("Caught $e")
        }
    }

    private fun simple2(): Flow<String> =
            flow {
                for (i in 1..3) {
                    println("Emitting $i")
                    emit(i) // emit next value
                }
            }.map { value ->
                check(value <= 1) { "Crashed on $value" }
                "string $value"
            }

    /**
     * 어느 곳에서 발생한 예외라도 처리가 가능하다.
     */
    @Test
    fun `모든 예외 처리 가능`() = runBlocking {
        try {
            simple2().collect { value -> println(value) }
        } catch (e: Throwable) {
            println("Caught $e")
        }
    }

    /**
     * 빌더 코드 블록 내에서 예외를 처리하는 것은 예외 투명성을 어기는 것이다. (밖에서 예외를 알 수 없다.)
     * 플로우에서는 catch 연산자를 이용하는 것을 권한다.
     *
     * catch 블록에서 예외를 새로운 데이터로 만들어 emit을 하거나, 다시 예외를 던지거나, 로그를 남길 수 있다.
     */
    @Test
    fun `예외 투명성`() = runBlocking<Unit> {
        simple2()
                .catch { e -> emit("Caught $e") } // emit on exception
                .collect { value -> println(value) }
    }

    /**
     * catch 연산자는 업스트림(catch 연산자를 쓰기 전의 코드)에만 영향을 미치고 다운스트림에는 영향을 미치지 않는다.
     * 이를 catch 투명성이라 한다.
     */
    @Test
    fun `catch 투명성`() = runBlocking<Unit> {
        simple()
                .catch { e -> println("Caught $e") } // does not catch downstream exceptions
                .collect { value ->
                    check(value <= 1) { "Collected $value" }
                    println(value)
                }
    }
}