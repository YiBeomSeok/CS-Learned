import org.junit.Test
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.selects.select

/**
 * 채널 팬아웃과 팬인은 공유 채널을 이용하여 여러 코루틴 간에 작접을 분배하고 결과를 수집하는 방법을 나타내는 용어이다.
 */

class ChannelFanOutFanIn {

    /**
     * Fan-out
     * 팬아웃은 한 개의 채널에서 데이터를 읽어 여러 개의 코루틴으로 데이터를 분배하는 패턴
     * 각 코루틴은 독립적으로 동시에 작업을 수행하고 이를 통해 병렬 처리를 구현한다.
     *
     * Fan-in
     * 팬인은 여러 개의 코루틴에서 데이터를 수집하여 한 개의 채널에 데이터를 쓰는 패턴.
     * 각 코루틴은 독립적으로 동시에 작업을 수행하고 결과를 같은 채널에 전송.
     * 이를 통해 여러 출처에서 생성된 데이터를 한 곳으로 집중할 수 있다.
     */

    private fun CoroutineScope.produceNumbers() = produce<Int> {
        var x = 1
        while (true) {
            send(x++)
            delay(100L)
        }
    }

    private fun CoroutineScope.processNumber(id: Int, channel: ReceiveChannel<Int>) = launch {
        channel.consumeEach {
            println("${id}가 ${it}을 받았습니다.")
        }
    }

    // Fan-out
    @Test
    fun `여러 코루틴이 동시에 채널 구독 - Fan-out`() = runBlocking {
        val producer = produceNumbers()
        repeat(5) {
            processNumber(it, producer)
        }
        delay(1000L)
        producer.cancel()
    }

    private suspend fun produceNumbers(channel: SendChannel<Int>, from: Int, interval: Long) {
        var x = from
        while (true) {
            channel.send(x)
            x += 2
            delay(interval)
        }
    }

    private fun CoroutineScope.processNumber(channel: ReceiveChannel<Int>) = launch {
        channel.consumeEach {
            println("${it}을 받았습니다.")
        }
    }

    // Fan-in
    @Test
    fun `생산자가 많은 경우`() = runBlocking {
        val channel = Channel<Int>()

        launch {
            produceNumbers(channel, 1, 100L)
        }

        launch {
            produceNumbers(channel, 2, 150L)
        }

        // 샐산자 2, 소비자 1

        processNumber(channel)
        delay(1000L)
        coroutineContext.cancelChildren()
    }

    private suspend fun someone(channel: Channel<String>, name: String) {
        for (comment in channel) {
            println("${name}: ${comment}")
            channel.send(comment.drop(1) + comment.first())
            delay(100L)
        }
    }

    /**
     * 두 개의 코루틴에서 채널을 서로 사용할 때 공정하게 기회를 준다.
     */
    @Test
    fun `공정한 채널`() = runBlocking {
        val channel = Channel<String>()
        launch {
            someone(channel, "bmsk")
        }

        launch {
            someone(channel, "hwang")
        }
        channel.send("gwang-jin")
        delay(1000L)
        coroutineContext.cancelChildren()
    }

    // 반환 값이 리시브채널
    private fun CoroutineScope.sayFast() = produce<String> {
        // 코루틴 스코프 + 샌드채널
        while (true) {
            delay(100L)
            send("패스트")
        }
    }

    // 반환 값이 리시브채널
    private fun CoroutineScope.sayCampus() = produce<String> {
        // 코루틴 스코프 + 샌드채널
        while (true) {
            delay(150L)
            send("캠퍼스")
        }
    }

    @Test
    fun `먼저 끝나는 요청을 처리 - select`() = runBlocking {
        val fasts = sayFast()
        val compuses = sayCampus()
        repeat(5) {
            select { // select: 먼저 끝내는 것만 처리하겠다.
                fasts.onReceive {
                    println("fast: $it")
                }
                compuses.onReceive {
                    println("campus: $it")
                }
            }
        }
    }

    // 채널에 대해 onReceive를 사용하는 것 이외에도 다음 상황에서 사용할 수 있다.
    /**
     * Job - onJoin (누구의 launch가 빨리?)
     * Deferred - onAwait (async가 반환하는게 Deffered)
     * SendChannel - onSend
     * ReceiveChannel - onReceive, onReceiveCatching
     * delay - onTimeout
     */
}