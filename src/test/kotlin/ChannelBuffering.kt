import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import org.junit.Test

class ChannelBuffering {

    /**
     * Channel 생성자는 인자로 버퍼의 사이즈를 지정 받는다.
     * 지정하지 않으면 버퍼를 생성하지 않는다.
     * 버퍼가 있어 좀 더 유연하게 채널을 사용할 수 있다.
     */
    @Test
    fun `Channel Buffer 생성`() = runBlocking<Unit> {
        val channel = Channel<Int>(10)  // 채널의 버퍼 개수 10
        launch {
            for (x in 1..20) {
                println("$x 전송중")
                channel.send(x) // buffer가 있기 때문에 receive가 있든 없든 계속 보낸다(버퍼 한도 내에서)
            }
            channel.close()
        }

        for (x in channel) {
            println("$x 수신")
            delay(100L) // 100 ms
        }
        println("완료")
    }

    /**
     * 랑데뷰는 버퍼 사이즈를 0으로 지정하는 것이다. 생성자에 사이즈를 전달하지 않으면 랑데뷰가 디폴트이다.
     * UNLIMITED - 무제한 버퍼 (링크드리스트로 메모리 크기에 제한을 받게되어 실제로는 무제한일 수 없다)
     * CONFLATED - 오래된 값을 지우는 모드
     * BUFFERED - 64개의 버퍼. 오버플로우에는 suspend
     */
    @Test
    fun `랑데뷰 버퍼 사이즈`() = runBlocking {
        val channel = Channel<Int>(Channel.RENDEZVOUS)
        launch {
            for (x in 1..20) {
                println("$x 전송중")
                channel.send(x)
            }
            channel.close()
        }

        for (x in channel) {
            println("$x 수신")
            delay(100L)
        }
        println("완료")
    }

    /**
     * 버퍼의 오버플로우 정책에 따라 다른 결과가 나올 수 있다.
     * SUSPEND - 잠이 들었다 깨어난다.
     * DROP_OLDEST - 예전 데이터를 지운다.
     * DROP_LATEST - 처리하지 못 한 새 데이터를 지운다.
     */
    @Test
    fun `버퍼 오버플로우`() = runBlocking<Unit> {
        val channel = Channel<Int>(2, BufferOverflow.DROP_LATEST)
        launch {
            for (x in 1..50) {
                channel.send(x)
            }
            channel.close()
        }

        delay(500L)

        for (x in channel) {
            println("$x 수신")
            delay(100L)
        }
        println("완료")
    }
}