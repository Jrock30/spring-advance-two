# Spring Advanced Proxy, Decorator Pattern, Proxy  Factory
- LogTrace
- Template Method Pattern
- Strategy Pattern
- Template Callback Pattern
## 요구사항
- 원본 코드를 전혀 수정하지 않고, 로그 추적기를 적용해라. 특정 메서드는 로그를 출력하지 않는 기능
- 보안상 일부는 로그를 출력하면 안된다.
- 다음과 같은 다양한 케이스에 적용할 수 있어야 한다.
- v1 인터페이스가 있는 구현 클래스에 적용 
- v2 인터페이스가 없는 구체 클래스에 적용 
- v3 컴포넌트 스캔 대상에 기능 적용
> 가장 어려문 문제는 원본 코드를 전혀 수정하지 않고, 로그 추적기를 도입하는 것이다. 이 문제를 해결하려면 프록시(Proxy)의 개념을 먼저 이해해야 한다.


> v1 - 인터페이스와 구현 클래스 - 스프링 빈으로 수동 등록   
> v2 - 인터페이스 없는 구체 클래스 - 스프링 빈으로 수동 등록  
> v3 - 컴포넌트 스캔으로 스프링 빈 자동 등록
---
## Proxy
- 대체가능
> 객체에서 프록시가 되려면, 클라이언트는 서버에게 요청을 한 것인지, 프록시에게 요청을 한 것인지 조차 몰라야 한다.   
> 서버와 프록시는 같은 인터페이스를 사용해야 한다. 그리고 클라이언트가 사용하는 서버 객체를 프록시 객체로 변경해도 클라이언트 코드를 변경하지 않고 동작할 수 있어야 한다.  
> 런타임(애플리케이션 실행 시점)에 클라이언트 객체에 DI를 사용해서 Client -> Server 에서 Client -> Proxy 로 객체 의존관계를 변경해도 클라이언트 코드를 전혀 변경하지 않아도 된다.   
> 클라이언트 입장에서는 변경 사실 조차 모른다. DI를 사용하면 클라이언트 코드의 변경 없이 유연하게 프록시를 주입할 수 있다.

- Proxy 주요기능
  - 접근 제어
    - 권한에 따른 접근 차단 캐싱
    - 지연 로딩
  - 부가 기능 추가
    - 원래 서버가 제공하는 기능에 더해서 부가 기능을 수행한다. 
    - ex) 요청 값이나, 응답 값을 중간에 변형한다.
    - ex) 실행 시간을 측정해서 추가 로그를 남긴다.

- GOF 디자인패턴
  - GOF 디자인 패턴에서는 이 둘을 의도(intent)에 따라서 프록시 패턴과 데코레이터 패턴으로 구분한다.
  - <b>프록시 패턴: 접근 제어가 목적
  - <b>데코레이터 패턴: 새로운 기능 추가가 목적

- 참고
> 프록시라는 개념은 클라이언트 서버라는 큰 개념안에서 자연스럽게 발생할 수 있다.   
> 프록시는 객체안에서의 개념도 있고, 웹 서버에서의 프록시도 있다.  
> 객체안에서 객체로 구현되어있는가, 웹 서버로 구현되어 있는가 처럼 규모의 차이가 있을 뿐 근본적인 역할은 같다.


- 프록시 패턴 vs 데코레이터 패턴
- 의도(intent)   
> 사실 프록시 패턴과 데코레이터 패턴은 그 모양이 거의 같고, 상황에 따라 정말 똑같을 때도 있다. 그러면 둘을 어떻게 구분하는 것일까?
> 디자인 패턴에서 중요한 것은 해당 패턴의 겉모양이 아니라 그 패턴을 만든 의도가 더 중요하다. 따라서 의도에 따라 패턴을 구분한다.
- 프록시 패턴의 의도: 다른 개체에 대한 접근을 제어하기 위해 대리자를 제공
- 데코레이터 패턴의 의도: 객체에 추가 책임(기능)을 동적으로 추가하고, 기능 확장을 위한 유연한 대안 제공
- <b>프록시를 사용하고 해당 프록시가 접근 제어가 목적이라면 프록시 패턴이고, 새로운 기능을 추가하는 것이 목적이라면 데코레이터 패턴이 된다.</b>


### 인터페이스, 클래스 기반 프록시 적용
### 인터페이스 기반 프록시 vs 클래스 기반 프록시
- 인터페이스가 없어도 클래스 기반으로 프록시를 생성할 수 있다.
- 클래스 기반 프록시는 해당 클래스에만 적용할 수 있다. 
- 인터페이스 기반 프록시는 인터페이스만 같으면 모든 곳에 적용할 수 있다.
- 클래스 기반 프록시는 상속을 사용하기 때문에 몇가지 제약이 있다.
  - 부모 클래스의 생성자를 호출해야 한다. 클래스에 final 키워드가 붙으면 상속이 불가능하다.
  - 메서드에 final 키워드가 붙으면 해당 메서드를 오버라이딩 할 수 없다.
