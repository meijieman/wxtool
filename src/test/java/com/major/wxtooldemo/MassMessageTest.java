package com.major.wxtooldemo;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMassMessageService;
import me.chanjar.weixin.mp.api.WxMpMaterialService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.WxMpMassNews;
import me.chanjar.weixin.mp.bean.WxMpMassOpenIdsMessage;
import me.chanjar.weixin.mp.bean.material.WxMediaImgUploadResult;
import me.chanjar.weixin.mp.bean.result.WxMpMassSendResult;
import me.chanjar.weixin.mp.bean.result.WxMpMassUploadResult;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * 群发消息
 * Created by Major on 2018/11/25
 */
public class MassMessageTest {

    private WxMpMassMessageService massMessageService;
    private WxMpMaterialService materialService;

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

        massMessageService = wxService.getMassMessageService();
        materialService = wxService.getMaterialService();
    }

    @Test
    public void massTextMsg() throws Exception {
        WxMpMassOpenIdsMessage mass = new WxMpMassOpenIdsMessage();
        mass.setMsgType(WxConsts.MassMsgType.TEXT);
        mass.setContent("宝哥中午好，回复一下勒");
        List<String> toUsers = mass.getToUsers();
        String openId = "o29QO1Njm17XCbIXreF3aoYnXHXI";
        String 小宝 = "o29QO1PNZQqkThD3YCWCTg_ps57Y";
        toUsers.add(openId);
        toUsers.add(小宝);

        WxMpMassSendResult result = massMessageService.massOpenIdsMessageSend(mass);
    }

    @Test
    public void massImageMsg() throws Exception {
        WxMpMassOpenIdsMessage mass = new WxMpMassOpenIdsMessage();
        mass.setMsgType(WxConsts.MassMsgType.IMAGE);
        mass.setMediaId("DsVLspx_6vLjC2djCQu3PetCqCQ-_o2k4EO7My-MWKM");
        List<String> toUsers = mass.getToUsers();
        String openId = "o29QO1Njm17XCbIXreF3aoYnXHXI";
        String 小宝 = "o29QO1PNZQqkThD3YCWCTg_ps57Y";
        toUsers.add(openId);
        toUsers.add(小宝);
        // FIXME: 2018/11/25 没有权限

        WxMpMassSendResult massResult = massMessageService.massOpenIdsMessageSend(mass);
    }


    @Test
    public void massVoiceMsg() throws Exception {
        WxMpMassOpenIdsMessage mass = new WxMpMassOpenIdsMessage();
        mass.setMsgType(WxConsts.MassMsgType.VOICE);
        mass.setMediaId("BUofvv1vC7DvC4vbRRoO0u2fLhZdxcI0SONluJZcL86fUbpj2YZymmzsNr_Bw9Op");
        List<String> toUsers = mass.getToUsers();
        String openId = "o29QO1Njm17XCbIXreF3aoYnXHXI";
        String 小宝 = "o29QO1PNZQqkThD3YCWCTg_ps57Y";
        toUsers.add(openId);
        toUsers.add(小宝);
        // FIXME: 2018/11/25 没有权限

        WxMpMassSendResult massResult = massMessageService.massOpenIdsMessageSend(mass);
    }

    @Test
    public void massNewsMsg() throws Exception {
        // 上传图文消息的封面图片
        FileInputStream fis = new FileInputStream(new File("D:\\素材\\Download\\pic8.jpg"));
        WxMediaUploadResult uploadMediaRes = materialService.mediaUpload(WxConsts.MassMsgType.IMAGE, "jpg", fis);

        // 上传图文消息的正文图片(返回的url拼在正文的<img>标签中)
        File file = new File("D:\\素材\\Download\\pic9.jpg");
        WxMediaImgUploadResult imagedMediaRes = materialService.mediaImgUpload(file);
        String url = imagedMediaRes.getUrl();

        WxMpMassNews news = new WxMpMassNews();
        WxMpMassNews.WxMpMassNewsArticle article1 = new WxMpMassNews.WxMpMassNewsArticle();
        article1.setTitle("标题1");
        article1.setContent("内容1");
        article1.setThumbMediaId(uploadMediaRes.getMediaId());
        news.addArticle(article1);

//        WxMpMassNews.WxMpMassNewsArticle article2 = new WxMpMassNews.WxMpMassNewsArticle();
//        article2.setTitle("标题2");
//        article2.setContent("内容2");
//        article2.setThumbMediaId(uploadMediaRes.getMediaId());
//        article2.setShowCoverPic(true);
//        article2.setAuthor("作者2");
//        article2.setContentSourceUrl("www.baidu.com");
//        article2.setDigest("摘要2");
//        news.addArticle(article2);

        WxMpMassUploadResult massUploadResult =massMessageService.massNewsUpload(news);

        WxMpMassOpenIdsMessage massMessage = new WxMpMassOpenIdsMessage();
        massMessage.setMsgType(WxConsts.MassMsgType.MPNEWS);
        massMessage.setMediaId(uploadMediaRes.getMediaId());
        List<String> toUsers = massMessage.getToUsers();
        String openId = "o29QO1Njm17XCbIXreF3aoYnXHXI";
        String 小宝 = "o29QO1PNZQqkThD3YCWCTg_ps57Y";
        toUsers.add(openId);
        toUsers.add(小宝);
        // FIXME: 2018/11/25 没有权限

        WxMpMassSendResult massResult = massMessageService.massOpenIdsMessageSend(massMessage);
    }
}
