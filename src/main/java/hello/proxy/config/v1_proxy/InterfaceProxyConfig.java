package hello.proxy.config.v1_proxy;

import hello.proxy.app.v1.*;
import hello.proxy.config.v1_proxy.interface_proxy.OrderControllerInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderRepositoryInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderServiceInterfaceServiceProxy;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * V1 프록시 런타임 객체 의존 관계 설정
 *
 * 프록시의 런타임 객체 의존 관계를 설정하면 된다.
 * 기존에는 스프링 빈이 orderControlerV1Impl , orderServiceV1Impl 같은 실제 객체를 반환했다.
 * 하지만 이제는 프록시를 사용해야한다. 따라서 프록시를 생성하고 프록시를 실제 스프링 빈 대신 등록한다. 실제 객체는 스프링 빈으로 등록하지 않는다.
 * 프록시는 내부에 실제 객체를 참조하고 있다. 예를 들어서 OrderServiceInterfaceProxy 는 내부에 실제 대상 객체인 OrderServiceV1Impl 을 가지고 있다.
 * 정리하면 다음과 같은 의존 관계를 가지고 있다.
 *      proxy -> target
 *      orderServiceInterfaceProxy -> orderServiceV1Impl
 * 스프링 빈으로 실제 객체 대신에 프록시 객체를 등록했기 때문에 앞으로 스프링 빈을 주입 받으면 실제 객체 대신에 프록시 객체가 주입된다.
 * 실제 객체가 스프링 빈으로 등록되지 않는다고 해서 사라지는 것은 아니다.
 * 프록시 객체가 실제 객체를 참조하기 때문에 프록시를 통해서 실제 객체를 호출할 수 있다. 쉽게 이야기해서 프록시 객체 안에 실제 객체가 있는 것이다.
 *
 * - InterfaceProxyConfig 를 통해 프록시를 적용한 후
 *     스프링 컨테이너에 프록시 객체가 등록된다. 스프링 컨테이너는 이제 실제 객체가 아니라 프록시 객체를 스프링 빈으로 관리한다.
 *     이제 실제 객체는 스프링 컨테이너와는 상관이 없다. 실제 객체는 프록시 객체를 통해서 참조될 뿐이다.
 *     프록시 객체는 스프링 컨테이너가 관리하고 자바 힙 메모리에도 올라간다. 반면에 실제 객체는 자바 힙 메모리에는 올라가지만 스프링 컨테이너가 관리하지는 않는다.
 */
@Configuration
public class InterfaceProxyConfig {
    @Bean
    public OrderControllerV1 orderController(LogTrace logTrace) {
        OrderControllerV1Impl controllerImpl = new OrderControllerV1Impl(orderService(logTrace)); // 실제 객체 주입
        return new OrderControllerInterfaceProxy(controllerImpl, logTrace); // 스프링 빈 프록시 객체 등록, 프록시 안에서 내부객체 참조
    }

    @Bean
    public OrderServiceV1 orderService(LogTrace logTrace) {
        OrderServiceV1Impl serviceImpl = new OrderServiceV1Impl(orderRepository(logTrace)); // 실제 객체 주입
        return new OrderServiceInterfaceServiceProxy(serviceImpl, logTrace); // 스프링 빈 프록시 객체 등록, 프록시 안에서 내부객체 참조
    }

    @Bean
    public OrderRepositoryV1 orderRepository(LogTrace logTrace) {
        OrderRepositoryV1Impl repositoryImpl = new OrderRepositoryV1Impl(); // 실제 객체 주입
        return new OrderRepositoryInterfaceProxy(repositoryImpl, logTrace); // 스프링 빈 프록시 객체 등록, 프록시 안에서 내부객체 참조
    }
}