- 이렇게 보면 인터페이스 기반의 프록시가 더 좋아보인다. 맞다. 인터페이스 기반의 프록시는 상속이라는 제약에서 자유롭다.  
  프로그래밍 관점에서도 인터페이스를 사용하는 것이 역할과 구현을 명확하게 나누기 때문에 더 좋다.
  인터페이스 기반 프록시의 단점은 인터페이스가 필요하다는 그 자체이다. 인터페이스가 없으면 인터페이스 기반 프록시를 만들 수 없다.
> 참고: 인터페이스 기반 프록시는 캐스팅 관련해서 단점이 있다.
### 결론
- 프록시를 적용할 때 V1처럼 인터페이스도 있고, V2처럼 구체 클래스도 있다. 따라서 2가지 상황을 모두 대응할 수 있어야 한다.
- 현재는 프록시 클래스를 너무 많이 만들어야한다. 대상 클래스에 따라 프록시 클래스들 다 만들어주어야한다. 동적 프록시로 해결해 보자.
- - -

## 동적 프록시 
 - 리플렉션 (reflection)
 - JDK 동적 프록시는 인터페이스가 필수이다. 인터페이스 없이 클래스만 있는 경우에는 어떻게 동적 프록시를 적용할 수 있을까?
   이것은 일반적인 방법으로는 어렵고 CGLIB 라는 바이트코드를 조작하는 특별한 라이브러리를 사용해야 한다.
 - CGLIB
   - CGLIB 는 바이트코드를 조작해서 동적으로 클래스를 생성하는 기술을 제공하는 라이브러리이다. 
   - CGLIB 를 사용하면 인터페이스가 없어도 구체 클래스만 가지고 동적 프록시를 만들어낼 수 있다. 
   - CGLIB 는 원래는 외부 라이브러리인데, 스프링 프레임워크가 스프링 내부 소스 코드에 포함했다. 따라서 스프링을 사용한다면 별도의 외부 라이브러리를 추가하지 않아도 사용할 수 있다.
   - 참고로 우리가 CGLIB 를 직접 사용하는 경우는 거의 없다. 스프링의 ProxyFactory 라는 것이 이 기술을 편리하게 사용하게 도와주기 때문에, 너무 깊이있게 파기 보다는 CGLIB 가 무엇인지 대략 개념만 잡으면 된다.
   - 
---
   
## Proxy Factory
- 스프링은 동적 프록시를 통합해서 편리하게 만들어주는 프록시 팩토리( ProxyFactory )라는 기능을 제공한다.
- 프록시 팩토리는 인터페이스가 있으면 JDK 동적 프록시를 (InvocationHandler) 사용하고, 구체 클래스만 있다면 CGLIB 를 (MethodInterceptor) 사용한다. 그리고 이 설정을 변경할 수도 있다
- 프록시 팩토리를 사용하면 Advice 를 호출하는 전용 InvocationHandler , MethodInterceptor 를 내부에서 사용한다.

---

## PointCut, Advice, Advisor
- 포인트컷( Pointcut ) 
  - 어디에 부가 기능을 적용할지, 어디에 부가 기능을 적용하지 않을지 판단하는 필터링 로직이다. 
    주로 클래스와 메서드 이름으로 필터링 한다. 이름 그대로 어떤 포인트(Point)에 기능을 적용할지 하지 않을지 잘라서(cut) 구분하는 것이다.
  - 대상 여부를 확인하는 필터 역할만 담당한다.
  - 크게 ClassFilter 와 MethodMatcher 둘로 이루어진다. 이름 그대로 하나는 클래스가
    맞는지, 하나는 메서드가 맞는지 확인할 때 사용한다. 둘다 true 로 반환해야 어드바이스를 적용할 수 있다.
- 어드바이스( Advice )
  - 이전에 본 것 처럼 프록시가 호출하는 부가 기능이다. 단순하게 프록시 로직이라 생각하면 된다.
  - 깔끔하게 부가 기능 로직만 담당한다.
- 어드바이저( Advisor )
  - 단순하게 하나의 포인트컷과 하나의 어드바이스를 가지고 있는 것이다. 쉽게 이야기해서 포인트컷1 + 어드바이스1이다.
> 조언( Advice )을 어디( Pointcut )에 할 것인가? 조언자( Advisor )는 어디( Pointcut )에 조언( Advice )을 해야할지 할지 알고 있다.
---

