## 순차적 suspend 함수 수행

```kotlin
import kotlin.random.Random
import kotlin.system.*
import kotlinx.coroutines.*

suspend fun getRandom1(): Int {
    delay(1000L)
    return Random.nextInt(0, 500)
}

suspend fun getRandom2(): Int {
    delay(1000L)
    return Random.nextInt(0, 500)
}

fun main() = runBlocking {
    val elapsedTime = measureTimeMillis {
        val value1 = getRandom1()
        val value2 = getRandom2()
        println("${value1} + ${value2} = ${value1 + value2}")
    }
    println(elapsedTime)
}
```

- 한 번에 한 suspend 함수가 수행되었기 때문에 아래와 같은 시간이 걸렸다.

```
189 + 240 = 429
2010
```

- 동시에 다른 블록을 수행할 수 있다. `launch`와 비슷하지만 수행 결과를 `await`키워드를 통해 받을 수 있다는 차이가 있다.
- 결과를 받아야 한다면 `async`, 결과를 받지 않아도 된다면 `launch`를 선택하는 것이 일반적.

```kotlin
import kotlin.random.Random
import kotlin.system.*
import kotlinx.coroutines.*

suspend fun getRandom1(): Int {
    delay(1000L)
    return Random.nextInt(0, 500)
}

suspend fun getRandom2(): Int {
    delay(1000L)
    return Random.nextInt(0, 500)
}

fun main() = runBlocking {
    val elapsedTime = measureTimeMillis {
        val value1 = async { getRandom1() } // 별도 코루틴으로 호출된다.
        val value2 = async { getRandom2() } // 별도 코루틴으로 호출된다.
        // await()는 job.join()과 비슷하지만 결과도 가져온다.
        println("${value1.await()} + ${value2.await()} = ${value1.await() + value2.await()}") // -> 즉, suspension point가 된다
    }
    println(elapsedTime)
}
```

결과의 걸린 시간은 더 효율적임을 보여준다.

```
389 + 401 = 790
1027
```

### CoroutineStart.LAZY

```kotlin
import kotlin.random.Random
import kotlin.system.*
import kotlinx.coroutines.*

suspend fun getRandom1(): Int {
    delay(1000L)
    return Random.nextInt(0, 500)
}

suspend fun getRandom2(): Int {
    delay(1000L)
    return Random.nextInt(0, 500)
}

fun main() = runBlocking {
    val elapsedTime = measureTimeMillis {
        val value1 = async(start = CoroutineStart.LAZY) { getRandom1() } // 코루틴은 만들어지지만 수행 예약을 하지는 않는다.
        val value2 = async(start = CoroutineStart.LAZY) { getRandom2() }

        value1.start()  // 큐에 수행 예약을 한다
        value2.start()

        println("${value1.await()} + ${value2.await()} = ${value1.await() + value2.await()}")
    }
    println(elapsedTime)
}
```