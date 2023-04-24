# ProGuard

## 난독화(Obfuscation)

난독화란 소프트웨어의 소스 코드를 외부에서 분석하기 어렵게 만드는 과정이다.
안드로이드 개발에서 난독화는 APK 파일의 크기를 줄이고, 리버스 엔지니어링을 어렵게 만들어 소스 코드의 보안을 강화하는 역할을 한다.
코틀린은 안드로이드 앱 개발에 사용되는 프로그래밍 언어이므로 안드로이드 코틀린에서도 난독화가 적용된다.

## ProGuard란
안드로이드 스튜디오는 기본적으로 ProGuard라는 난독화 도구를 사용하여 난독화를 수행한다.
- ProGuard는 클래스, 메서드, 변수의 이름을 의미없는 문자열로 변경하고, 불필요한 코드를 제거하여 APK파일의 크기를 줄이고 리버스 엔지니어링을 어렵게한다.
- 자바와 안드로이드 애플리케이션을 위한 오픈소스 난독화 도구이다.
- 코드 최적화, 불필요한 코드 제거(Shrinking), 난독화(Obfuscation), 최적화(Optimization) 그리고 런타임 라이브러리 감지(Preverification)와 같은 작업을 수행한다.

### 주요 목적
1. 소스 코드 보호: ProGuard는 클래스, 메서드, 변수의 이름을 가독성이 없는 문자열로 바꾸어 리버스 엔지니어링을 어렵게 만든다.
2. 애플리케이션 크기 축소: 불필요한 코드, 리소스 및 메타데이터를 제거하여 애플리케이션의 크기를 줄인다.
    - 이로 인해 다운로드 시간과 설치 공간이 절약되며, 애플리케이션의 성능도 향상된다.
3. 성능 최적화: ProGuard는 코드 최적화를 통해 애플리케이션의 실행 속도를 향상시키고, 메모리 사용량을 줄인다.

### 사용
`build.gradle` 파일에 다음과 같은 설정을 추가해야 한다.
```groovy
android {
    ...
    buildTypes {
        release {
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

이 설정은 릴리즈 빌드 시 ProGuard를 활성화하고, 기본 ProGuard 설정 파일(`proguard-android-optimize.txt`)과 사용자 정의 ProGuard 설정 파일(`proguard-rules.pro`)을 사용하도록 지시한다.

ProGuard 설정 파일에서는 난독화와 최적화에 대한 규칙을 정의할 수 있다. 예를 들어, 특정 클래스나 패키지의 난독화를 유지하거나 제외하려면 다음과 같이 설정할 수 있다.

```kotlin
- keep public class com.example.MyClass
- keep public class com.example.mypackage.** { *; }
```

ProGuard는 매우 강력한 도구이지만, 난독화 과정에서 애플리케이션의 정상 작동을 방해할 수 있는 경우가 있다.
이를 해결하기 위해 적절한 ProGuard 설정 파일과 규칙을 사용하는 것이 중요하다.