## 빈 후처리기 - BeanPostProcessor
> 스프링이 빈 저장소에 등록할 목적으로 생성한 객체를 빈 저장소에 등록하기 직전에 조작하고 싶다면 빈 후처리기를 사용하면 된다.  
> 빈 포스트 프로세서( BeanPostProcessor )는 번역하면 빈 후처리기인데, 이름 그대로 빈을 생성한 후에 무언가를 처리하는 용도로 사용한다.  
> 객체를 조작할 수도 있고, 완전히 다른 객체로 바꿔치기 하는 것도 가능하다.

> 빈 등록 과정을 빈 후처리기와 함께 살펴보자
> 1. 생성: 스프링 빈 대상이 되는 객체를 생성한다. ( @Bean , 컴포넌트 스캔 모두 포함)
> 2. 전달: 생성된 객체를 빈 저장소에 등록하기 직전에 빈 후처리기에 전달한다.
> 3. 후 처리 작업: 빈 후처리기는 전달된 스프링 빈 객체를 조작하거나 다른 객체로 바뀌치기 할 수 있다.  
> 4. 등록: 빈 후처리기는 빈을 반환한다. 전달 된 빈을 그대로 반환하면 해당 빈이 등록되고, 바꿔치기 하면 다른 객체가 빈 저장소에 등록된다.

## Spring BeanPostProcessor (AnnotationAwareAspectJAutoProxyCreator)
1. 생성: 스프링이 스프링 빈 대상이 되는 객체를 생성한다. ( @Bean , 컴포넌트 스캔 모두 포함)
2. 전달: 생성된 객체를 빈 저장소에 등록하기 직전에 빈 후처리기에 전달한다.
3. 모든 Advisor 빈 조회: 자동 프록시 생성기 - 빈 후처리기는 스프링 컨테이너에서 모든 Advisor 를 조회한다.
4. 프록시 적용 대상 체크: 앞서 조회한 Advisor 에 포함되어 있는 포인트컷을 사용해서 해당 객체가 프록시를 적용할 대상인지 아닌지 판단한다. 이때 객체의 클래스 정보는 물론이고, 해당 객체의 모든 메서드를 포인트컷에 하나하나 모두 매칭해본다. 그래서 조건이 하나라도 만족하면 프록시 적용 대상이 된다. 예를 들어서 10개의 메서드 중에 하나만 포인트컷 조건에 만족해도 프록시 적용 대상이 된다.
5. 프록시 생성: 프록시 적용 대상이면 프록시를 생성하고 반환해서 프록시를 스프링 빈으로 등록한다. 만약 프록시 적용 대상이 아니라면 원본 객체를 반환해서 원본 객체를 스프링 빈으로 등록한다.
6. 빈 등록: 반환된 객체는 스프링 빈으로 등록된다.

## @Aspect
> @Aspect 는 관점 지향 프로그래밍(AOP)을 가능하게 하는 AspectJ 프로젝트에서 제공하는 애노테이션이다. 스프링은 이것을 차용해서 프록시를 통한 AOP 를 가능하게 한다. 
> 자동 프록시 생성기 ( AnnotationAwareAspectJAutoProxyCreator )는 Advisor 를 자동으로 찾아와서 필요한 곳에 프록시를 생성하고 적용 준다 했다.
> 자동 프록시 생성기는 여기에 추가로 하나의 역할을 더 하는데, 바로 @Aspect 를 찾아서 이것을 Advisor 로 만들어준다. 
> 쉽게 이야기해서 지금까지 기능에 @Aspect 를 Advisor 로 변환해서 저장하는 기능도 한다. 
> 그래서 이름 앞에 AnnotationAware (애노테이션을 인식하는)가 붙어 있는 것이다.

<pre>
@Aspect 를 어드바이저로 변환해서 저장하는 과정

1. 실행: 스프링 애플리케이션 로딩 시점에 자동 프록시 생성기를 호출한다.
2. 모든 @Aspect 빈 조회: 자동 프록시 생성기는 스프링 컨테이너에서 @Aspect 애노테이션이 붙은 스프링 빈을 모두 조회한다.
3. 어드바이저 생성: @Aspect 어드바이저 빌더를 통해 @Aspect 애노테이션 정보를 기반으로 어드바이저를 생성한다.
4. @Aspect 기반 어드바이저 저장: 생성한 어드바이저를 @Aspect 어드바이저 빌더 내부에 저장한다.

@Aspect 어드바이저 빌더
BeanFactoryAspectJAdvisorsBuilder 클래스이다. 
@Aspect 의 정보를 기반으로 포인트컷, 어드바이스, 어드바이저를 생성하고 보관하는 것을 담당한다. 
@Aspect 의 정보를 기반으로 어드바이저를 만들고, @Aspect 어드바이저 빌더 내부 저장소에 캐시한다. 
캐시에 어드바이저가 이미 만들어져 있는 경우 캐시에 저장된 어드바이저를 반환한다.
</pre>

