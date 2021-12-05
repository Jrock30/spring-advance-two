package hello.proxy.jdkdynamic.code;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 동적 프록시에 적용할 핸들러 로직이다.
 *
 * 동적 프록시는 java.lang.reflect.Proxy 를 통해서 생성할 수 있다.
 * 클래스 로더 정보, 인터페이스, 그리고 핸들러 로직을 넣어주면 된다. 그러면 해당 인터페이스를 기반으로 동적 프록시를 생성하고 그 결과를 반환한다.
 */
@Slf4j
public class TimeInvocationHandler implements InvocationHandler { // JDK 동적 프록시에 적용할 공통 로직을 개발할 수 있다.

    private final Object target;

    public TimeInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        Object result = method.invoke(target, args); // 동적, proxy 호출 로직, call()

        long endTIme = System.currentTimeMillis();
        long resultTime = endTIme - startTime;
        log.info("TimeProxy 종료 resultTime={}", resultTime);
        return result;
    }
}
