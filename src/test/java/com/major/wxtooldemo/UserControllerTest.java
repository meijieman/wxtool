package com.major.wxtooldemo;

import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpUserService;
import me.chanjar.weixin.mp.api.WxMpUserTagService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import me.chanjar.weixin.mp.bean.tag.WxUserTag;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * Created by Major on 2018/11/25
 */
public class UserControllerTest {

    private static Logger log = LoggerFactory.getLogger(UserControllerTest.class);

    private WxMpUserService userService;
    private WxMpUserTagService userTagService;

    @Before
    public void setUp() throws Exception {
        WxMpService wxService = new WxMpServiceImpl();
        //微信配置参数
        WxMpInMemoryConfigStorage wxConfigProvider = new WxMpInMemoryConfigStorage();
//        wxConfigProvider.setAppId("wx60a8f1c3a95b0b9c");
//        wxConfigProvider.setSecret("5b0e8613b538da5ac4bbc610998f10ba");
        wxConfigProvider.setAppId("wxa7dcbbf769a34104");
        wxConfigProvider.setSecret("2ddd2c0efb0202eff77a1cbca8ef92a8");
        wxConfigProvider.setTmpDirFile(new File("D:\\tmp"));
        wxService.setWxMpConfigStorage(wxConfigProvider);

        userService = wxService.getUserService();
        userTagService = wxService.getUserTagService();
    }


    @Test
    public void list() throws Exception {
        // 获取帐号的关注者列表
        WxMpUserList list = userService.userList(null);
        log.info("", list);
    }
    @Test
    public void userUpdateRemark() throws Exception {
        String openId = "o29QO1Njm17XCbIXreF3aoYnXHXI";
        String 小宝 = "o29QO1PNZQqkThD3YCWCTg_ps57Y";
        userService.userUpdateRemark(小宝, "宝哥牛逼");

    }

    @Test
    public void getUser() throws Exception {
        String openId = "o29QO1Njm17XCbIXreF3aoYnXHXI";
        String 小宝 = "o29QO1PNZQqkThD3YCWCTg_ps57Y";

        WxMpUser user = userService.userInfo(小宝);
        log.info("{}", user);
    }

    @Test
    public void addTag() throws Exception {
        WxUserTag tag = userTagService.tagCreate("运动");

    }

    @Test
    public void tagList() throws Exception {
        List<WxUserTag> list = userTagService.tagGet();
    }
}
