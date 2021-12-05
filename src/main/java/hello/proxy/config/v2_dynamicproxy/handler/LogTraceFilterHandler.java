package hello.proxy.config.v2_dynamicproxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.util.PatternMatchUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogTraceFilterHandler implements InvocationHandler {

    private final Object target;
    private final LogTrace logTrace;
    private final String[] patterns;

    public LogTraceFilterHandler(Object target, LogTrace logTrace, String[] patterns) {
        this.target = target;
        this.logTrace = logTrace;
        this.patterns = patterns;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 메서드 이름 필터
        String methodName = method.getName();
        // ex) save, request, reque*, *est, *que*
        if (!PatternMatchUtils.simpleMatch(patterns, methodName)) { // 패턴매칭이 안되면
            return method.invoke(target, args); // 특정이름이 매칭이 안되면 아래의 로그 남기지 않고 바로 invoke 로 넘어간다.
        }

        TraceStatus status = null;
        try {
            /**
             * message
             * LogTrace 에 사용할 메시지이다.
             * 프록시를 직접 개발할 때는 "OrderController.request()" 와 같이 프록시마다 호출되는 클래스와 메서드 이름을 직접 남겼다.
             * 이제는 Method 를 통해서 호출되는 메서드 정보와 클래스 정보를 동적으로 확인할 수 있기 때문에 이 정보를 사용하면 된다.
             */
            String message = method.getDeclaringClass().getSimpleName() + "." +
                    method.getName() + "()";
//            status = logTrace.begin("OrderController.request()");
            status = logTrace.begin(message);

            // target 호출(실제 호출할 대상), 로직
//            String result = target.request(itemId);
            Object result = method.invoke(target, args);

            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
