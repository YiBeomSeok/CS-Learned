# Retrofit
- OkHttp를 네트워크 레이어로 활용하는 상위호환 라이브러리
- Retrofit은 RESTful 웹 서비스와 통신하기 위한 오픈소스 HTTP 클라이언트 라이브러리이다.

## 주요 특징

1. 타입 안전(Type Safe): Retrofit은 런타임에 발생할 수 있는 오류를 최소화하기 위해 타입 안전을 지원한다. 이를 통해 애플리케이션에서 정의한 데이터 타입에 대한 명확한 유효성 검사와 변환을 수행할
   수 있다.
2. 선언적 API 정의: Retrofit은 간편하게 웹 서비스 API를 인터페이스로 선언할 수 있게 해준다. 이 인터페이스에 어노테이션을 활용하여 HTTP 메서드, 엔드포인트, 쿼리 및 경로 파라미터, 헤더 등을
   지정할 수 있다.
3. 커스텀 변환기 지원: Retrofit은 Gson, Jackson, Moshi 등 다양한 JSON 변환 라이브러리를 지원한다. 또한 커스텀 변환기를 사용하여 API 응답을 원하는 형식으로 처리할 수도 있다.
4. 동기 및 비동기 호출: Retrofit은 동기 및 비동기 요청을 모두 지원하므로 애플리케이션의 작업 흐름에 맞게 선택할 수 있다. 비동기 호출의 경우 코루틴이나 콜백을 사용할 수 있다.
5. 인터셉터 지원: Retrofit은 인터셉터를 통해 요청과 응답을 수정하거나 가로챌 수 있다. 이를 사용하여 헤더를 추가하거나 로깅, 인증 등의 작업을 수행할 수 있다.

## 예제

1. 먼저 API 인터페이스를 생성하고 어노테이션으로 엔드포인트와 HTTP 메서드를 정의한다.

```kotlin
interface ApiService {
    @GET("users/{userId}")
    suspend fun getUser(
            @Path("userId") userId: Int
    ): User
}
```

2. Retrofit 인스턴스를 생성하고 필요한 변환기와 기본 URL을 설정한다.

```kotlin
val retrofit = Retrofit.Uilber()
        .baseUrl("https://api.example.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
```

3. 생성한 Retrofit 인스턴스를 사용하여 API 인터페이스의 구현체를 생성한다.

```kotlin
val userId = 1
val user = apuService.getUser(userId)
```

Retrofit은 간편한 API 선언, 타입 안전성, 성능 최적화 등의 장점을 제공한다. 이를 통해 애플리케이션의 개발 및 유지 보수가 훨씬 용이해질 수 있다.
