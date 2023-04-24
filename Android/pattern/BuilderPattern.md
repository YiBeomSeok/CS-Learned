# BuilderPattern

Builder 패턴은 객체 생성 과정에서 객체의 복잡성을 분리하고, 객체 생성 과정을 단계별로 나누어 처리하는 디자인 패턴이다.

- 이 패턴은 생성자를 이용하여 객체를 생성할 때 인자의 수가 많거나, 인자들 사이의 관계가 복잡한 경우 유용하게 사용된다.

## 목적

1. 객체 생성 과정의 복잡성을 분리.
2. 객체의 불변성을 보장.
3. 가독성 있는 코드를 작성.
4. 생성자의 매개변수 중 일부를 선택적으로 설정.

## 구성요소

1. Builder: 객체를 생성하기 위한 인터페이스. 객체 생성에 필요한 메서드를 선언한다.
2. ConcreteBuilder: Builder 인터페이스를 구현하는 클래스. 구체적인 객체 생성 과정을 담당하며, 완성된 객체를 반환한다.
3. Director: Builder 인터페이스를 사용하여 객체 생성 과정을 조절한다.
4. Product: Builder를 통해 생성되는 최종 객체이다.

## 예시

예를 들어, 음식점의 메뉴 주문 시스템을 Builder 패턴을 사용하여 구현해볼 수 있다.

```kotlin
// Product
data class Meal(val mainDish: String, val sideDish: String?, val drink: String?)

// Builder
interface MealBuilder {
    fun setMainDish(mainDish: String): MealBuilder
    fun setSideDish(sideDish: String): MealBuilder
    fun setDrink(drink: String): MealBuilder
    fun build(): Meal
}

// ConcreteBuilder
class MealBuilderImpl : MealBuilder {
    private lateinit var mainDish: String
    private var sideDish: String? = null
    private var drink: String? = null

    override fun setMainDish(mainDish: String): MealBuilder {
        this.mainDish = mainDish
        return this
    }

    override fun setSideDish(sideDish: String): MealBuilder {
        this.sideDish = sideDish
        return this
    }

    override fun setDrink(drink: String): MealBuilder {
        this.drink = drink
        return this
    }

    override fun build(): Meal {
        return Meal(mainDish, sideDish, drink)
    }
}

fun main() {
    val mealBuilder = MealBuilderImpl()
    val meal = mealBuilder.setMainDish("Burger")
            .setSideDish("Fries")
            .setDrink("Coke")
            .build()

    println(meal)
}
```

음식점의 메뉴를 주문하는 과정에서, 주문자가 선택적으로 사이드 메뉴와 음료를 설정할 수 있게 하여, 주문 메뉴를 유연하게 구성할 수 있다.
이처럼 Builder 패턴은 객체 생성 과정을 유연하게 구성하고 코드의 가독성을 높이는 데 도움이 된다. 또한 객체 생성 과정에서의 복잡성을 분리함으로써 코드의 유지보수와 확장성이 향상된다.

Builder 패턴은 객체 생성 과정의 복잡성 때문에 사용되기도 하지만, 다양한 선택 사항들을 조합하여 객체를 생성하는 경우에도 유용하다. 이런 경우, 클라이언트는 필요한 선택 사항만 설정하고 나머지는 기본값이나
선택하지 않은 상태로 둘 수 있다. 이렇게 하면 객체 생성 과정이 단순해지며, 클라이언트 코드가 더 간결해진다.

그러나 Builder 패턴을 사용하는 경우에도 단점이 존재하는데, 예를 들어 별도의 Builder 클래스를 생성해야 하므로 클래스의 수가 늘어나고 코드 복잡성이 증가할 수 있다는 것이다. 또한 객체 생성 시간이
증가할 수 있다.

결과적으로 Builder 패턴은 객체 생성 과정에서의 복잡성이나 선택 사항이 많ㅇ느 경우 코드 가독성과 유지보수성을 높이기 위해 사용된다. 필요에 따라 이 패턴을 사용하여 객체 생성 과정을 최적화하고, 사용자에게 더
유연한 객체 생성 방법을 제공할 수 있는 것이다.

### 이 패턴이 사용되는 라이브러리들 몇가지

1. **OkHttp**: OkHttp의 `Request.Builder` 클래스는 웹 요청을 구성하기 위해 Builder 패턴을 사용한다. 이를 통해 개발자들은 필요한 옵션을 선택적으로 설정하여 웹 요청 객체를
   생성할 수 있다.
2. **Retrofit**: Retrofit은 REST API 통신 라이브러리이다. Retrofit에서는 `Retrofit.Builder` 클래스를 사용하여 객체를 구성하고 생성한다. 이를 통해 개발자들은 서버
   URL, 변환기, 인터셉터 등을 선택적으로 설정할 수 있다.
3. **Glide**: Glide는 안드로이드에서 이미지 로딩 및 캐싱을 처리하는 라이브러리이다. Glide에서는 `RequestOptions` 클래스를 사용하여 이미지 처리 옵션을 구성한다. 이를 통해 개발자들은
   이미지 크기, 비율, 캐싱 전략 등을 선택적으로 설정할 수 있다.

```kotlin
Glide.with(context)
        .load(imageUrl)
        .pply(RequestOptions().centerCrop().placeholder(R.drawable.placeholder))
        .into(imageView)
```