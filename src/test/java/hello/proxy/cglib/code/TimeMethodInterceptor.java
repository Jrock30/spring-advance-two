package hello.proxy.cglib.code;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * CGLIB
 * 인터페이스 없이 클래스만 있는 경우에는 어떻게 동적 프록시 적용
 * MethodInterceptor
 *
 */
@Slf4j
public class TimeMethodInterceptor implements MethodInterceptor { // Spring MethodInterceptor

    private final Object target;

    public TimeMethodInterceptor(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        Object result = methodProxy.invoke(target, args);// method.invoke 를 사용하여도 되나 MethodProxy 를 권장, 조금 더 빠르다고 한다.

        long endTIme = System.currentTimeMillis();
        long resultTime = endTIme - startTime;
        log.info("TimeProxy 종료 resultTime={}", resultTime);
        return result;
    }
}
