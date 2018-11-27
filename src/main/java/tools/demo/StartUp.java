package tools.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import tools.demo.test.WebTest;

import javax.xml.ws.Endpoint;
@SpringBootApplication
public class StartUp {

    public static void main(String[] args) {
        SpringApplication.run( StartUp.class, args );
//        String address= "http://localhost:9900/PushMessage/sendOcuMessageToUsers";
//        Endpoint.publish( address,new WebTest());


    }
}
