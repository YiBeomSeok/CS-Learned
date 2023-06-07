import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

// data를 만드는 측과 소비자 입장의 속도가 같은 속도로 움직일 수는 없다.
// 그렇기 때문에 버퍼를 만들어 유연하게 동작하도록 만들 수 있다.
class FlowBufferingTest {

    // 생산자
    private fun simple(): Flow<Int> = flow {
        for (i in 1..3) {
            delay(100)
            emit(i)
        }
    }

    // 버퍼가 없는 플로우
    // 보내는 쪽과 받는 쪽이 모두 바쁘다고 가정
    @Test
    fun `버퍼가 없는 플로우`() = runBlocking<Unit> {
        val time = measureTimeMillis {
            simple().collect { value ->
                delay(300)
                println(value)
            }
        }

        println("Collected in $time ms")
    }

    // buffer로 버퍼를 추가해 보내는 측이 더 이상 기다리지 않게 한다.
    @Test
    fun `버퍼가 있는 플로우`() = runBlocking<Unit> {
        val time = measureTimeMillis {
            simple().buffer()
                    .collect { value ->
                        delay(300)
                        println(value)
                    }
        }

        println("Collected in $time ms")
    }

    /**
     * `conflate`를 이용하면 중간의 값을 융합할 수 있다.
     * 처리보다 빨리 발생한 데이터의 중간 값들을 누락한다.
     */
    @Test
    fun `conflate 사용`() = runBlocking<Unit> {
        val time = measureTimeMillis {
            simple().conflate()
                    .collect { value ->
                        delay(300)
                        println(value)
                    }
        }
        println("Collected in $time ms")
    }

    /**
     * 마지막 값만 처리하기
     * `collectLatest`는 마지막 값을 기다리는 것은 아니다.
     * 첫번째 값을 받고 처리하는 과정에 2번째 값이 왔다면 리셋이 된다. 이후에도 반복된다.
     */
    @Test
    fun `collectLatest 사용`() = runBlocking<Unit> {
        val time = measureTimeMillis {
            simple().collectLatest { value ->
                println("값 ${value}를 처리하기 시작합니다.")
                delay(300)
                println(value)
                println("처리를 완료하였습니다.")
            }
        }
        println("Collected in $time ms")
    }
}