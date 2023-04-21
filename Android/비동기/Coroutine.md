# ✍️ Coroutine

비동적으로 실행되는 코드를 간소화하기 위해 Android에서 사용할 수 있는 동시 실행 설계 패턴

Kotlin 1.3에서 추가되었으며 일종의 경량 스레드

- "co"와 "routine"의 합성어로, 협동 루틴을 의미한다.
- 코루틴은 서로 협력하는 여러 루틴이 독립적인 실행을 중지하고 재개할 수 있는 방식으로 동시성을 지원하는 프로그래밍 기법이다.
- 코루틴은 프로그램의 실행 흐름을 중단할 수 있는 지점을 만들어 여러 작업을 동시에 진행하도록 한다.
- 각 코루틴은 다른 코루틴이 실행되도록 양보할 수 있으며, 이를 통해 서로 협력하면서 동시성을 처리한다.

### 사용 방법

```groovy
dependencies {
    implementation 'org.jetbrains.kotlinx:kolinx-coroutines-android:1.3.9'
}
```

## 코루틴의 구성 요소

- CoroutineContext: 코루틴이 실행될 Context
    - 코루틴의 실행 목적에 맞게 실행될 수 있는 특정 스레드풀을 지정해준다.
- CoroutineScope: 코루틴을 제어할 수 있는 범위
    - 제어는 어떤 작업을 취소하거나 끝날 때까지 기다리는 것을 뜻한다.
- Builder: 코루틴을 실행하는 함수
    - 종류로는 Launch, async 등이 있다.

### CoroutineContext

코루틴 처리를 어떻게 할 것인지에 대한 요소들의 집합.
코루틴은 여러 함수를 번갈아 가며 동작할 수 있고 코루틴이 실행되는 스레드를 지정할 수 있다.

#### 스레드를 지정해야 하는 이유

실행하는 동작에 따라 빠르게 처리해야 하는 것들이 있고, IO작업처럼 오래 걸리는 동작이 있을 수 있다.
예를 들어 안드로이드의 UI 작업은 메인 스레드에서만 수행되어야 하기 때문에 메인 스레드에서 코루틴이 실행되어야 한다.

#### CoroutineContext 요소

**Dispatcher**: 코루틴을 처리할 스레드를 Setting 하고 할당하는 역할

| 요소         | 스레드                                                               |
|------------|-------------------------------------------------------------------|
| Main       | 메인 스레드                                                            |
| IO         | I/O 작업을 하는데 최적화된 스레드                                              |
| Default    | CPU 사용양이 많은 작업을 할 때 실행 (크기가 큰 리스트를 다루거나 필터링을 하는 등의 무거운 연산이 필요할 때) |
| Unconfined | 다른 Dispatchers와 달리 특정 스레드를 지정하지 않음 (일반적으로 사용되지 않고, 권장되지 않는다)      |

**Job**: 생성된 코루틴을 컨트롤. (생명 주기, 부모 자식 관계 정리 및 관리 -> 마치 포크함수처럼..)

### CoroutineScope

코루틴 처리를 어떻게 할 것인지에 대한 요소들의 집합
코루틴이 실행되는 범위를 뜻한다.

| 종류             | 범위                                                                                                           |
|----------------|--------------------------------------------------------------------------------------------------------------|
| GlobalScope    | Application lifecycle (어플리케이션이 살아있는 동안.. 만일 엑티비티에서 실행시켰다면, 앱이 종료되지 않는 이상 계속 작업이 가능하다 -> 리소스 낭비가 될 수 있으므로 주의) |
| CoroutineScope | Scope 안의 작업이 끝날 때까지                                                                                          |
| ViewModelScope | ViewModel lifecycle                                                                                          |

### Builder

설정해준 컨텍스트와 스코프를 통해 코루틴을 실행할 수 있게 해 준다.
코루틴을 생성하는 메소드.

