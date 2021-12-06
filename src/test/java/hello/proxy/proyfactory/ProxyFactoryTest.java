package hello.proxy.proyfactory;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ConcreteService;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

import static org.assertj.core.api.Assertions.*;

/**
 * 프록시 팩토리의 서비스 추상화 덕분에 구체적인 CGLIB, JDK 동적 프록시 기술에 의존하지 않고, 매우 편리하게 동적 프록시를 생성할 수 있다.
 * 프록시의 부가 기능 로직도 특정 기술에 종속적이지 않게 Advice 하나로 편리하게 사용할 수 있었다.
 * 이것은 프록시 팩토리가 내부에서 JDK 동적 프록시인 경우 InvocationHandler 가 Advice 를 호출하도록 개발해두고,
 * CGLIB 인 경우 MethodInterceptor 가 Advice 를 호출하도록 기능을 개발해두었기 때문이다.
 *
 * 참고
 * 스프링 부트는 AOP 를 적용할 때 기본적으로 proxyTargetClass=true 로 설정해서 사용한다.
 * 따라서 인터페이스가 있어도 항상 CGLIB 를 사용해서 구체 클래스를 기반으로 프록시를 생성한다.
 */
@Slf4j
public class ProxyFactoryTest {

    @Test
    @DisplayName("인터페이스가 있으면 JDK 동적 프록시 사용")
    public void interfaceProxy() throws Exception {
        ServiceImpl target = new ServiceImpl();
        /**
         * 프록시 팩토리에 타겟을 넣는다.
         * 그러면 MethodInterceptor 안에 MethodInvocation 에 타겟정보가 들어감
         *
         * 프록시 팩토리를 생성할 때, 생성자에 프록시의 호출 대상을 함께 넘겨준다.
         * 프록시 팩토리는 이 인스턴스 정보를 기반으로 프록시를 만들어낸다.
         * 만약 이 인스턴스에 인터페이스가 있다면 JDK 동적 프록시를 기본으로 사용하고 인터페이스가 없고 구체 클래스만 있다면 CGLIB를 통해서 동적 프록시를 생성한다.
         * 여기서는 target 이 new ServiceImpl() 의 인스턴스이기 때문에 ServiceInterface 인터페이스가 있다.
         * 따라서 이 인터페이스를 기반으로 JDK 동적 프록시를 생성한다.
         */
        ProxyFactory proxyFactory = new ProxyFactory(target); // 프록시 팩토리에 타겟을 넣는다.

        /**
         * 프록시 팩토리를 통해서 만든 프록시가 사용할 부가 기능 로직을 설정한다.
         * JDK 동적 프록시가 제공하는 InvocationHandler 와 CGLIB 가 제공하는 MethodInterceptor 의 개념과 유사하다.
         * 이렇게 프록시가 제공하는 부가 기능 로직을 어드바이스( Advice )라 한다. 번역하면 조언을 해준다고 생각하면 된다.
         */
        proxyFactory.addAdvice(new TimeAdvice()); // 프록시 팩토리에 설정한 advice 를 넣는다.

        /**
         * 프록시 객체를 생성하고 그 결과를 받는다.
         */
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass()); // JDK 동적프록시

        proxy.save(); // JdkDynamicProxy 와 똑같이 작동

        /**
         * AopUtils, 프록시 팩토리를 사용할 때만 사용가능
         */
        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();
        assertThat(AopUtils.isCglibProxy(proxy)).isFalse();
    }

    @Test
    @DisplayName("구체 클래스만 있으면 CGLIB 사용")
    public void concreteProxyProxy() throws Exception {
        ConcreteService target = new ConcreteService();

        /**
         * 프록시 팩토리에 타겟을 넣는다.
         * 그러면 MethodInterceptor 안에 MethodInvocation 에 타겟정보가 들어감
         *
         * 프록시 팩토리를 생성할 때, 생성자에 프록시의 호출 대상을 함께 넘겨준다.
         * 프록시 팩토리는 이 인스턴스 정보를 기반으로 프록시를 만들어낸다.
         * 만약 이 인스턴스에 인터페이스가 있다면 JDK 동적 프록시를 기본으로 사용하고 인터페이스가 없고 구체 클래스만 있다면 CGLIB 를 통해서 동적 프록시를 생성한다.
         * 여기서는 target 이 new ConcreteService() 의 인스턴스이기 때문에 구체 클래스 이다.
         * 따라서 이 구레 클래스 기반으로 CGLIB 를 사용한다.
         */
        ProxyFactory proxyFactory = new ProxyFactory(target); // 프록시 팩토리에 타겟을 넣는다.

        /**
         * 프록시 팩토리를 통해서 만든 프록시가 사용할 부가 기능 로직을 설정한다.
         * JDK 동적 프록시가 제공하는 InvocationHandler 와 CGLIB 가 제공하는 MethodInterceptor 의 개념과 유사하다.
         * 이렇게 프록시가 제공하는 부가 기능 로직을 어드바이스( Advice )라 한다. 번역하면 조언을 해준다고 생각하면 된다.
         */
        proxyFactory.addAdvice(new TimeAdvice()); // 프록시 팩토리에 설정한 advice 를 넣는다.

        /**
         * 프록시 객체를 생성하고 그 결과를 받는다.
         */
        ConcreteService proxy = (ConcreteService) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass()); // JDK 동적프록시

        proxy.call(); // JdkDynamicProxy 와 똑같이 작동

        /**
         * AopUtils, 프록시 팩토리를 사용할 때만 사용가능
         */
        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }

    @Test
    @DisplayName("ProxyTargetClass 옵션을 사용하면 인터페이스가 있어도 CGLIB 를 사용하고, 클래스 기반 프록시 사용")
    public void proxyTargetClass() throws Exception {
        ServiceImpl target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true); // ** 인터페이스가 있어도 CGLIB 를 사용하는 옵션 **
        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass()); // JDK 동적프록시

        proxy.save(); // JdkDynamicProxy 와 똑같이 작동

        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }
}
