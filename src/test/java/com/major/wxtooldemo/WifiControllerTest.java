package com.major.wxtooldemo;

import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpWifiService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.wifi.WxMpWifiShopListResult;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * 微信连 wifi
 * Created by Major on 2018/11/25
 */
public class WifiControllerTest {

    private WxMpWifiService wifiService;

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

        wifiService = wxService.getWifiService();

    }

    @Test
    public void method() throws Exception {
        WxMpWifiShopListResult result = wifiService.listShop(0, 100);
    }
}
