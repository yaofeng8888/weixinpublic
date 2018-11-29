package com.yf.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageUtil {
    /**
     * 返回消息的类型：文本
     */
    public static final String RESP_MESSAGE_TYPE_TEXT = "text";

    /**
     * 解析微信发来的请求   XML
     * @param request
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static Map<String,String> pareXml(HttpServletRequest request)throws Exception{
        //将解析的结果存储在HashMap中
         Map<String,String> reqMap = new HashMap<>();
         //从request中获取输入流
        InputStream in = request.getInputStream();
        //读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(in);
        //得到XML根元素
            Element root = document.getRootElement();
        //得到所有的子节点
        List<Element> list = root.elements();
        for (Element ele:list) {
            reqMap.put(ele.getName(),ele.getText());
        }
        //释放资源
        in.close();
        in = null;
        return reqMap;
    }

    /**
     * 支持字符数据  CDATA块，比如发送的内容里面特殊符号
     * 如<![CDATA[<书名>]]>
     */
    private static XStream xStream = new XStream(new XppDriver(){
        @Override
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out){
            //对所有的xml节点的转换都增加CDATA标记
            boolean cdata = true;
                @Override
                @SuppressWarnings("unchecked")
                public void startNode(String name, Class clazz) {
                    super.startNode(name,clazz);
                }

                @Override
                protected void writeText(QuickWriter writer, String text) {
                    if(cdata){
                       writer.write("<![CDATA[");
                       writer.write(text);
                       writer.write("]]>");
                    }else {
                        writer.write(text);
                    }
                }
            };
        }
    });
}
