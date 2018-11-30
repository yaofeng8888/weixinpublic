package com.yf.controller;

import com.yf.model.*;
import com.yf.util.AccessToken;
import com.yf.util.WeiXinUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MenuController {

    @RequestMapping("/createMenu")
    public String createMenu(){
        //此方法首先获取token，因为此接口不能每次都调用，所以因该调用此接口获取数据后保存起来，首次需要调用此方法
        AccessToken token = WeiXinUtil.getAccessToken(WeiXinUtil.APPID, WeiXinUtil.APPSECRET);
        if(token==null){
            return "token获取失败";
        }else {
            //创建菜单
            int menu = WeiXinUtil.createMenu(init(),token.getToken());
            if(menu==0){
                return "success";
            }else {
                return "fail";
            }
        }
    }

    public Menu init(){
        Menu menu = new Menu();
        List<Button> button = new ArrayList<>();//一级菜单
        ViewButton viewButton = new ViewButton();
        viewButton.setName("谷歌一下");
        viewButton.setUrl("http://www.google.com");
//        viewButton.setType("View");这里如果要指定，需要小写
        button.add(viewButton);

        ClickButton clickButton = new ClickButton();
        clickButton.setName("购买");
        clickButton.setKey("buy");//判断点击的是哪个按钮
        button.add(clickButton);


        CommonButton commonButton = new CommonButton();//一级菜单
        commonButton.setName("菜单");
        List<Button> sub_botton = new ArrayList<>();
        ClickButton clickButton1 = new ClickButton();
        clickButton1.setName("第一个按钮");//二级菜单
        clickButton1.setKey("first");//判断点击的是哪个按钮

        ClickButton clickButton2 = new ClickButton();
        clickButton2.setName("第二个按钮");//二级菜单
        clickButton2.setKey("second");//判断点击的是哪个按钮

        ClickButton clickButton3 = new ClickButton();
        clickButton3.setName("第一个按钮");//二级菜单
        clickButton3.setKey("third");//判断点击的是哪个按钮

        commonButton.setSub_button(sub_botton);
        button.add(commonButton);
        menu.setButton(button);
        return menu;
    }
}
