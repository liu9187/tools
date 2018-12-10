package tools.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class StartUp {

    public static void main(String[] args) {
        SpringApplication.run( StartUp.class, args );
//        String address= "http://localhost:9900/PushMessage/sendOcuMessageToUsers";
//        Endpoint.publish( address,new WebTest());


    }
}
