package tools.demo.test;

import tools.demo.service.OneService;
import tools.demo.utils.SpringUtil;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

@WebService
public class WebTest {
    @WebMethod
    public String login(String name) {
       // TestService testService = (TestService) SpringUtil.getBean( "testService" );
      // OneService oneService= (OneService) SpringUtil.getBean( "oneService" );
        OneService oneService=   SpringUtil.getBean( "oneService" ,OneService.class);
        String result = "0";
        try {
            if (null == result) {
                return result;
            }
            result = oneService.getName( name );
        } catch (Exception e) {
            return "error";
        }

        return result;
    }

    public static void main(String[] args) {
//        String address = "http://localhost:9900/PushMessage/sendOcuMessageToUsers";
//        Endpoint.publish( address, new WebTest() );
//        System.out.println( "完成" );
        TestService testService = (TestService) SpringUtil.getBean( "testService" );
        System.out.println( testService.say() );

    }
}
