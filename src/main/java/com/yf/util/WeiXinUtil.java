package com.yf.util;

import com.yf.model.Menu;
import net.sf.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

public class WeiXinUtil {
    public static final String APPID = "appid";
    public static final String APPSECRET = "APPSECRET";//开发者密码
    //获取access_token的接口地址(GET)限200次每天
    public final static String access_token_url = "https://aip.weixin.qq.com/cgi-bin" +
            "/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    //菜单创建（post）限100每天
    public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token = ACCESS_TOKEN";

    public static JSONObject httpRequest(String requestUrl,String requestMethod,String outputStr){
        JSONObject jsonObject = null;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            //创建SSLContext对象 并使用给我们指定的信任管理器初始化
            TrustManager[] tm  = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null,tm,new java.security.SecureRandom());
            //从上述SSLContext对象中得到SSLContextFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection connection =(HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(ssf);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type","application/json;charset = utf-8");
            connection.setDoInput(true);
            connection.setUseCaches(false);
            //设置请求方式（get/post）
            connection.setRequestMethod(requestMethod);
            if("GET".equalsIgnoreCase(requestMethod)){
                connection.connect();
            }
            //当有数据要提交的时候
            if(null!=outputStr){
                OutputStream outputStream = connection.getOutputStream();
                //防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            //返回的输入流转为字符串
            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = reader.readLine())!=null){
                stringBuffer.append(str);
            }
            reader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            connection.disconnect();
            jsonObject = JSONObject.fromObject(stringBuffer.toString());
        }catch (ConnectException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     *
     * @param appid
     * @param appsecret
     * @return
     */
    public static AccessToken getAccessToken(String appid,String appsecret){
        AccessToken accessToken = null;
        String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
        JSONObject jsonObject = httpRequest(requestUrl,"GET",null);
        //请求成功
        if(null!=jsonObject){
            try {
                accessToken = new AccessToken();
                accessToken.setToken(jsonObject.getString("access_token"));
                accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
            }catch (Exception e){
                //获取token失败
                System.out.println("获取token失败"+jsonObject.getInt("errcode"));
            }
        }
        return accessToken;
    }

    public static int createMenu(Menu menu, String accessToken){
        int result = 0;
        //拼装创建菜单的url
        String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
        //将菜单对象转为json字符串
        String josnMenu = JSONObject.fromObject(menu).toString();
        //调用创建菜单的接口
        JSONObject jsonObject = httpRequest(url,"POST",josnMenu);
        if(null!=josnMenu){
            if(0!=jsonObject.getInt("errcode")){
                result = jsonObject.getInt("errcode");
                System.out.println("创建菜单失败");
            }
        }
        return result;
    }
}
