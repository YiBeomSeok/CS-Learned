import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ChannelBasis {

    /**
     * 채널은 일종의 파이프이다.
     * 송신측에서 채널에 send로 데이터를 전달하고 수신 측에서 채널을 통해 receive 받는다.
     * `trySend`와 `tryReceive`도 존재한다. (suspension point가 없어 기다리지 않는 함수이다)
     * 과거엔 null을 반환하는 `offer`와 `poll`을 사용하였다.
     */
    @Test
    fun `기본적인 채널`() = runBlocking<Unit> {
        val channel = Channel<Int>()
        launch {
            for (x in 1..10) {
                channel.send(x) // suspension point // 데이터가 없는 경우 잠이 들게된다.
            }
        }

        repeat(10) {
            println(channel.receive())  // suspension point // 데이터가 없는 경우 잠이 들었다가 깨어난다.
        }

        println("완료")
    }

    /**
     * `send`나 `receive`가 suspension point이고 서로에게 의존적이기 때문에 같은 코루틴에서 사용하는 것은 위험할 수 있다.
     * 아래의 테스트를 실행하면 무한으로 대기하게 된다.
     */
    @Test
    fun `같은 코루틴에서 channel 읽고 쓰기`() = runBlocking<Unit> {
        val channel = Channel<Int>()
        launch {
            for (x in 1..10) {
                channel.send(x) // 여기서 launch 블록 자체가 잠이 들게 된다. // suspension point
            }

            repeat(10) {
                println(channel.receive())
            }

            println("완료")
        }
    }

    @Test
    fun `같은 코루팀에서 channel 읽고 쓰기 변경`() = runBlocking<Unit> {
        val channel = Channel<Int>()
        launch {
            for (x in 1..10) {
                channel.send(x)
            }
        }
        launch {
            repeat(10) {
                println(channel.receive())
            }
            println("완료")
        }
    }

    /**
     * 채널에서 더 이상 보낼 자료가 없으면 close 메서드를 이용해 채널을 닫을 수 있다.
     * 채널은 for in 을 이용해서 반복적으로 receive할 수 있고 close되면 for in은 자동으로 종료된다.
     */
    @Test
    fun `채널 close`() = runBlocking<Unit> {
        val channel = Channel<Int>()
        launch {
            for (x in 1..10) {
                channel.send(x)
            }
            channel.close() // 이 부분이 주석처리되면 아래의 for loop는 끝나지 않는다.
        }

        // 명시적으로 close가 없을 경우, 몇 번 가져올 것인지 맞추어야 할 것이다.
        for (x in channel) {
            println(x)
        }
        println("완료")
    }

    @Test
    fun `채널 프로듀서`() = runBlocking<Unit> {
        val oneToTen = produce { // ProducerScope = CoroutineScope + SendChannel // 자기 자신이 코루틴이면서 채널인 것처럼 사용가능
            for (x in 1..10) {  // this.send // this.coroutineContext
                channel.send(x)
            }
        }

        oneToTen.consumeEach {
            println(it)
        }

        println("완료")

        // channel을 만들고 launch로 별도로 코루틴스코프를 만들었던 코드를 위처럼 변경가능
    }
}