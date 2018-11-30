package com.yf.controller;

import com.yf.util.MessageUtil;
import com.yf.util.SignUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.Element;
import java.util.Map;

/**
 * 微信的公众号认证和消息发送都是这一个接口，所以我们需要判断
 * 到底是认证还是消息。
 * 微信文档规定get请求是认证，post请求是消息---->是一个xml文件的数据包
 */
@RestController
public class TestController {

    @RequestMapping("getTest")//此地址的路径，后面的配置的时候会用到
    public String getTest(HttpServletRequest request) {
        String method = request.getMethod();//请求方式
        if (method.equalsIgnoreCase("get")) {
            String signature = request.getParameter("signature");//微信服务器传过来
            //时间戳
            String timestamp = request.getParameter("timestamp");//微信服务器传过来
            //随机数
            String nonce = request.getParameter("nonce");//微信服务器传过来
            //随机字符串
            String enchostr = request.getParameter("enchostr");//微信服务器传过来

            if (SignUtil.checkSignnature(signature, timestamp, nonce)) {//此处需要服务器对传递过来的书记进行校验
                //用于判断是否是来自微信服务器
                System.out.println("success");
                return enchostr;
            }
            System.out.println("fail");
            return "fail";
        } else  if (request.getMethod().equalsIgnoreCase("post")){
            //接收用户的消息
            String requMessage = null;//返回的xml数据变量
            String respContent = "找不到相关数据，请确认发送类容";
            String fromUserName = "";
            String toUserName = "";
            String msgType = "";
            try {
                //xml请求解析
                Map<String, String> map = MessageUtil.pareXml(request);
                //发送账号
                fromUserName = map.get("FromUserName");
                //公众账号
                toUserName = map.get("ToUserName");
                //消息类型
                msgType = map.get("MsgType");
                //需要判断消息类型来做具体处理
                if ("text".equalsIgnoreCase(msgType)) {
                    String fromContent = map.get("Content");
                    if (fromContent.contains("英雄联盟")) {
                        respContent = "欢迎回来，召唤师!";
                    }
                }else if("event".equalsIgnoreCase(msgType)){
                    String event = map.get("Event");//获取菜单的事件类型
                    if (event.equalsIgnoreCase("click")){//是一个点击事件
                        String eventKey = map.get("EventKey");//获取点击事件的key
                        if(eventKey.equalsIgnoreCase("first")){//点击了第一个按钮
                            //需要执行了业务逻辑
                        }else if (eventKey.equalsIgnoreCase("second")){   //点击了第二个按钮
                            //需要执行了业务逻辑
                        }else if(eventKey.equalsIgnoreCase("third")){//点击了第三个按钮
                            //需要执行了业务逻辑
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                respContent = "服务器跑丢了";
            }
            requMessage = ("<xml><toUserName><![CDATA["+fromUserName+"]]></ToUserName>"+"<FromUserName>" +
                    "<![CDATA["+toUserName+"]]></FormUserName><CreateTime>"+System.currentTimeMillis()+
                    "</CreateTime><MsgType><![CDATA[text]]><MsgType><Content><![CDATA["+respContent+"]]></Content></xml>");
        return requMessage;
        }
        System.out.println("失败了");
        return "";
    }
}
