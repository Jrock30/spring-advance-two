package hello.proxy.cglib;

import hello.proxy.cglib.code.TimeMethodInterceptor;
import hello.proxy.common.service.ConcreteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;

/**
 * CGLIB
 * 인터페이스 없이 클래스만 있는 경우에는 어떻게 동적 프록시 적용
 * MethodInterceptor
 *
 * Enhancer : CGLIB 는 Enhancer 를 사용해서 프록시를 생성한다.
 * enhancer.setSuperclass(ConcreteService.class) : CGLIB 는 구체 클래스를 상속 받아서 프록시를 생성할 수 있다. 어떤 구체 클래스를 상속 받을지 지정한다.
 * enhancer.setCallback(new TimeMethodInterceptor(target))
 * 프록시에 적용할 실행 로직을 할당한다. enhancer.create() : 프록시를 생성한다.
 * 앞서 설정한 enhancer.setSuperclass(ConcreteService.class) 에서 지정한 클래스를 상속 받아서 프록시가 만들어진다.
 *
 * JDK 동적 프록시는 인터페이스를 구현(implement)해서 프록시를 만든다. CGLIB는 구체 클래스를 상속 (extends)해서 프록시를 만든다.
 *
 * CGLIB 제약
 * - 클래스 기반 프록시는 상속을 사용하기 때문에 몇가지 제약이 있다.
 *   - 부모 클래스의 생성자를 체크해야 한다. CGLIB는 자식 클래스를 동적으로 생성하기 때문에 기본 생성자가 필요하다.
 *   - 클래스에 final 키워드가 붙으면 상속이 불가능하다. CGLIB 에서는 예외가 발생한다.
 *   - 메서드에 final 키워드가 붙으면 해당 메서드를 오버라이딩 할 수 없다. CGLIB 에서는 프록시 로직이 동작하지 않는다.
 */
@Slf4j
public class CglibTest {

    @Test
    public void cglib() throws Exception {
        ConcreteService target = new ConcreteService();

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ConcreteService.class); // 구체클래스 기반 상속받은
        enhancer.setCallback(new TimeMethodInterceptor(target));
        ConcreteService proxy = (ConcreteService) enhancer.create();// proxy 생성
        log.info("targetClass={}", target.getClass()); // class hello.proxy.common.service.ConcreteService
        log.info("proxyClass={}", proxy.getClass()); // class hello.proxy.common.service.ConcreteService$$EnhancerByCGLIB$$25d6b0e3
        proxy.call();
    }
}
