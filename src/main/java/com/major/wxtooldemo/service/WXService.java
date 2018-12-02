package com.major.wxtooldemo.service;

import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;

/**
 * 管理素材
 */
public interface WXService {

    WxMpMaterialUploadResult uploadFile(String path, String type);

    WxMediaUploadResult uploadTmpFile(String path, String type);

    // 图文上传

    // 查询所有素材

    // 删除素材

    // 获取素材

}
