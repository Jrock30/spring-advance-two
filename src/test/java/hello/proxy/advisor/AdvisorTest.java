package hello.proxy.advisor;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Method;

@Slf4j
public class AdvisorTest {

    @Test
    public void advisorTest1() throws Exception {
        ServiceImpl target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        /**
         * Advisor 인터페이스의 가장 일반적인 구현체이다.
         * 생성자를 통해 하나의 포인트컷과 하나의 어드바이스를 넣어주면 된다.
         * 어드바이저는 하나의 포인트컷과 하나의 어드바이스로 구성된다.
         */
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(Pointcut.TRUE, new TimeAdvice()); // 항상 참인 포인트컷
        /**
         * 프록시 팩토리에 적용할 어드바이저를 지정한다. 어드바이저는 내부에 포인트컷과 어드바이스를 모두 가지고 있다.
         * 따라서 어디에 어떤 부가 기능을 적용해야 할지 어드바이스 하나로 알 수 있다. 프록시 팩토리를 사용할 때 어드바이저는 필수이다.
         */
        proxyFactory.addAdvisor(advisor);
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
        proxy.find();
    }

    @Test
    @DisplayName("직접 만든 포인트컷")
    public void advisorTest2() throws Exception {
        ServiceImpl target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(new MyPointcut(), new TimeAdvice()); // 항상 참인 포인트컷
        proxyFactory.addAdvisor(advisor);
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
        proxy.find();
    }

    @Test
    @DisplayName("스프링이 제공하는 포인트컷")
    public void advisorTest3() throws Exception {
        ServiceImpl target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        /**
         * NameMatchMethodPointcut : 메서드 이름을 기반으로 매칭한다. 내부에서는 PatternMatchUtils 를 사용한다.
         * 예) *xxx* 허용
         * JdkRegexpMethodPointcut : JDK 정규 표현식을 기반으로 포인트컷을 매칭한다. TruePointcut : 항상 참을 반환한다.
         * AnnotationMatchingPointcut : 애노테이션으로 매칭한다. AspectJExpressionPointcut : aspectJ 표현식으로 매칭한다. *** 실무에서 거의 사용 ***
         *
         *
         */
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut(); // 스프링 제공 포인트 컷, *xx* 허용
        pointcut.setMappedNames("save");
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, new TimeAdvice()); // 항상 참인 포인트컷
        proxyFactory.addAdvisor(advisor);
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
        proxy.find();
    }

    static class MyPointcut implements Pointcut {
        @Override
        public ClassFilter getClassFilter() {
            return ClassFilter.TRUE;
        }

        @Override
        public MethodMatcher getMethodMatcher() {
            return new MyMethodMatcher();
        }
    }

    /**
     * 직접 구현한 MethodMatcher (스프링이 다 해줘서 직접 구현할 일은 거의 없을 듯)
     */
    static class MyMethodMatcher implements MethodMatcher {

        private String matchName = "save";

        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            boolean result = method.getName().equals(matchName);
            log.info("포인트컷 호출 method={} targetClass={}", method.getName(), targetClass);
            log.info("포인트컷 결과 result={}", result);
            return result;
        }

        @Override
        public boolean isRuntime() {
            return false;
        }

        @Override
        public boolean matches(Method method, Class<?> targetClass, Object... args) {
            return false;
        }
    }
}
