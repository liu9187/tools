package tools.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tools.demo.service.WeChatPayService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v2/wxpay")
public class WeChatPayController {
    @Autowired
    private WeChatPayService wxPayService;

    /**
     * 微信调用
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/notify",method = {RequestMethod.GET,RequestMethod.POST})
      public String wxNotify(HttpServletRequest request, HttpServletResponse response){
            try {
                return  wxPayService.callBack(request,response);
            }catch (Exception e){
                 response.setHeader("Content-type","application/xml");
                 return "<xml>\n" +
                         "  <return_code><![CDATA[FAIL]]></return_code>\n" +
                         "  <return_msg><![CDATA[]]></return_msg>\n" +
                         "</xml>";
            }
      }
}
