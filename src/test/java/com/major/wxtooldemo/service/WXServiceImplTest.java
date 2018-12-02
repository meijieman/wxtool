package com.major.wxtooldemo.service;

import com.major.wxtooldemo.service.impl.WXServiceImpl;
import com.major.wxtooldemo.util.CloseUtil;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMaterialService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.material.*;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by Major on 2018/11/25
 */
public class WXServiceImplTest {

    private static Logger log = LoggerFactory.getLogger(WXServiceImpl.class);
    private WxMpMaterialService materialService;

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

        materialService = wxService.getMaterialService();
    }

    @Test
    public void uploadImage() {
        String path = "D:\\素材\\Download\\pic1.jpg";
        String type = WxConsts.MaterialType.IMAGE;

        File file = new File(path);
        WxMpMaterial material = new WxMpMaterial();
        material.setFile(file);
        try {
            WxMpMaterialUploadResult result = materialService.materialFileUpload(type, material);
            log.info("uploadImage 上传永久素材 " + result);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void uploadVideo() throws Exception {
        String path = "D:\\素材\\抖音视频.mp4";
        String type = WxConsts.MaterialType.VIDEO;

        File file = new File(path);
        WxMpMaterial material = new WxMpMaterial();
        material.setFile(file);
        material.setVideoTitle("一个demo视频"); // 视频素材需要设置 name
        material.setVideoIntroduction("视频的简单描述");
        try {
            WxMpMaterialUploadResult result = materialService.materialFileUpload(type, material);
            log.info("uploadVideo 上传永久素材 " + result);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void uploadTmpImage() throws Exception {
        String path = "D:\\素材\\image\\z.jpg";
        String type = WxConsts.MediaFileType.IMAGE;

        File file = new File(path);
        try {
            WxMediaUploadResult result = materialService.mediaUpload(type, file);
            log.info("uploadTmpImage 上传临时素材 " + result);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }

    //    ================================
    @Test
    public void uploadTmpVideo() throws Exception {
        String path = "E:\\music\\The known Universe.mp4";
        String type = WxConsts.MediaFileType.VIDEO;

        File file = new File(path);
        try {
            WxMediaUploadResult result = materialService.mediaUpload(type, file);
            log.info("uploadTmpVideo 上传临时素材 " + result);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void uploadTmpVoice() throws Exception {
        String path = "D:\\tmp\\zzzC912sNafsMC28RuHps10exRtQ5AKdlCATGmJk5dizKAnVP0RpxJ3KNh4bEnigAZ73691932919452918539.amr";
        String type = WxConsts.MediaFileType.VOICE;

        File file = new File(path);
        try {
            WxMediaUploadResult result = materialService.mediaUpload(type, file);
            log.info("uploadTmpVoice 上传临时素材 " + result);
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new RuntimeException("上传临时素材失败 " + e.getLocalizedMessage());
        }
    }

    @Test
    public void uploadNews() throws Exception {
        // 单图文
        WxMpMaterialNews news = new WxMpMaterialNews();
        WxMpMaterialNews.WxMpMaterialNewsArticle article = new WxMpMaterialNews.WxMpMaterialNewsArticle();
        article.setContent("content");
        article.setAuthor("author");
        article.setThumbMediaId("DsVLspx_6vLjC2djCQu3PetCqCQ-_o2k4EO7My-MWKM");
        article.setTitle("single title");
        article.setContent("single content");
        article.setContentSourceUrl("content url");
        article.setShowCoverPic(true);
        article.setDigest("single news");

        news.addArticle(article);
        WxMpMaterialUploadResult result = materialService.materialNewsUpload(news);
        log.info("{}", result);
    }

    @Test
    public void downloadImage() throws WxErrorException, IOException {
        String mediaId = "DsVLspx_6vLjC2djCQu3PetCqCQ-_o2k4EO7My-MWKM"; // 图片
        InputStream is = materialService.materialImageOrVoiceDownload(mediaId);
        File file = new File("d://tmp", mediaId + ".jpg");
        FileOutputStream fos = new FileOutputStream(file);
        write(is, fos);
        log.info("文件路径{} , 存在{} ", file.getAbsolutePath(), file.exists());
    }

    @Test
    public void downloadVoice() throws WxErrorException, IOException {
        String mediaId = "C912sNafsMC28RuHps10exRtQ5AKdlCATGmJk5dizKAnVP0RpxJ3KNh4bEnigAZ7"; // 语音
        File file = materialService.mediaDownload(mediaId);
        if (file == null) {
            log.error("下载失败");
            return;
        }
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream("d://tmp/"  + mediaId+ ".amr");
        write(fis, fos);
        log.info("文件路径{} , 存在{} ", file.getAbsolutePath(), file.exists());
    }

    private void write(InputStream is, OutputStream os) throws IOException {
        if (is != null && os != null) {
            byte[] buff = new byte[1024 * 2];
            int len;
            while ((len = is.read(buff)) != -1) {
                os.write(buff, 0, len);
            }
            CloseUtil.close(is, os);
        }
    }

    @Test
    public void downloadVideo() throws WxErrorException {
        String mediaId = "DsVLspx_6vLjC2djCQu3PVgzM7IoEK0f00xOvyYImws";

        WxMpMaterialVideoInfoResult file = materialService.materialVideoInfo(mediaId);
        String downUrl = file.getDownUrl();
        log.info("视频路径 {}" , downUrl);
    }

    @Test
    public void count() throws WxErrorException {
        WxMpMaterialCountResult result = materialService.materialCount();
        log.info("result " + result);
    }

    @Test
    public void batch() throws Exception{
        // 素材的类型，图片（image）、视频（video）、语音 （voice）
        WxMpMaterialFileBatchGetResult result = materialService.materialFileBatchGet("voice", 0, 20);
        log.info("result " + result);
    }

    @Test
    public void batchNews() throws Exception {
        // 、图文（news）
        WxMpMaterialNewsBatchGetResult result = materialService.materialNewsBatchGet(0, 10);
        log.info("result " + result);
    }
}