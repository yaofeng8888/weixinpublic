package com.yf.controller;

import com.yf.util.SignUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TestController {
    @RequestMapping("getTest")//此地址的路径，后面的配置的时候会用到
    public String getTest(HttpServletRequest request){
        String signature =request.getParameter("signature");//微信服务器传过来
        //时间戳
        String timestamp = request.getParameter("timestamp");//微信服务器传过来
        //随机数
        String nonce = request.getParameter("nonce");//微信服务器传过来
        //随机字符串
        String enchostr = request.getParameter("enchostr");//微信服务器传过来

        if (SignUtil.checkSignnature(signature,timestamp,nonce)){//此处需要服务器对传递过来的书记进行校验
            //用于判断是否是来自微信服务器
            System.out.println("success");
            return enchostr;
        }
        System.out.println("fail");
        return "fail";
    }
}
