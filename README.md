# Spring Advanced Proxy, Decorator Pattern
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