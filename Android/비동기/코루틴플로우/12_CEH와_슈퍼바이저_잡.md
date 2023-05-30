## GlobalScope

어디에서 속하지 않지만 원래부터 존재하는 전역 `GlobalScope`가 있다. 이 전역 스코프를 이용하여 코루틴을 쉽게 수행할 수 있다.

```kotlin
import kotlin.random.Random
import kotlin.system.*
import kotlinx.coroutines.*

suspend fun printRandom() {
    delay(500L)
    println(Random.nextInt(0, 500))
}

fun main() {
    val job = GlobalScope.launch(Dispatchers.IO) {
        launch { printRandom() }
    }
    Thread.sleep(1000L)
}
```

간편하게 사용할 수 있지만, 어떤 계층에도 속하지 않고 영원히 동작하게 되어 관리하기 어렵다는 문제점이 있다.
프로그래밍에서 전역 객체를 잘 사용하지 않는 것 처럼 `GlobalScope` 또한 잘 사용되지 않는다.

## Coroutine Scope

`GlobalScope`보다 권장되는 형식.
[생명주기 관리와 메모리 누수를 방지](%EC%98%88%EC%8B%9C%2FCoroutineScope_vs_GlobalScope.md)하기 위함.

```kotlin
import kotlin.random.Random
import kotlin.system.*
import kotlinx.coroutines.*

suspend fun printRandom() {
    delay(500L)
    println(Random.nextInt(0, 500))
}

fun main() {
    val scope = CoroutineScope(Dispatchers.Default + CoroutineName("Scope"))
    val job = scope.launch(Dispatchers.IO) {
        launch { printRandom() }
    }
    Thread.sleep(1000L)
}
```

## CEH (CoroutineExceptionHandler)

예외를 가장 체계적으로 다루는 방법으로 CEH를 이용하는 방법이 있다.

`CoroutineExceptionHandler`를 이용해서 상위 코루틴 빌더의 컨텍스트에 등록한다.

```kotlin
import kotlin.random.Random
import kotlin.system.*
import kotlinx.coroutines.*

suspend fun printRandom1() {
    delay(1000L)
    println(Random.nextInt(0, 500))
}

suspend fun printRandom2() {
    delay(500L)
    throw ArithmeticException()
}

val ceh = CoroutineExceptionHandler { _, exception ->
    println("Something happend: $exception")
}

fun main() = runBlocking<Unit> {
    val scope = CoroutineScope(Dispatchers.IO)
    val job = scope.launch(ceh) {
        launch { printRandom1() }
        launch { printRandom2() }
    }
    job.join()
}
```

```
Something happend: java.lang.ArithmeticException
```

### runBlocking과 CEH

`runBlocking`에서는 CEH를 사용할 수 없다.

```kotlin
import kotlin.random.Random
import kotlin.system.*
import kotlinx.coroutines.*

suspend fun getRandom1(): Int {
    delay(1000L)
    return Random.nextInt(0, 500)
}

suspend fun getRandom2(): Int {
    delay(500L)
    throw ArithmeticException()
}

val ceh = CoroutineExceptionHandler { _, exception ->
    println("Something happend: $exception")
}

fun main() = runBlocking<Unit> { // 1 최상단 코루틴
    val job = launch(ceh) { // 2
        val a = async { getRandom1() } // 3
        val b = async { getRandom2() } // 3
        println(a.await())
        println(b.await())
    }
    job.join()
}
```

## SupervisorJob

SupervisorJob은 예외에 의한 취소를 아래쪽으로 내려가게 한다.

아래의 예제는 job2에서 문제(예외)가 발생하고, 부모에게 예외를 올려 보내고 스코프가 캔슬되어 그 자식인 job1도 캔슬되는 것이 일반적이나, job1은 캔슬되지 않는 것을 볼 수 있다.

```kotlin
import kotlin.random.Random
import kotlin.system.*
import kotlinx.coroutines.*

suspend fun printRandom1() {
    delay(1000L)
    println(Random.nextInt(0, 500))
}

suspend fun printRandom2() {
    delay(500L)
    throw ArithmeticException()
}

val ceh = CoroutineExceptionHandler { _, exception ->
    println("Something happend: $exception")
}

fun main() = runBlocking<Unit> {
    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob() + ceh)
    val job1 = scope.launch { printRandom1() }
    val job2 = scope.launch { printRandom2() }
    joinAll(job1, job2)
}
```

### SupervisorScope

- 일반적으로 코루틴 스코프 내에서 발생한 오류는 해당 스코프에 있는 모든 코루틴을 취소한다. 그러나 `SupervisorScope`를 사용하면 이러한 오류 전파를 제한할 수 있다.
- `SupervisorScope` 내에서 하나의 자식 코루틴에서 오류가 발생하더라도, 다른 자식 코루틴들은 계속 실행된다. 즉 `SupervisorScope`는 자식 코루틴들이 독립적으로 실행되도록 보장한다. 이는
  오류가 발생한 코루틴이 전체 작업의 진행을 방해하는 것을 방지하며, 각 코루틴이 개별적으로 오류를 처리할 수 있도록 한다.
- 따라서, `SupervisorScope`는 병렬 작업이 필요하고, 각 작업이 독립적으로 실행되어야 하며, 하나의 작업에서 발생한 오류가 다른 작업에 영향을 미치지 않아야 하는 경우에 유용하다.

```kotlin
import kotlin.random.Random
import kotlin.system.*
import kotlinx.coroutines.*

suspend fun printRandom1() {
    delay(1000L)
    println(Random.nextInt(0, 500))
}

suspend fun printRandom2() {
    delay(500L)
    throw ArithmeticException()
}

suspend fun supervisoredFunc() = supervisorScope {
    launch { printRandom1() }
    launch(ceh) { printRandom2() }
}

val ceh = CoroutineExceptionHandler { _, exception ->
    println("Something happend: $exception")
}

fun main() = runBlocking<Unit> {
    val scope = CoroutineScope(Dispatchers.IO)
    val job = scope.launch {
        supervisoredFunc()
    }
    job.join()
}
```

> 슈퍼바이저 스코프를 사용할 때 주의점은 무조건 자식 수준에서 예외를 핸들링 해야한다는 것입니다. 자식의 실패가 부모에게 전달되지 않기 때문에 자식 수준에서 예외를 처리해야합니다.