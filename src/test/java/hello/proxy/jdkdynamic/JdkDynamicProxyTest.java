package hello.proxy.jdkdynamic;

import hello.proxy.jdkdynamic.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

/**
 * AImpl , BImpl 각각 프록시를 만들지 않았다. 프록시는 JDK 동적 프록시를 사용해서 동적으로 만들고 TimeInvocationHandler 는 공통으로 사용했다.
 *
 * JDK 동적 프록시 기술 덕분에 적용 대상 만큼 프록시 객체를 만들지 않아도 된다. 그리고 같은 부가 기능 로직을 한번만 개발해서 공통으로 적용할 수 있다.
 * 만약 적용 대상이 100개여도 동적 프록시를 통해서 생성하고, 각각 필요한 InvocationHandler 만 만들어서 넣어주면 된다.
 * 결과적으로 프록시 클래스를 수 없이 만들어야 하는 문제도 해결하고, 부가 기능 로직도 하나의 클래스에 모아서 단일 책임 원칙(SRP)도 지킬 수 있게 되었다.
 */
@Slf4j
public class JdkDynamicProxyTest {

    @Test
    public void dynamicA() throws Exception {
        AInterface target = new AImpl();
        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        // 동적 생성
        AInterface proxy = (AInterface) Proxy.newProxyInstance(AInterface.class.getClassLoader(), new Class[]{AInterface.class}, handler);

        proxy.call();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());
    }

    @Test
    public void dynamicB() throws Exception {
        BInterface target = new BImpl();
        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        // 동적 생성, invoke() 호출
        BInterface proxy = (BInterface) Proxy.newProxyInstance(BInterface.class.getClassLoader(), new Class[]{BInterface.class}, handler);

        proxy.call();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());
    }

}
