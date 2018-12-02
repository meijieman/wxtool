package com.major.wxtooldemo.service.impl;

import com.major.wxtooldemo.service.WXService;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.material.WxMpMaterial;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class WXServiceImpl implements WXService {

    private static Logger log = LoggerFactory.getLogger(WXServiceImpl.class);

    private final WxMpService wxService;

    public WXServiceImpl(){
        //配置微信参数  获取wxService
        wxService = new WxMpServiceImpl();
        //微信配置参数
        WxMpInMemoryConfigStorage wxConfigProvider = new WxMpInMemoryConfigStorage();
//        wxConfigProvider.setAppId("wx60a8f1c3a95b0b9c");
//        wxConfigProvider.setSecret("5b0e8613b538da5ac4bbc610998f10ba");
        wxConfigProvider.setAppId("wxa7dcbbf769a34104");
        wxConfigProvider.setSecret("2ddd2c0efb0202eff77a1cbca8ef92a8");
        wxService.setWxMpConfigStorage(wxConfigProvider);
    }

    @Override
    public WxMpMaterialUploadResult uploadFile(String path, String type) {
        File file = new File(path);
        WxMpMaterial material = new WxMpMaterial();
        material.setFile(file);
        material.setVideoTitle("一个demo视频"); // 视频素材需要设置 name
        material.setVideoIntroduction("视频的简单描述");
        try {
            WxMpMaterialUploadResult result = wxService.getMaterialService().materialFileUpload(type, material);
            log.info("uploadFile " + result);
            if (result.getErrCode() != null) {
                throw new RuntimeException("上传永久素材失败，返回 " + result.getErrMsg());
            }
            return result;
        } catch (WxErrorException e) {
            throw new RuntimeException("上传永久素材失败 " + e.getLocalizedMessage());
        }
    }

    @Override
    public WxMediaUploadResult uploadTmpFile(String path, String type){
        File file = new File(path);
        try {
            WxMediaUploadResult result = wxService.getMaterialService().mediaUpload(type, file);
            log.info("uploadTmpFile " + result);
            return result;
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new RuntimeException("上传临时素材失败 " + e.getLocalizedMessage());
        }
    }

}
