package hello.proxy.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Reflection
 *
 * 리플렉션을 사용하면 클래스와 메서드의 메타정보를 사용해서 애플리케이션을 동적으로 유연하게 만들 수 있다.
 * 하지만 리플렉션 기술은 런타임에 동작하기 때문에, 컴파일 시점에 오류를 잡을 수 없다.
 *
 * 예를 들어서 지금까지 살펴본 코드에서 getMethod("callA") 안에 들어가는 문자를 실수로 getMethod("callZ") 로 작성해도 컴파일 오류가 발생하지 않는다.
 * 그러나 해당 코드를 직접 실행하는 시점에 발생하는 오류인 런타임 오류가 발생한다.
 *
 * 가장 좋은 오류는 개발자가 즉시 확인할 수 있는 컴파일 오류이고, 가장 무서운 오류는 사용자가 직접 실행할 때 발생하는 런타임 오류다.
 * 따라서 리플렉션은 일반적으로 사용하면 안된다.
 * 지금까지 프로그래밍 언어가 발달하면서 타입 정보를 기반으로 컴파일 시점에 오류를 잡아준 덕분에 개발자가 편하게 살았는데, 리플렉션은 그것에 역행하는 방식이다.
 * 리플렉션은 프레임워크 개발이나 또는 매우 일반적인 공통 처리가 필요할 때 부분적으로 주의해서 사용해야 한다.
 */
@Slf4j
public class ReflectionTest {

    @Test
    public void reflection0() throws Exception {
        Hello target = new Hello();

        // 공통 로직1 시작
        log.info("start");
        String result1 = target.callA(); // 호출하는 메서드만 다름, 동적처리 필요
        log.info("result={}", result1);

        // 공통 로직2 시작
        log.info("start");
        String result2 = target.callB(); // 호출하는 메서드만 다름, 동적처리 필요(리플렉션, 람다)
        log.info("result={}", result2);
    }

    @Test
    public void reflection1() throws Exception {
        // 클래스 정보
        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello"); // $ -> 내부 클래스

        Hello target = new Hello();

        // callA 메서드 정보
        Method methodCallA = classHello.getMethod("callA");
        Object result1 = methodCallA.invoke(target);
        log.info("result1={}", result1);

        // callB 메서드 정보
        Method methodCallB = classHello.getMethod("callB");
        Object result2 = methodCallB.invoke(target);
        log.info("result2={}", result2);
    }

    /**
     * 정적인 target.callA() , target.callB() 코드를 리플렉션을 사용해서 Method 라는 메타정보로 추상화했다.
     * 덕분에 공통 로직을 만들 수 있게 되었다.
     */
    @Test
    public void reflection2() throws Exception {
        // 클래스 정보
        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello"); // $ -> 내부 클래스

        Hello target = new Hello();
        Method methodCallA = classHello.getMethod("callA");
        dynamicCall(methodCallA, target);

        Method methodCallB = classHello.getMethod("callB");
        dynamicCall(methodCallB, target);
    }

    /**
     * 공통 로직1, 공통 로직2를 한번에 처리할 수 있는 통합된 공통 처리 로직이다.
     *
     * Method method : 첫 번째 파라미터는 호출할 메서드 정보가 넘어온다. 이것이 핵심이다.
     * 기존에는 메서드 이름을 직접 호출했지만, 이제는 Method 라는 메타정보를 통해서 호출할 메서드 정보가 동적으로 제공된다.
     *
     *  Object target
     *    - 실제 실행할 인스턴스 정보가 넘어온다. 타입이 Object 라는 것은 어떠한 인스턴스도 받을 수 있다는 뜻이다.
     *    - 물론 method.invoke(target) 를 사용할 때 호출할 클래스와 메서드 정보가 서로 다르면 예외가 발생한다.
     */
    private void dynamicCall(Method method, Object target) throws Exception {
        log.info("start");
//        String result = target.callA(); // 호출하는 메서드만 다름, 동적처리 필요
        Object result = method.invoke(target); // 추상화로 변경
        log.info("result={}", result);
    }

    @Slf4j
    static class Hello {
        public String callA() {
            log.info("callA");
            return "A";
        }

        public String callB() {
            log.info("callB");
            return "B";
        }
    }
}
