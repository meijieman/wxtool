package com.major.wxtooldemo.controller;

import com.major.wxtooldemo.util.CloseUtil;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.message.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 接收消息，分发消息
 * <p>
 * 管理微信服务器
 * 被动消息
 * 事件消息
 */
@RestController
public class MessageController {

    private static Logger log = LoggerFactory.getLogger(MessageController.class);

    // 校验 token，在公众号配置网页调用
    @GetMapping("/token")
    public void test(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("收到请求 " + request.getMethod() + " " + request.getRequestURL());

        //微信服务器get传递的参数
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        log.info("signature " + signature + ", timestamp " + timestamp + ", nonce " + nonce + ", echostr " + echostr);

        //微信工具类
        WxMpService wxService = new WxMpServiceImpl();
        //注入token的配置参数
        /*
         * 生产环境 建议将 WxMpInMemoryConfigStorage 持久化
         */
        WxMpInMemoryConfigStorage wxConfigProvider = new WxMpInMemoryConfigStorage();
        //注入token值
        wxConfigProvider.setToken("java_token");
        wxService.setWxMpConfigStorage(wxConfigProvider);

        boolean flag = wxService.checkSignature(timestamp, nonce, signature);
        PrintWriter out = response.getWriter();
        if (flag) {
            log.info("配置成功");
            out.print(echostr);
            out.flush();
        } else {
            log.error("配置失败");
        }
        CloseUtil.close(out);
    }

    /**
     * 接收普通消息
     * <p>
     * 文本消息
     * 图片消息
     * 语音消息
     * 视频消息
     * 小视频消息
     * 地理位置消息
     * 链接消息
     */
    @PostMapping("/token")
    public void received(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //消息的接受、处理、响应
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        WxMpXmlMessage msg = WxMpXmlMessage.fromXml(request.getInputStream());
        String msgType = msg.getMsgType();
        String fromUser = msg.getFromUser();
        String toUser = msg.getToUser();
        String content = msg.getContent();
        log.info("收到消息 msgType {}, msgId {}, content {}", msgType, msg.getMsgId(), content);
        log.warn("收到消息 " + msg);

        switch (msgType) {
            case WxConsts.XmlMsgType.TEXT: {
                // 收到文本消息
                dispatchTextMsg(response, fromUser, toUser, content);
                break;
            }
            case WxConsts.XmlMsgType.IMAGE: {
                // 收到图片消息

                replyText(response, fromUser, toUser, "这张图片真好看");
                break;
            }
            case WxConsts.XmlMsgType.VIDEO: {
                // 收到视频消息
                String video = WxMpXmlOutMessage.VIDEO()
                        .toUser(fromUser)
                        .fromUser(toUser)
                        .description("视频消息")
                        .mediaId(msg.getMediaId())
                        .title("想不到吧")
                        .build()
                        .toXml();
                reply(response, video);
                break;
            }
            case WxConsts.XmlMsgType.SHORTVIDEO: {
                // 收到小视频消息

                break;
            }
            case WxConsts.XmlMsgType.VOICE: {
                // 收到语音消息

                String voice = WxMpXmlOutMessage.VOICE()
                        .toUser(fromUser)
                        .fromUser(toUser)
                        .mediaId(msg.getMediaId())
                        .build()
                        .toXml();
                reply(response, voice);
                break;
            }
            case WxConsts.XmlMsgType.LOCATION: {
                // 收到位置消息

                String text = WxMpXmlOutMessage.TEXT()
                        .toUser(fromUser)
                        .fromUser(toUser)
                        .content("地理位置 " + msg.getLocationX() + ", " + msg.getLocationY() + ", " + msg.getLabel())
                        .build()
                        .toXml();
                reply(response, text);
                break;
            }
            case WxConsts.XmlMsgType.LINK: {
                // 收到超链接
                replyText(response, fromUser, toUser, "欢迎访问<a href=\"https://github.com/meijieman\">我的博客</a>!");
                break;
            }
            case WxConsts.XmlMsgType.EVENT: {
                String event = msg.getEvent();
                switch (event) {
                    case WxConsts.EventType.SUBSCRIBE: {
                        log.info("订阅");

                        break;
                    }
                    case WxConsts.EventType.UNSUBSCRIBE: {
                        log.error("取消订阅");

                        break;
                    }
                    case WxConsts.EventType.CLICK:
                    case WxConsts.EventType.VIEW: {
                        String eventKey = msg.getEventKey();
                        log.info("自定义菜单消息处理 " + eventKey);
                        if ("click_01".equals(eventKey)) {
                            replyText(response, fromUser, toUser, "我知道你点啦");
                        }
                        break;
                    }
                    case WxConsts.EventType.SCAN: {
                        log.info("SCAN " + msg.getScanCodeInfo());

                        break;
                    }
                    case WxConsts.EventType.SCANCODE_PUSH: {
                        ScanCodeInfo info = msg.getScanCodeInfo();
                        log.info("SCANCODE_PUSH " + info);

                        // FIXME: 2018/11/25 不能回复消息？
                        replyText(response, fromUser, toUser, "【扫码内容】" + info.getScanResult() + ", " + info.getScanType());
                        break;
                    }
                    case WxConsts.EventType.SCANCODE_WAITMSG: {
                        ScanCodeInfo info = msg.getScanCodeInfo();
                        log.info("SCANCODE_WAITMSG " + info);

                        replyText(response, fromUser, toUser, "【扫码内容】" + info.getScanResult() + ", " + info.getScanType());
                        break;
                    }
                    case WxConsts.EventType.PIC_PHOTO_OR_ALBUM:
                    case WxConsts.EventType.PIC_SYSPHOTO:
                    case WxConsts.EventType.PIC_WEIXIN: {
                        log.info("PIC " + msg.getSendPicsInfo());
                        break;
                    }
                    default:
                        log.error("其他事件 eventType " + event);
                        break;
                }
                break;
            }
            default:
                replyMenu(response, fromUser, toUser);
                break;
        }
    }

