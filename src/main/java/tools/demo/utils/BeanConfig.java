package tools.demo.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.demo.service.Impl.OneServiceImpl;
import tools.demo.service.OneService;
import tools.demo.test.TestService;

@Configuration
public class BeanConfig {
    @Bean("testService")
    public TestService testService() {
        TestService testService = new TestService();
        return testService;
    }
    @Bean("oneService")
    public OneService oneService(){
        OneServiceImpl impl=new OneServiceImpl();
        OneService oneService= name ->impl.getName( name ) ;
        return  oneService;
    }
}
