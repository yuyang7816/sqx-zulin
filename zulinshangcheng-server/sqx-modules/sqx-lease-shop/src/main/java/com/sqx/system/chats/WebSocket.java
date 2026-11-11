package com.sqx.system.chats;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.domain.repository.entity.chats.ChatsContent;
import com.sqx.domain.repository.service.chats.ChatsContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket聊天实现
 */
@Component
@ServerEndpoint("/websocket/{wxUserId}")
public class WebSocket {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //通过类似GET请求方式传递参数的方法（服务端采用第二种方法"WebSocketHandler"实现）
    //websocket = new WebSocket("ws://127.0.0.1:18080/testWebsocket?id=23&name=Lebron");
    /**
     * 在线人数
     */
    public static int onlineNumber = 0;
    /**
     * 以用户的id为key，WebSocket为对象保存起来
     */
    private static Map<String, WebSocket> clients = new ConcurrentHashMap<String, WebSocket>();
    /**
     * 会话
     */
    private Session session;
    /**
     * 用户id
     */
    private String wxUserId;

    /**
     * 商品id
     */
    private String goodId;

    //这里使用静态，让 service 属于类
    private static ChatsContentService chatContentService;

    //注入的时候，给类的 service 注入
    @Autowired
    public void setWxChatContentService(ChatsContentService chatContentService) {
        WebSocket.chatContentService = chatContentService;
    }


    /**
     * 建立连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("wxUserId") String wxUserId, Session session) {
        onlineNumber++;
        logger.info("现在来连接的客户id：" + wxUserId);
        this.wxUserId = wxUserId;
        this.session = session;
        // 获取查询参数中的 goodId
        Map<String, List<String>> queryParam = session.getRequestParameterMap();
        String goodId = queryParam.getOrDefault("goodId", Collections.singletonList(null)).get(0);

        this.goodId = (goodId != null && !goodId.isEmpty()) ? goodId : null;

        logger.info("有新连接加入！ 当前在线人数" + onlineNumber);
        try {
            //把自己的信息加入到map当中去
            clients.remove(wxUserId);
            clients.put(wxUserId, this);
            /*sendMessageTo("恭喜你连接成功！",wxUserId);*/
        } catch (Exception e) {
            logger.info(wxUserId + "上线的时候通知所有人发生了错误");
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.info("服务端发生了错误" + error.getMessage());
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        onlineNumber--;
        //webSockets.remove(this);
        clients.remove(wxUserId);
        logger.info("有连接关闭！ 当前在线人数" + onlineNumber);
    }

    /**
     * 接收客户端的消息，并把消息发送给所有连接的会话
     */
    @OnMessage
    public void onMessage(String message) {
        try {
            //解析聊天内容
            JSONObject jsonObject = JSON.parseObject(message);
            String type = jsonObject.getString("type");
            String content = jsonObject.getString("content");
            String sendType = jsonObject.getString("sendType");
            String userId = jsonObject.getString("userId");
            String storeId = jsonObject.getString("storeId");
            Long chatId = Long.valueOf(jsonObject.getString("chatId")); //会话id
            if ("1".equals(sendType)) {
                logger.info("用户发送消息：" + message);
            } else {
                logger.info("客服发送消息：" + message);
            }
            //聊天记录
            ChatsContent wxChatContent = new ChatsContent();
            wxChatContent.setContent(content);
            wxChatContent.setType(Integer.valueOf(type));
            wxChatContent.setSendType(Integer.valueOf(sendType));
            wxChatContent.setUserId(Long.valueOf(userId));
            if (storeId != null) {
                wxChatContent.setStoreId(Long.valueOf(storeId));
            }
            wxChatContent.setChatId(chatId);
            if (goodId != null) {
                wxChatContent.setGoodId(Long.valueOf(goodId));
            }
            wxChatContent.setCreateTime(DateUtils.createTime()); //创建时间
            wxChatContent.setStatus(1); //未读
            //判断对方是否在线，消息设为已读
            for (WebSocket item : clients.values()) {
                if ("1".equals(sendType)) { //用户发送
                    if (item.wxUserId.equals(storeId)) {
                        wxChatContent.setStatus(2); //已读
                        item.session.getAsyncRemote().sendText(message);
                    }
                } else {
                    if (item.wxUserId.equals(userId)) {
                        wxChatContent.setStatus(2); //已读
                        item.session.getAsyncRemote().sendText(message);
                    }
                }
            }
            //保存消息内容
            chatContentService.saveBody(wxChatContent);
        } catch (Exception e) {
            logger.info("发生了错误了");
        }
    }

    public void sendMessageTo(String message, String wxUserId) throws IOException {
        for (WebSocket item : clients.values()) {
            if (item.wxUserId.equals(wxUserId)) {
                item.session.getAsyncRemote().sendText(message);
                break;
            }
        }
    }

    public void sendMessageAll(String message, String FromUserName) throws IOException {
        for (WebSocket item : clients.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineNumber;
    }

}
