# AppBarLayout

- AppBarLayout은 Android 앱에서 상단 앱 바의 동작과 스크롤 이벤트를 관리하는데 사용되는 수직 LinearLayout이다.
- AppBarLayout은 Material Design 가이드라인을 따르며, 앱 내의 주요 액션과 탐색을 사용자에게 표시하는데 도움이 된다.
- 주로 Toolbar, TabLayout, 또는 CollapsingToolbarLayout와 함께 사용되어 풍부한 사용자 경험을 제공한다.

## AppBarLayout의 주요 특징

1. 스크롤 동작: AppBarLayout과 함께 CoordinatorLayout을 사용하면, 스크롤과 함께 앱 바를 축소하거나 확장할 수 있다. 이를 통해 앱의 주요 콘텐츠에 초점을 맞추면서도, 필요한 경우 앱
   바에 액세스할 수 있다.
2. 연계된 스크롤: AppBarLayout은 다른 뷰와 연계된 스크롤 동작을 제공한다. 예를 들어, 앱 바가 축소되거나 확장될 때 RecyclerView 또는 NestedScrollView의 콘텐츠도 함께
   스크롤되도록 설정할 수 있다.
3. 플렉서블한 구성: AppBarLayout은 서로 다른 뷰를 포함하고 정렬할 수 있다. Toolbar, TabLayout 및 CollapsingToolBarLayout과 같은 여러 가지 컴포넌트를 조합하여 사용자
   경험을 개선할 수 있다.