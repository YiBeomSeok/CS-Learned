import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.junit.Test

// 여러개의 플로우가 있을 수 있을 때 어떻게 결합할 것인가?

class FlowCombination {

    /**
     * zip으로 플로우를 묶을 때에는 두 데이터가 모두 준비된 이후에 동작한다.
     */
    @Test
    fun `zip으로 묶기`() = runBlocking<Unit> {
        val nums = (1..3).asFlow()
        val strs = flowOf("일", "이", "삼")
        nums.zip(strs) { a, b -> "${a}은(는) $b" } // 1은(는) 일
                .collect { println(it) }
    }

    /**
     * combine으로 묶을 경우, 양쪽의 데이터를 같은 시점에 묶지 않고 한 쪽이 갱신되면 새로 묶어 데이터를 만든다.
     */
    @Test
    fun `combine으로 묶기`() = runBlocking<Unit> {
        val nums = (1..3).asFlow().onEach { delay(100L) }
        val strs = flowOf("일", "이", "삼").onEach { delay(200L) }
        nums.combine(strs) { a, b -> "${a}은(는) $b" }
                .collect { println(it) }
    }
    // 숫자와 문자가 짝이 맞지 않으므로 적합한 코드는 아닐 것이다.
    // 짝을 이룰 필요 없이 최신의 데이터를 이용해 가공해야 하는 경우 사용할 수 있다.
    // ex) UI 화면에 차지하는 여러 데이터가 있는데 각 데이터들의 업데이트에 따라 갱신하고 싶을 경우
}