# Hilt

Google의 Dagger를 기반으로 만든 Dependency Injection 라이브러리

Android App에 특화된 DI

- Android class에 의존성 주입을 지원하고 생명 주기를 자동으로 관리

## 사용

Project 수준의 `build.gradle`에 dependency를 추가
App 수준의 `build.gradle`에 dependency를 추가

### Application

`HiltAndroidApp`어노테이션은 어플리케이션의 기본 클래스를 비롯하여 `Hilt`코드 생성을 트리거한다.
`Application`클래스에 `Hilt`를 설정하고 `Application`수준의 컴포넌트를 사용할 수 있게 되면 `AndroidEntryPoint`라는 어노테이션으로 안드로이드 클래스에 종속 항목을 제공할 수
있다.

```kotlin
@HiltAndroidApp
class ExampleApplication : Application() { ... }
```

### Activity

`AndroidEntryPoint`어노테이션은 프로젝트의 각 안드로이드 클래스에 관하여 개별 `Hilt`컴포넌트를 생성한다.

```kotlin
@AndroidEntryPoint
class ExampleActivity : AppCompatActivity() { ... }
```

### Inject

컴포넌트에서 종속 항목을 가져오기 위해서는, `Inject`어노테이션을 사용하여 필드 삽입을 실행한다.
`Inject`어노테이션은 클래스에 인스턴스를 제공하는 방법을 `Hilt`에게 알려준다.

```kotlin
class AnalyticsAdapter @Inject constructor(
        private val service: AnalyticsService
) { ... }
```

## Android Class에 송속성 주입 범위

다음은 앞서 언급한 `AndroidEntryPoint`어노테이션이 지원하는 범위이다.

- `Application` (`@HiltAndroidApp`을 사용)
- `ViewModel` (`@HiltViewModel`을 사용)
- `Activity`
- `Fragment`
- `View`
- `Service`
- `BroadcastReceiver`

`Application`과 `ViewModel`을 제외한 나머지는 `@AndroidEntryPoint`를 사용하여 해당 클래스에 지정하면 `Hilt`컴포넌트를 생성할 수 있다.

## Android Class용으로 생성된 Component 생명주기

- 필드 삽입을 수행할 수 있는 각 `Android Class`마다 `@InstallIn`을 설정해주어야 한다.
- 이 어노테이션에는 구성요소 간 기간을 설정해서 모듈이 어느 범위까지 사용되는 지 지정해야 한다.
- 다음은 그 범위를 정리해놓은 표이다.

| 생성된 구성요소                  | 생성 위치                  | 소멸 위치                |
|---------------------------|------------------------|----------------------|
| SingletonComponent        | Application#onCreate() | Application 소멸됨      |
| ActivityRetainedComponent | Activity#onCreate()    | Activity#onDestroy() |
| ViewModelComponent        | ViewModel 생성됨          | ViewModel 소멸됨        |
| ActivityComponent         | Activity#onCreate()    | Activity#onDestory() |
| FragmentComponent         | Fragment#onAttach()    | Fragment#onDestroy() |
| ViewComponent             | View#super()           | View 소멸됨             |
| ViewWithFragmentComponent | View#super()           | View 소멸됨             |
| ServiceComponent          | Service#onCreate()     | Service#onDestroy()  |

예를 들어 `SingletonComponent`는 어플리케이션 전체 기간 동안 존재하고 사용할 수 있다.

## Component Scopes

다음은 `Component Scopes`를 나타내는 표이다.

- 기본적으로 `Hilt`의 모든 결합은 범위가 지정되지 않는다.
- 그렇기 때문에 앱이 결합을 요청할 때마다 `Hilt`는 필요한 유형의 새 인스턴스를 생성한다.
- `Hilt`는 결합을 특정 컴포넌트 범위로 지정할 수 있는데, 그 범위를 아래에 나타내었다.
- 해당 결합의 범위를 지정하게 되면 한번만 생성되고, 동일한 요청에 대해 동일한 인스턴스를 공유하게 된다.

| Android 클래스                        | 생성된 구성요소                  | 범위                      |
|------------------------------------|---------------------------|-------------------------|
| Application                        | SingletonComponent        | @Singleton              |
| Activity                           | ActivityRetainedComponent | @ActivityRetainedScoped |
| ViewModel                          | ViewModelConmponent       | @ViewModelScoped        |
| Activity                           | ActivityComponent         | @ActivityScoped         |
| Fragment                           | FragmentComponent         | @FragmentScoped         |
| View                               | ViewComnponent            | @ViewScoped             |
| @WithFragmentBindings 주석이 지정된 View | ViewWithFragmentComponent | @ViewScoped             |
| Service                            | ServiceComponent          | @ServiceScoped          |

## 그 밖의 Annotation
- `@Module` : Hilt 모듈인지 여부를 판단
- `@InstallIn` : Component 범위를 지정
- `@Binds` : Interface 삽입
- `@Provides` : Instance 삽입
- `@Qualifier` : 동일한 유형에 대해 여러 결합 제공
  - 예를 들어 TestApi와 RealApi가 있다고 할 때, 이 어노테이션을 사용하여 레트로핏 결합을 만들어 사용할 수 있다.
- `@ApplicationContext` : `Application`의 `context`를 제공하는 한정자
