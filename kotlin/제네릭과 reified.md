# 코틀린 제네릭

제네릭은 타입 안정성(type safety)을 보장하면서 재사용 가능한 코드를 작성할 수 있도록 도와준다.

코틀린에서 제네릭을 사용하려면 클래스나 함수의 선언 시점에 타입 매개변수(type parameter)를 지정해야 한다. 이러한 타입 매개변수는 일반적으로 대문자 알파벳으로 표시된다.

예를 들어, List 클래스를 만들 때 타입 매개변수를 사용하여 다양한 타입의 요소를 저장할 수 있도록 만들 수 있다.

```kotlin
class List<T> {
    fun add(item: T) { /* ... */
    }
    fun get(index: Int): T { /* ... */
    }
}
```

이렇게 선언된 List 클래스는 다음과 같이 사용할 수 있다.

```kotlin
val intList = List<Int>()
intList.add(1)
intList.add(2)
val firstItem = intList.get(0)

val stringList = List<String>()
stringList.add("Hello")
stringList.add("World")
val secondItem = stringList.get(1)
```

이렇게 타입 매개변수를 사용하면, 코드의 중복을 줄이고, 재사용성을 높이며, 안정성을 보장할 수 있다.
또한, 코틀린에서는 제네릭을 통해 컬렉션과 같은 자료구조를 사용할 수 있다.

## Type Erasure(타입 소거)

Type Erasure는 제네릭을 사용하는 언어에서 컴파일 타임에 타입 정보를 제거하는 프로세스를 말한다.

제네릭을 사용하면 컴파일러는 코드에서 사용되는 타입 정보를 검사하고, 이를 바탕으로 타입 안정성을 보장한다.
그러나, 컴파일러가 생성한 클래스 파일에는 타입 정보가 포함되지 않으며, 대신 제네릭 타입의 변수나 매개변수는 Object로 변환된다.

**이를 타입 소거라고 하는 것이다.**

예를 들어, 다음과 같은 제네릭 클래스가 있다고 하자.

```kotlin
class MyClass<T>(val value: T) {
    fun printValue() {
        println(value)
    }
}
```

이 클래스를 사용하는 코드에서는 타입 매개변수를 지정하여 객체를 생성할 수 있다.

```kotlin
val intClass = MyClass<Int>(1)
val stringClass = MyClass<String>("Hello")

intClass.printValue() // 출력 결과: 1
stringClass.printValue() // 출력 결과: Hello
```

하지만 컴파일러는 실제론 다음과 같이 타입 소거를 수행한다.

```kotlin
class MyClass(val value: Any) {
    fun printValue() {
        println(value)
    }
}
```

즉, 컴파일러는 클래스를 생성할 때, 타입 매개변수를 `Any`로 대체하고, 메서드나 변수에서는 `Object`로 취급한다.
이를 통해, 제네릭이 지원되지 않는 런타임 환경에서도 코드가 동작할 수 있다.

타입 소거의 결과로, 제네릭 타입 정보는 런타임에 사용할 수 없게 되지만, 제네릭 타입의 메서드를 호출할 때는 타입 캐스팅을 통해 타입 안정성을 보장할 수 있다.

## reified ?

코틀린에서 `reified`는 제네릭을 사용할 때 타입 소거(Type Erasure)로 인해 타입 정보가 유실되는 문제를 해결하기 위한 기능이다.

일반적으로 제네릭을 사용하는 함수나 클래스에서는 타입 매개변수의 실제 타입 정보를 런타임에는 알 수 없다.

그렇기 때문에 타입 매개변수로 전달된 인스턴스의 타입 정보를 사용할 수 없다. 이 때 `reified` 키워드를 사용하면, 컴파일러는 타입 매개변수의 실제 타입 정보를 유지하도록 한다.

예를 들어, 다음과 같은 제네릭 함수가 있다고 가정하자.

```kotlin
fun <T> printType(item: T) {
    println(item::class.simpleName)
}
```

이 함수는 실행 시점에서 `item`의 실제 타입 정보를 알 수 없다. 그렇기 때문에 `item::class`를 사용하여 타입 정보를 가져올 수 없다.
하지만 이 때, `reified`를 사용하여 다음과 같이 함수를 변경하면, 컴파일러는 타입 매개변수의 실제 타입 정보를 유지하도록 한다.

```kotlin
inline fun <reified T> printType(item: T) {
    println(T::class.simpleName)
}
```

이제, `printType` 함수를 호출할 때, `reified` 키워드를 사용하면, 타입 매개변수의 실제 타입 정보를 가져올 수 있다.

```kotlin
printType(1) // Int
printType("Hello") // String
```

즉, `reified` 키워드를 사용하면, 타입 매개변수의 실제 타입 정보를 런타임에도 유지할 수 있어, 제네릭을 보다 유연하게 사용할 수 있다.
단, `reified` 키워드는 inline 함수에서만 사용할 수 있다.