| 종류          | 설명                                                                                                                                                          |
|-------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------|
| launch      | 코루틴을 즉시 실행시키고, Job()을 반환. 결과는 Return하지 않음(실행 후 망각) -> suspend 함수가 아닌 일반 함수 안에서 suspend function을 호출할 때와 코루틴의 결과가 필요 없을 때 사용하면 좋다.                           |
| async       | 현재 스레드 중단 없이 코루틴을 즉시 시작시키고 Deferred<T> 객체를 리턴. suspend function 내부에서만 사용 가능. 호출한 쪽에서 await()로 결과를 기다릴 수 있다(Return) -> 병행으로 실행될 필요가 있는 다수의 코루틴을 사용할 때 사용하면 좋다. |
| withContext | 부모 코루틴에 의해서 사용되는 컨텍스트와 다른 컨텍스트에서 코루틴을 실행시킬 수 있게 한다. Context를 변환. await() 없이 결과를 Return -> 코루틴에서 결과를 반환할 때, async 대신 유용하게 사용할 수 있다.                           |
| runBlocking | 결과를 반환 할 때까지 현재 스레드를 중단. 그렇기 때문에 만일 Main Thread에서 잘 못 사용하게 되면 ANR이 발생할 수 있다. (코루틴의 취지와는 정반대이긴 하나, 테스트 코드를 작성하거나 레거시 코드나 라이브러리 통합시에는 유용할 수 있다.               |

### suspend function

suspend 키워드가 붙은 코루틴 안에서 실행시키려는 함수
코루틴 안에서 실행되는 함수는 suspend 키워드를 붙여야 한다.

## 스레드(thread)와의 차이

코루틴과 스레드는 프로그램에서 병렬 또는 동시 처리를 가능하게 하는 방법이지만, 작동 방식과 특성에 몇 가지 차이점이 있다.

스레드는 운영 체제가 관리하는 경량 프로세스로, 프로세스 내부에서 독립적인 실행 흐름을 가진다.
각 스레드는 자체 스택과 레지스터를 가지며, 스레드 간에 프로세스의 메모리를 공유할 수 있다.

### 스레드의 주요 특징

- 멀티 스레드 프로그래밍은 운영 체제가 스케줄링하므로 개발자가 직접 제어할 수 없다.
- 스레드간의 데이터 공유가 쉽지만, 동시성 문제로 인해 동기화와 관련된 복잡한 문제가 발생할 수 있다.
- 스레드 생성, 종료 및 관리에 상대적으로 높은 오버헤드가 있다.

### 스레드의 오버헤드?

스레드의 상대적으로 높은 오버헤드는 스레드 생성, 종료 및 컨텍스트 스위칭과 관련된 작업 때문이다.

- 스레드는 운영 체제의 리소스를 사용한다. 스레드 생성 시, 운영 체제는 스레드에 필요한 자원(스택, 레지스터 등)을 할당해야 하며, 스레드 종료 시에는 이러한 자원을 해제해야 한다. 이 과정은 상대적으로 높은
  오버헤드를 발생시킨다.
- 스레드의 컨텍스트 스위칭은 운영 체제가 관리하므로, 실행 중인 스레드의 상태를 저장하고, 대기 중인 다른 스레드의 상태를 복원하는 작업이 필요하다. 이 작업은 CPU 레지스터, 스택 메모리 등의 변경을 포함하며,
  오버헤드를 발생시킨다.

반면, 코루틴은 다음과 같은 이유로 낮은 오버헤드를 가진다.

- 코루틴은 스레드와 달리 운영 체제의 리소스를 직접 사용하지 않는다. 대신, 이미 생성된 스레드 내에서 실행되며, 스레드에 할당된 리소스를 공유한다. 따라서 코루틴의 생성 및 종료에 대한 오버헤드가 상대적으로
  낮다.
- 코루틴의 컨텍스트 스위칭은 개발자가 직접 제어할 수 있으며, 필요한 경우에만 수행된다. 또한 코루틴 간의 컨텍스트 스위칭은 스레드와 달리 운영 체제의 개입이 없어, CPU 레지스터, 스택 메모리 등의 변경이 덜
  복잡하고 빠르다. 이로 인해 코루틴의 컨텍스트 스위칭에 소요되는 시간이 적다.

#### 스레드를 사용할 경우

```kotlin
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val executorService = Executors.newFixedThreadPool(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startTime = System.currentTimeMillis()

        val threads = mutableListOf<Thread>()

        for (i in 0 until 10) {
            val thread = thread {
                Log.d("ThreadExample", "스레드 $i 시작")
                Thread.sleep(1000)
                Log.d("ThreadExample", "스레드 $i 종료")
            }
            threads.add(thread)
        }

        threads.forEach { it.join() }

        Log.d("ThreadExample", "실행 시간: ${System.currentTimeMillis() - startTime}")
    }
}
```

위의 코드에서는 10개의 스레드를 생성하여 각각의 스레드에서 1초 동안 일시 정지한 후 작업을 완료한다.
여기서 각 스레드의 생성, 종료 및 컨텍스트 스위칭에 소요되는 시간이 오버헤드로 작용한다.

### 코루틴의 주요 특징

코루틴은 동시성을 지원하는 하나의 프로그램 실행 흐름에서 여러 개의 실행 단위를 독입적으로 관리할 수 있는 기능이다.
코루틴은 함수가 일시 중지된 지점에서 실행을 중지하고, 필요한 경우 다시 해당 지점에서 실행을 재개할 수 있도록 한다.

- 코루틴은 개발자가 직접 스케줄링을 제어할 수 있다.
- 코루틴은 비동기 작업이나 I/O 작업과 같이 복잡한 로직을 쉽게 구현할 수 있도록 돕는다.
- 코루틴 생성, 종료 및 관리에 상대적으로 낮은 오버헤드가 있다.

#### 코루틴을 사용할 경우

```kotlin
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startTime = System.currentTimeMillis()

        val coroutineScope = CoroutineScope(Dispatchers.Default)

        runBlocking {
            for (i in 0 until 10) {
                coroutineScope.launch {
                    Log.d("CoroutineExample", "코루틴 $i 시작")
                    kotlinx.coroutines.delay(1000)
                    Log.d("CoroutineExample", "코루틴 $i 종료")
                }
            }
        }

        Log.d("CoroutineExample", "실행 시간: ${System.currentTimeMillis() - startTime}")
    }
}
```

위의 예제에서는 10개의 코루틴을 생성하여 각 코루틴에서 1초 동안 일시 정지한 후 작업을 완료한다.
코루틴은 스레드에 비해 생성, 종료 및 컨텍스트 스위칭에 소요되는 시간이 적어 오버헤드가 낮다.

### 요약

스레드는 운영 체제가 관리하고 멀티태스킹을 위한 병렬 처리를 지원한다. 코루틴은 개발자가 제어하는 동시성을 지원하는 비동기 처리 방식이다.
스레드는 복잡한 동기화 문제와 높은 오버헤드를 가지지만, 코루틴은 낮은 오버헤드와 간편한 작업 관리를 제공한다.
이를 통해 코루틴을 사용한 동시성 프로그래밍이 더 효율적이고 가볍게 구현될 수 있다.