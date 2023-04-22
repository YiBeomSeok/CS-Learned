# OkHttp

OkHttp는 Android와 같은 자바 기반 플랫폼에서 HTTP 및 HTTP/2 클라이언트를 구현하기 위한 오픈소스 라이브러리.

- Square Inc.에서 개발되었으며, 안드로이드 앱에서 네트워크 요청을 처리하고 효율적으로 웹 서비스와 통신하는 데 사용된다.
- OkHttp는 빠른 성능, 안정성 및 사용 편의성을 목표로 설계되었다.

## 주요 기능

1. 강력한 연결: OkHttp는 HTTP/2 및 SPDY를 지원하여 여러 개의 요청을 동일한 `소켓`에 동시에 전송할 수 있다. 이로 인해 지연 시간이 감소하고 전체적인 네트워크 효율성이 향상된다.
2. 효율적인 요청 처리: OkHttp는 연결 재사용, 요청 우선 순위 지정, 캐시 및 GZIP 압축을 지원하여 네트워크 사용을 최적화한다. 이를 통해 데이터 사용량이 감소하고 응답 시간이 단축된다.
3. 안정적인 오류 처리: OkHttp는 네트워크 상태 변화, 연결 시간 초과 및 요청 실패와 같은 일반적인 네트워크 문제를 자동으로 처리한다. 이를 통해 안정적인 네트워크 통신을 구현할 수 있다.
4. 인터셉터: OkHttp 인터셉터를 사용하여 요청 및 응답을 가로채고 수정할 수 있다. 이를 통해 로깅, 인증 토큰 추가, 캐시 정책 변경 등의 기능을 쉽게 구현할 수 있다.

## 사용 예시

```groovy
dependencies {
    implementation 'com.squareup.okhttp3:okhttp:4.9.3'
}
```

```kotlin
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

// OkHttpClient 객체 생성
val client = OkHttpClient()

// 요청 URL
val url = "https://api.example.com/data"

// Request 객체 생성
val request = Request.Builder()
        .url(url)
        .build()

// 요청 실행 및 응답 처리
client.newCall(request).enqueue(object : okhttp3.Callback {
    override fun onFailure(call: okhttp3.Call, e: IOException) {
        e.printStackTrace()
    }

    override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
        response.use {
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            // 응답 데이터 출력
            println(response.body!!.string())
        }
    }
})
```

위 예제는 비동기 방식으로 HTTP 요청을 수행한다. `client.newCall(request).enqueue()`메서드를 호출하면 새로운 작업이 백그라운드에서 실행되고, 응답이 도착하면 `onResponse()`
메서드가 호출된다.
요청이 실패하면 `onFailure()`메서드가 호출된다.

- 주의할 점은 이 코드는 메인 스레드에서 실행되지 않으며, 네트워크 작업은 항상 백그라운드 스레드에서 실행되야 한다는 것이다.
- 안드로이드 앱에서 이코드를 사용할 때에는 백그라운드 스레드에서 실행하고, UI를 업데이트할 필요가 있을 때 메인 스레드로 전환해야 한다.
