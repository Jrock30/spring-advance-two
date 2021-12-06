package hello.proxy.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Interface 있다, JDK 동적 프록시
 */
@Slf4j
public class TimeAdvice implements MethodInterceptor { // MethodInterceptor (org.aopalliance.intercept.MethodInterceptor), spring aop 안에 있음.
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable { // MethodInvocation 안에 타겟정보가 있음. 프록시 팩토리 생성할 때 넣음
        log.info("TimeAdvice 실행");
        long startTime = System.currentTimeMillis();

//        Object result = method.invoke(target, args); // 동적, proxy 호출 로직, call()
        Object result = invocation.proceed(); // 타겟 클래스를 호출하고 결과를 받는다. (알아서 호출(실행)을 해준다.)

        long endTIme = System.currentTimeMillis();
        long resultTime = endTIme - startTime;
        log.info("TimeProxy 종료 resultTime={}", resultTime);
        return result;
    }
}
