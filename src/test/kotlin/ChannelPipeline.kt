import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ChannelPipeline {

    /**
     * 파이프라인은 일반적인 패턴이다. 하나의 스트림을 프로듀서가 만들고, 다른 코루틴에서 그 스트림을 읽어 새로운 스트림을 만드는 패턴이다.
     */

    private fun CoroutineScope.produceNumbers() = produce<Int> {
        var x = 1
        while(true) {
            send(x++)
        }
    }

    private fun CoroutineScope.produceStringNumbers(numbers: ReceiveChannel<Int>): ReceiveChannel<String> = produce {
        for (i in numbers) {
            send("${i}!")
        }
    }

    @Test
    fun `파이프라인`() = runBlocking<Unit> {
        val numbers = produceNumbers()
        val stringNumbers = produceStringNumbers(numbers)

        repeat(10) {
            println(stringNumbers.receive())    // close가 없기 때문에 명시적 receive
        }

        println("완료")
        coroutineContext.cancelChildren() // numbers와 stringNumbers 모두 취소
    }


    // send 채널, ProducerScope = CoroutineScope + SendChannel
    private fun CoroutineScope.filterOdd(numbers: ReceiveChannel<Int>): ReceiveChannel<String> = produce {
        for (i in numbers) {
            if (i % 2 == 1) {
                send("${i}!")
            }
        }
    }

    @Test
    fun `홀수 필터`() = runBlocking<Unit> {
        val numbers = produceNumbers()  // receive 채널, send X
        val oddNumbers = filterOdd(numbers)

        repeat(10) {
            println(oddNumbers.receive())
        }

        println("완료")
        coroutineContext.cancelChildren()
    }

    private fun CoroutineScope.numbersFrom(start: Int) = produce<Int> {
        var x = start
        while(true) {
            send(x++)
        }
    }

    private fun CoroutineScope.filter(numbers: ReceiveChannel<Int>, prime: Int): ReceiveChannel<Int> = produce {
        for (i in numbers) {
            if (i % prime != 0) {
                send(i)
            }
        }
    }

    @Test
    fun `소수 필터`() = runBlocking {
        var numbers = numbersFrom(2) // Receive 채널

        repeat(100) {
            val prime = numbers.receive() // 2
            println(prime)
            numbers = filter(numbers, prime) // loop가 돌 때마다 numbers 채널은 대체된다.
        }
        println("완료")
        coroutineContext.cancelChildren()
    }
}