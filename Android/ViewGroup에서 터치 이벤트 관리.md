# Touch

[ViewGroup에서 터치 이벤트 관리](https://developer.android.com/training/gestures/viewgroup?hl=ko)

> `ViewGroup`에서 터치 이벤트를 처리할 때는 특별히 주의해야 합니다. 왜냐하면 `ViewGroup`에는 `ViewGroup`과 다른 터치 이벤트의 타겟인 하위 요소가 있는 경우가 흔하기 때문입니다.
>
> 각 뷰가 의도대로 올바르게 터치 이벤트를 수신하도록 하려면 `onInterceptTouchEvent()` 메서드를 재정의해야 합니다.

## ViewGroup

`ViewGroup`은 Android에서 여러 개의 `View`를 그룹화하여 사용할 수 있는 레이아웃 컨테이너이다.

`ViewGroup`은 자식 `View`들의 위치와 크기를 관리하고, 레이아웃의 계층 구조를 정의하는 데 사용된다.

`ViewGroup`에서 터치 이벤트를 관리하려면, Android 터치 이벤트 처리 매커니즘을 이해해야 한다.

## 터치 이벤트

터치 이벤트는 Android에서 `MotionEvent` 객체로 표현되며, 다양한 종류의 터치 이벤트를 포함할 수 있다.

이벤트에는 `ACTION_DOWN`, `ACTION_MOVE`, `ACTION_UP`, `ACTION_CANCEL` 등이 포함된다.

`ViewGroup`에서 터치 이벤트를 관리하려면 다음 두 가지 메서드를 사용해야 한다.

1. `onInterceptTouchEvent()`: 이 메서드는 `ViewGroup`에서 호출되어 현재 터치 이벤트를 가로챌지 아닌지를 결정한다. 이 메서드에서 `true`를 반환하면, 현재 및 이후의 모든
   이벤트가
   `ViewGroup`에 전달되고, 자식 `View`에게는 전달되지 않는다. `false`를 반환한다면, 터치 이벤트가 자식 `View`로 전달된다.
2. `onTouchEvent()`: 이 메서드는 `ViewGroup` 또는 자식 `View`에서 호출되어 실제 터치 이벤트를 처리한다. 이 메서드에서 `true`를 반환하면, 터치 이벤트가 처리되었음을
   나타낸다. `false`를
   반환하면, 이벤트가 다른 `View`로 전달된다.

- false냐 true냐에 따라 계층적으로 터치 이벤트가 전달된다.

### 터치 이벤트 처리의 기본 원칙

- 먼저 가장 깊은 레이어의 자식 `View`로부터 시작하여, 해당 `View`의 `onTouchEvent()` 메서드가 호출된다.
- 만약 해당 `View`에서 이벤트를 처리하지 않으면 (`onTouchEvent()`가 `false`를 반환했을 것이다), 이벤트는 상위 `ViewGroup`으로 전달된다. 이 과정은 root `ViewGroup`
  까지 반복된다. (
  아마도 이러한 과정을 포함한 이유 등으로 레이아웃을 flat하게 구성하는 것이 좋은 것 같다..)
- 상위 `ViewGroup`에서도 이벤트를 처리하지 않으면, 이벤트는 무시된다.

`ViewGroup`에서 터치 이벤트를 처리하는 방법을 사용자 정의하려면, `onInterceptTouchEvent()` 및 `onTouchEvent()`메서드를 오버라이드하여 원하는 동작을 구현해야 할 것이다.
이렇게
하면 `ViewGroup`에서 제스처 인식기, 드래그 앤 드롭 등 다양한 터치 상호작용을 구현할 수 있다.

### 간단 정리

- Activity -> ViewGroup -> View 순서로 터치 전달
- View -> ViewGroup -> Activity로 터치 처리
- 단, onInterceptTouchEvent()를 true로 return 시 터치가 가로채어지고 더 아래로 터치가 내려가지 않음.
- onTouchEvent()를 true로 return 시 터치 이벤트가 처리되고, 더 위로(root쪽으로) 터치 이벤트가 흘러가지 않음.