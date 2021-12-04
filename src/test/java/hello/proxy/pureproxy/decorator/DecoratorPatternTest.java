package hello.proxy.pureproxy.decorator;

import hello.proxy.pureproxy.decorator.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * Decorator Pettern
 *
 * - 부가 기능 추가
 * 앞서 설명한 것 처럼 프록시를 통해서 할 수 있는 기능은 크게 접근 제어와 부가 기능 추가라는 2가지로 구분한다. 앞서 프록시 패턴에서 캐시를 통한 접근 제어를 알아보았다.
 * 이번에는 프록시를 활용해서 부가 기능을 추가해보자. 이렇게 프록시로 부가 기능을 추가하는 것을 데코레이터 패턴이라 한다.
 * 데코레이터 패턴: 원래 서버가 제공하는 기능에 더해서 부가 기능을 수행한다.
 * 예) 요청 값이나, 응답 값을 중간에 변형한다.
 * 예) 실행 시간을 측정해서 추가 로그를 남긴다.
 */
@Slf4j
public class DecoratorPatternTest {

    @Test
    public void noDecorator() throws Exception {

        RealComponent realComponent = new RealComponent();
        DecoratorPatternClient client = new DecoratorPatternClient(realComponent);
        client.execute();
    }

    @Test
    public void decorator1() throws Exception {
        Component realComponent = new RealComponent();
        Component messageDecorator = new MessageDecorator(realComponent);
        DecoratorPatternClient client = new DecoratorPatternClient(messageDecorator);
        client.execute();
    }

    /**
     * proxy 가 proxy 호출 느낌
     */
    @Test
    public void decorator2() throws Exception {
        Component realComponent = new RealComponent();
        Component messageDecorator = new MessageDecorator(realComponent);
        Component timeDecorator = new TimeDecorator(messageDecorator);
        DecoratorPatternClient client = new DecoratorPatternClient(timeDecorator);
        client.execute();
    }
}
