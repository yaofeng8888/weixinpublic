package com.yf.util;

import java.security.MessageDigest;
import java.util.Arrays;

public class SignUtil {
    private static String token = "yaofengjava";//此处使用的值需要和服务器的配置token一致。改token是你在微信公众号里面设置的
                                                //(令牌)token

    /**
     * 校验签名
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @return 布尔值
     */
    public static boolean checkSignnature(String signature,String timestamp,String nonce){
        String checktext = null;
        if(null!=signature){
            //对token timestamp nonce 按照字典排序
            String[] paramArr = new String[]{token,timestamp,nonce};
            Arrays.sort(paramArr);
            //将排序后的结果拼成一个字符串；
            String  content = paramArr[0].concat(paramArr[1]).concat(paramArr[2]);
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
                //对接后的字符串进行加密
                byte[] bytes = messageDigest.digest(content.toString().getBytes());
                checktext = byteToStr(bytes);
            }catch (Exception e){

            }
        }
        //将加密后的字符串与signature进行对比
        return checktext!=null?checktext.equals(signature.toUpperCase()):false;
    }

    /**
     * 将字节数组转为十六进制的字符串
     * @param byteArray
     * @return
     */
    private static String byteToStr(byte[] byteArray){
        String  str = "";
        for(int i = 0;i<byteArray.length;i++){
            str =byteToHexStr(byteArray[i]);
        }
        return str;
    }

    /**
     * 将字节转化为十六进制的字符串
     * @param myByte
     * @return
     */
    private static String byteToHexStr(byte myByte){
        char[] Digit = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] tamArr = new char[2];
        tamArr[0] = Digit[(myByte>>>4)&0x0F];
        tamArr[1] = Digit[myByte&0X0F];
        String str = new String(tamArr);
        return str;
    }
}
