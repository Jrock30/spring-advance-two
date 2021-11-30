package hello.proxy.pureproxy.proxy;

import hello.proxy.pureproxy.proxy.code.CacheProxy;
import hello.proxy.pureproxy.proxy.code.ProxyPatternClient;
import hello.proxy.pureproxy.proxy.code.RealSubject;
import org.junit.jupiter.api.Test;

public class ProxyPatternTest {

    @Test
    public void noProxyTest() {
        RealSubject realSubject = new RealSubject();
        ProxyPatternClient client = new ProxyPatternClient(realSubject);
        client.execute();
        client.execute();
        client.execute();
    }

    /**
     *
     * realSubject 와 cacheProxy 를 생성하고 둘을 연결한다.
     * 결과적으로 cacheProxy 가 realSubject 를 참조하는 런타임 객체 의존관계가 완성된다.
     * 그리고 마지막으로 client 에 realSubject 가 아닌 cacheProxy 를 주입한다.
     * 이 과정을 통해서 client -> cacheProxy -> realSubject 런타임 객체 의존 관계가 완성된다.
     *
     * cacheProxyTest() 는 client.execute() 을 총 3번 호출한다.
     * 이번에는 클라이언트가 실제 realSubject 를 호출하는 것이 아니라 cacheProxy 를 호출하게 된다.
     *
     * 1. client cacheProxy 호출 -> cacheProxy 에 캐시 값이 없다. -> realSubject 를 호출, 결과를 캐시에 저장 (1초)
     * 2. client cacheProxy 호출 -> cacheProxy 에 캐시 값이 있다. -> cacheProxy 에서 즉시 반환 (0초)
     * 3. client cacheProxy 호출 -> cacheProxy 에 캐시 값이 있다. -> cacheProxy 에서 즉시 반환 (0초)
     *
     * 결과적으로 캐시 프록시를 도입하기 전에는 3초가 걸렸지만,
     * 캐시 프록시 도입 이후에는 최초에 한번만 1 초가 걸리고,이후에는 거의 즉시 반환한다.
     *
     * 정리
     *   - 프록시 패턴의 핵심은 RealSubject 코드와 클라이언트 코드를 전혀 변경하지 않고,
     *     프록시를 도입해서 접근 제어를 했다는 점이다. 그리고 클라이언트 코드의 변경 없이 자유롭게 프록시를 넣고 뺄 수 있다.
     *     실제 클라이언트 입장에서는 프록시 객체가 주입되었는지, 실제 객체가 주입되었는지 알지 못한다.
     *
     */
    @Test
    public void cacheProxyTest() throws Exception {
        RealSubject realSubject = new RealSubject(); // real object
        CacheProxy cacheProxy = new CacheProxy(realSubject); // cache proxy
        ProxyPatternClient client = new ProxyPatternClient(cacheProxy);
        client.execute();
        client.execute();
        client.execute();
    }
}
