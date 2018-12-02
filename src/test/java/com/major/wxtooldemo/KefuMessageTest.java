package com.major.wxtooldemo;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpKefuService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Created by Major on 2018/11/25
 */
public class KefuMessageTest {

    WxMpKefuService kefuService;

    @Before
    public void setUp() throws Exception {
        WxMpService wxService = new WxMpServiceImpl();
        wxService.setMaxRetryTimes(1); // 设置重试次数

        //微信配置参数
        WxMpInMemoryConfigStorage wxConfigProvider = new WxMpInMemoryConfigStorage();
//        wxConfigProvider.setAppId("wx60a8f1c3a95b0b9c");
//        wxConfigProvider.setSecret("5b0e8613b538da5ac4bbc610998f10ba");
        wxConfigProvider.setAppId("wxa7dcbbf769a34104");
        wxConfigProvider.setSecret("2ddd2c0efb0202eff77a1cbca8ef92a8");
        wxConfigProvider.setTmpDirFile(new File("D:\\tmp"));
        wxService.setWxMpConfigStorage(wxConfigProvider);

        kefuService = wxService.getKefuService();
    }

    @Test
    public void text() throws WxErrorException {
        String 小宝 = "o29QO1PNZQqkThD3YCWCTg_ps57Y";
        String toUser = "o29QO1Njm17XCbIXreF3aoYnXHXI";
        WxMpKefuMessage text = WxMpKefuMessage.TEXT()
                .toUser(小宝)
                .content("宝哥中午好，宝哥下午好 666")
                .build();
        kefuService.sendKefuMessage(text);
    }

    @Test
    public void image() throws WxErrorException {
        WxMpKefuMessage text = WxMpKefuMessage.IMAGE()
                .toUser("o29QO1Njm17XCbIXreF3aoYnXHXI")
                .mediaId("DsVLspx_6vLjC2djCQu3PetCqCQ-_o2k4EO7My-MWKM")
                .build();
        kefuService.sendKefuMessage(text);
    }

    @Test
    public void voice() throws WxErrorException {
        WxMpKefuMessage text = WxMpKefuMessage.VOICE()
                .toUser("o29QO1Njm17XCbIXreF3aoYnXHXI")
                .mediaId("BUofvv1vC7DvC4vbRRoO0u2fLhZdxcI0SONluJZcL86fUbpj2YZymmzsNr_Bw9Op")
                .build();
        kefuService.sendKefuMessage(text);
    }


    @Test
    public void news() throws WxErrorException {
        WxMpKefuMessage.WxArticle article1 = new WxMpKefuMessage.WxArticle();
        article1.setUrl("https://github.com/meijieman");
        article1.setPicUrl("https://avatars0.githubusercontent.com/u/19690896?s=400&u=60dc523cad0df4bd02b9e6b23a61550af4026877&v=4");
        article1.setDescription("Is Really A Happy Day");
        article1.setTitle("Happy Day");

        WxMpKefuMessage.WxArticle article2 = new WxMpKefuMessage.WxArticle();
        article2.setUrl("https://github.com/meijieman");
        article2.setPicUrl("https://avatars0.githubusercontent.com/u/19690896?s=400&u=60dc523cad0df4bd02b9e6b23a61550af4026877&v=4");
        article2.setDescription("Is Really A Happy Day");
        article2.setTitle("Happy Day");

        WxMpKefuMessage news = WxMpKefuMessage.NEWS()
                .toUser("o29QO1Njm17XCbIXreF3aoYnXHXI")
                .addArticle(article1)
//                .addArticle(article2)
                .build();
        kefuService.sendKefuMessage(news);

    }



}
