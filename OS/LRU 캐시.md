# LRU Cache(Least Recently Used Cache)

캐시 교체 알고리즘 중 하나로, 가장 최근에 사용되지 않은 항목을 우선적으로 제거하는 방식을 사용한다. 이 방식은 캐시에 저장된 데이터를 관리할 때 효율적인 메모리 사용과 높은 캐시 히트율을 달성할 수 있는 데
도움이 된다.

## 주요 특징

1. 공간 제약: LRU 캐시는 고정된 크기의 메모리 공간을 사용하여 캐시를 관리한다. 캐시의 크기가 꽉차면, 새로운 항목을 추가하기 위해 가장 최근에 사용되지 않은 항목을 제거한다.
2. 최근 사용 기록: LRU 캐시는 각 항목의 사용 기록을 추적한다. 항목이 캐시에서 조회되거나 새로 추가될 때마다 사용 기록이 갱신된다.
3. 교체 적책: 캐시에서 제거할 항목을 결정할 때, 가장 오랫동안 사용되지 않은 항목이 제거된다. 이 방식은 자주 사용되는 항목이 캐시에 더 오래 유지되도록 하여 캐시 히트율을 높인다.

LRU 캐시는 여러 상황에서 사용되는데, 웹 페이지 캐싱, 데이터베이스 쿼리 캐싱, 이미지 로딩 라이브러리 등 다양한 분야에서 활용된다.
이러한 분야에서 LRU 캐시는 메모리를 효율적으로 사용하면서 높은 성능을 제공할 수 있는 메커니즘이 필요한 경우 적용되는 기술이다.

## 코틀린을 사용하여 구현해보자

코틀린을 사용하여 구현하기 위해 `LinkedHashMap`을 사용한다.

1. LRU 캐시를 구현하는 클래스를 생성

```kotlin
import java.util.LinkedHashMap

class LRUCache<K, V>(private val maxSize: Int) : LinkedHashMap<K, V>(maxSize + 1, 1.0f, true) {

    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean {
        return size > maxSize
    }
}
```

2. LRU 캐시 인스턴스를 생성하고 사용한다.

```kotlin
// LRU 캐시 인스턴스를 생성 (최대 크기는 5로 설정함)
val lruCache = LRUCache<String, Int>(5)

// 캐시에 데이터 추가
lruCache["one"] = 1
lruCache["two"] = 2
lruCache["three"] = 3
lruCache["four"] = 4
lruCache["five"] = 5

// 캐시에서 데이터 가져오기
val retrievedData = lruCache["two"]

// 캐시에 새로운 데이터 추가 (가장 오래된 항목인 "one"이 제거된다)
lruCache["six"] = 6
```

LinkedHashMap을 사용하여 간단한 LRU 캐시를 구현하였다.

캐시의 크기가 maxSize를 초과하면, 가장 최근에 사용되지 않은 항목이 제거되도록 `removeEldestEntry` 메서드를 오버라이드하였다.