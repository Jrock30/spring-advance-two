package hello.proxy.app.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *   - @RequestMapping : 스프링MVC는 타입에 @Controller 또는 @RequestMapping 애노테이션이 있어야 스프링 컨트롤러로 인식한다.
 *     그리고 스프링 컨트롤러로 인식해야, HTTP URL이 매핑되고 동작한다. 이 애노테이션은 인터페이스에 사용해도 된다.
 *   - @ResponseBody : HTTP 메시지 컨버터를 사용해서 응답한다. 이 애노테이션은 인터페이스에 사용해도 된다.
 *   - @RequestParam("itemId") String itemId : 인터페이스에는 @RequestParam("itemId") 의 값을 생략하면 itemId 단어를 컴파일 이후 자바 버전에 따라 인식하지 못할 수 있다.
 *     인터페이스에서는 꼭 넣어주자. 클래스에는 생략해도 대부분 잘 지원된다.
 *   - 코드를 보면 request() , noLog() 두 가지 메서드가 있다. request() 는 LogTrace 를 적용할 대상이고, noLog() 는 단순히 LogTrace 를 적용하지 않을 대상이다.
 */
@RequestMapping // 스프링은 @Controller 또는 @RequestMapping 이 있어야 스프링 컨트롤러로 인식
@ResponseBody
public interface OrderControllerV1 {

    @GetMapping("/v1/request")
    String request(@RequestParam("itemId") String itemId);

    @GetMapping("/v1/no-log")
    String noLog();
}
