# Bitmap Pool

* 비트맵 풀은 Android에서 이미지 처리를 최적화하기 위해 사용되는 메모리 관리 기술이다. 비트맵 풀은 이미 사용되었던 비트맵 객체를 저장하고, 필요할 때 재사용할 수 있는 메모리
  공간을 제공한다.
* 비트맵 풀의 주요 목적은 메모리 할당 및 가비지 컬렉션에 따른 성능 저하를 최소화하는 것이다. 이미지 처리 작업에서 비트맵 객체를 생성하고 해제하는 과정은 메모리를 반복적으로 할당하고 해제해야 하므로, 가비지
  컬렉션을 유발하게 되고 이는 오버헤드가 되므로 성능 저하를 일으킬 것이다.

## 비트맵 풀을 사용한 이점

1. 메모리 최적화: 이미 사용된 비트맵 객체를 재사용함으로써 메모리 할당 및 해제 작업을 줄일 수 있다. 이로 인해 메모리 사용량을 최적화하고, 가비지 컬렉션으로 인한 성능 저하를 최소화할 수 있다.
2. 빠른 이미지 처리: 비트맵 풀에 저장된 비트맵 객체를 재사용하면 새로운 비트맵 객체를 생성하는 시간을 절약할 수 있다. 이로 인해 이미지 처리 속도가 향상되고 전체 애플리케이션의 성능이 개선된다.
3. 안정성 향상: 비트맵 풀을 사용하여 메모리를 효율적으로 관리하면, 메모리 부족 문제를 예방하고 애플리케이션의 안정성을 높일 수 있다.

## 코드로는 어떻게 사용될까?

LRU 캐시를 사용하여 간단한 비트맵 풀을 구현하고, 비트맵을 재사용할 수 있는 방법이 있다.

1. 비트맵 풀을 구현하기 위해 [LRU 캐시](..%2F..%2FOS%2FLRU%20%EC%BA%90%EC%8B%9C.md)를 사용한다.

```java
import android.util.LruCache;
import android.graphics.Bitmap;

public class BitmapPool {
    private LruCache<String, Bitmap> mBitmapCache;

    public BitmapPool(int maxSize) {
        mBitmapCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public Bitmap getBitmap(String key) {
        return mBitmapCache.get(key);
    }

    public void putBitmap(String key, Bitmap bitmap) {
        mBitmapCache.put(key, bitmap)
    }
}
```

2. 비트맵 풀의 인스턴스를 생성하고 사용한다.

```java
// 비트맵 풀 인스턴스 생성 (최대 크기는 10MB로 설정함)
BitmapPool bitmapPool = new BitmapPool(10*1024*1024);

// 비트맵을 생성하고 키를 사용하여 비트맵 풀에 추가한다
Bitmap sampleBitmap = BitmapFactory.decodeResource(getResouces(),R.drawable.sampe_image);
bitmapPool.putBitmap("sample_image",sampleBitmap);

// 필요한 경우 키를 사용하여 비트맵을 비트맵 풀에서 가져온다
Bitmap retrievedBitmap = bitmapPool.getBitmap("sample_image");
```

물론 실제 애플리케이션에서는 Glide와 같은 외부 라이브러리를 사용하여 비트맵 풀을 관리하는 것이 더 효율적이다. 비트맵 풀의 기본 개념을 이해하고 간단한 구현 방법을 확인하고자 작성한다.