    private void dispatchTextMsg(HttpServletResponse response, String fromUser, String toUser, String content) throws IOException {
        if ("1".equals(content)) {
            // 回复文本
            WxMpXmlOutTextMessage text = WxMpXmlOutTextMessage.TEXT()
                    .toUser(fromUser)
                    .fromUser(toUser)
                    .content("收到消息【" + content + "】")
                    .build();
            reply(response, text.toXml());
        } else if ("2".equals(content)) {
            // 回复图片
            String mediaId = "DsVLspx_6vLjC2djCQu3PetCqCQ-_o2k4EO7My-MWKM";
            WxMpXmlOutImageMessage image = WxMpXmlOutImageMessage.IMAGE()
                    .toUser(fromUser)
                    .fromUser(toUser)
                    .mediaId(mediaId)
                    .build();
            reply(response, image.toXml());
        } else if ("3".equals(content)) {
            // 回复音乐
            // FIXME: 2018/11/25
            String music = WxMpXmlOutMessage.MUSIC()
                    .toUser(fromUser)
                    .fromUser(toUser)
                    .thumbMediaId("")
                    .musicUrl("")
                    .hqMusicUrl("")
                    .title("")
                    .description("")
                    .build()
                    .toXml();
            reply(response, music);
        } else if ("4".equals(content)) {
            // 回复视频
            String mediaId = "DsVLspx_6vLjC2djCQu3PVgzM7IoEK0f00xOvyYImws";
            String video = WxMpXmlOutMessage.VIDEO()
                    .toUser(fromUser)
                    .fromUser(toUser)
                    .mediaId(mediaId)
                    .description("视频描述")
                    .title("视频标题")
                    .build()
                    .toXml();

            reply(response, video);
        } else if ("5".equals(content)) {
            // 回复语音
            String mediaId = "BUofvv1vC7DvC4vbRRoO0u2fLhZdxcI0SONluJZcL86fUbpj2YZymmzsNr_Bw9Op";
            String voice = WxMpXmlOutMessage.VOICE()
                    .toUser(fromUser)
                    .fromUser(toUser)
                    .mediaId(mediaId)
                    .build()
                    .toXml();
            reply(response, voice);
        } else if ("6".equals(content)) {
            // 回复图文消息
            WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
            item.setDescription("要不要 star 一下！");
            item.setPicUrl("https://avatars0.githubusercontent.com/u/19690896?s=400&u=60dc523cad0df4bd02b9e6b23a61550af4026877&v=4");
            item.setTitle("个人微博主页");
            item.setUrl("https://github.com/meijieman");

            String news = WxMpXmlOutMessage.NEWS()
                    .fromUser(toUser)
                    .toUser(fromUser)
                    .addArticle(item)
                    .build()
                    .toXml();
            reply(response, news);
        } else {
            replyMenu(response, fromUser, toUser);
        }
    }

    private void replyMenu(HttpServletResponse response, String fromUser, String toUser) throws IOException {
        String str = "1. 文本消息\r\n" +
                "2. 图片消息\n" +
                "3. 音乐消息\n" +
                "4. 视频消息\n" +
                "5. 语音消息\n" +
                "6. 图文消息\n";
        replyText(response, fromUser, toUser, str);


    }

    private void replyText(HttpServletResponse response, String fromUser, String toUser, String content) throws IOException {
        WxMpXmlOutTextMessage text = WxMpXmlOutTextMessage.TEXT()
                .fromUser(toUser)
                .toUser(fromUser)
                .content(content)
                .build();

        reply(response, text.toXml());
    }

    private void reply(HttpServletResponse response, @NonNull String text) throws IOException {
        log.info("回复内容 " + text);
        PrintWriter writer = response.getWriter();
        writer.write(text);
        CloseUtil.close(writer);
    }
}
