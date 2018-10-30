package tools.demo.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.demo.test.TestService;

@Configuration
public class BeanConfig {
    @Bean("testService")
    public TestService testService() {
        TestService testService = new TestService();
        return testService;
    }
}
