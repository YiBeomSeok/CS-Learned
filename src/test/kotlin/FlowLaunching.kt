import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import org.junit.Test

// 플로우를 어떻게 런칭할 수 있는가?
class FlowLaunching {
    private fun log(msg: String) = println("${Thread.currentThread().name}: $msg")
    private fun events() = (1..3).asFlow().onEach { delay(100) }
    /**
     * addEventListener { event -> handle(event) }
     */


    /**
     * GUI 어플리케이션의 addEventListener(어떤 이벤트들에 대해 콜백에서 처리하는 형태) 대신 onEach를 사용할 수 있다.
     * 이벤트마다 onEach가 대응하는 것이다.
     *
     * - 그러나 `events()`는 collect가 호출된 이후에만 동작한다.(콜드 스트림)
     * - collect()는 스트림이 끝날 때까지 기다리게 된다.
     */
    @Test
    fun `이벤트를 Flow로 처리하기`() = runBlocking<Unit> {
        events()
                .onEach { event -> println("Event: $event") }
                .collect() // 스트림이 끝날 때까지 기다리게 된다. 이벤트는 계속 발생한다. -> 이벤트를 감시하는 것 때문에 코드 진행이 멈추게 된다!!!
        // UI 작업, 네트워크 호출 등의 작업이 여기에 있을 경우 collect가 끝나지 않을 경우 동작할 수 없게된다!!
        println("Done")
    }

    // 위의 문제를 해결하기 위해 launchIn을 이용한다.
    @Test
    fun `launchIn을 사용하여 런칭하기`() = runBlocking<Unit> { // this: 코루틴 스코프
        events()
                .onEach { event -> log("Event: $event") }
                .launchIn(this) // 코루틴 스코프. // 새로운 코루틴을 만들어내서 onEach를 수행 // 별개의 코루틴에서 이벤트 처리
        println("Done")
        // UI 작업
        // 네트워크 호출
    }
}