package com.major.wxtooldemo;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMenuService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义菜单管理
 * <p>
 * Created by Major on 2018/11/25
 */
public class MenuControllerTest {


    private WxMpMenuService menuService;

    @Before
    public void setUp() throws Exception {
        WxMpService wxService = new WxMpServiceImpl();
        wxService.setMaxRetryTimes(1); // 设置重试次数
        wxService.setRetrySleepMillis(2000);
        //微信配置参数
        WxMpInMemoryConfigStorage wxConfigProvider = new WxMpInMemoryConfigStorage();
//        wxConfigProvider.setAppId("wx60a8f1c3a95b0b9c");
//        wxConfigProvider.setSecret("5b0e8613b538da5ac4bbc610998f10ba");
        wxConfigProvider.setAppId("wxa7dcbbf769a34104");
        wxConfigProvider.setSecret("2ddd2c0efb0202eff77a1cbca8ef92a8");
        wxConfigProvider.setTmpDirFile(new File("D:\\tmp"));
        wxService.setWxMpConfigStorage(wxConfigProvider);

//        wxService.shortUrl()
        menuService = wxService.getMenuService();
    }

    @Test
    public void view() throws Exception {
        WxMpMenu wxMpMenu = menuService.menuGet();

    }

    /*
    1、自定义菜单最多包括3个一级菜单，每个一级菜单最多包含5个二级菜单。
    2、一级菜单最多4个汉字，二级菜单最多7个汉字，多出来的部分将会以“...”代替。
    3、创建自定义菜单后，菜单的刷新策略是，在用户进入公众号会话页或公众号profile页时，
        如果发现上一次拉取菜单的请求在5分钟以前，就会拉取一下菜单，如果菜单有更新，就会刷新客户端的菜单。
        测试时可以尝试取消关注公众账号后再次关注，则可以看到创建后的效果。
     */
    @Test
    public void createMenu() throws Exception {
        WxMenu wxMenu = new WxMenu();
        List<WxMenuButton> buttons = new ArrayList<>();

        {
            WxMenuButton button1 = new WxMenuButton();
            button1.setName("扫码");
            button1.setKey("click_func");
            button1.setType(WxConsts.MenuButtonType.CLICK);

            List<WxMenuButton> subButtons = new ArrayList<>();
            WxMenuButton sub3 = new WxMenuButton();
            sub3.setName("扫描二维码");
            sub3.setType(WxConsts.MenuButtonType.SCANCODE_PUSH);
            sub3.setKey("scan_01");
            subButtons.add(sub3);

            WxMenuButton sub4 = new WxMenuButton();
            sub4.setName("扫描二维码 wait");
            sub4.setType(WxConsts.MenuButtonType.SCANCODE_WAITMSG);
            sub4.setKey("scan_02");
            subButtons.add(sub4);

            button1.setSubButtons(subButtons);
            buttons.add(button1);
        }

        {
            WxMenuButton button3 = new WxMenuButton();
            button3.setName("图片");
            button3.setKey("click_03");
            button3.setType(WxConsts.MenuButtonType.CLICK);

            List<WxMenuButton> subButtons1 = new ArrayList<>();
            WxMenuButton sub2 = new WxMenuButton();
            sub2.setName("选择图片");
            sub2.setType(WxConsts.MenuButtonType.PIC_WEIXIN);
            sub2.setKey("select_pic");
            subButtons1.add(sub2);

            WxMenuButton sub5 = new WxMenuButton();
            sub5.setName("选择图片02");
            sub5.setType(WxConsts.MenuButtonType.PIC_SYSPHOTO);
            sub5.setKey("select_pic_02");
            subButtons1.add(sub5);

            WxMenuButton sub6 = new WxMenuButton();
            sub6.setName("选择图片03");
            sub6.setType(WxConsts.MenuButtonType.PIC_PHOTO_OR_ALBUM);
            sub6.setKey("select_pic_03");
            subButtons1.add(sub6);

            button3.setSubButtons(subButtons1);
            buttons.add(button3);
        }

        {
            WxMenuButton button4 = new WxMenuButton();
            button4.setName("其他");
            button4.setKey("click_04");
            button4.setType(WxConsts.MenuButtonType.CLICK);

            List<WxMenuButton> subButtons = new ArrayList<>();
            WxMenuButton button = new WxMenuButton();
            button.setName("跳转页面-这是一个超级超级长的标题");
            button.setType(WxConsts.MenuButtonType.VIEW);
            button.setUrl("https://github.com");
            subButtons.add(button);

            WxMenuButton sub1 = new WxMenuButton();
            sub1.setName("01事件触发");
            sub1.setType(WxConsts.MenuButtonType.CLICK);
            sub1.setKey("click_01");
            subButtons.add(sub1);

            WxMenuButton sub2 = new WxMenuButton();
            sub2.setName("发送位置");
            sub2.setType(WxConsts.MenuButtonType.LOCATION_SELECT);
            sub2.setKey("location");
            subButtons.add(sub2);

//            WxMenuButton sub3 = new WxMenuButton();
//            sub3.setName("小程序");
//            sub3.setType(WxConsts.MenuButtonType.MINIPROGRAM);
//            sub3.setUrl("http://mp.weixin.qq.com");
//            sub3.setAppId("wx286b93c14bbf93aa");
//            sub3.setPagePath("pages/lunar/index");
//            sub3.setKey("m_program");
//            subButtons.add(sub3);

            button4.setSubButtons(subButtons);
            buttons.add(button4);
        }


        wxMenu.setButtons(buttons);
        // 设置菜单
        menuService.menuCreate(wxMenu);
    }
}
