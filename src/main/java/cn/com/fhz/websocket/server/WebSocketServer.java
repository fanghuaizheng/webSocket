package cn.com.fhz.websocket.server;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by woni on 18/5/25.
 */
@ServerEndpoint(value = "/websocket")
@Component
public class WebSocketServer {

    /*生成logger*/
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    //保存用户在线数
    private static AtomicLong onlineCount = new AtomicLong(0);

    private static ConcurrentHashMap<String,Set<WebSocketServer>> userWebSocketMap = new ConcurrentHashMap<>();


    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //每一个连接唯一主见
    private String id;


    @OnOpen
    public void onOpen(Session session){
        this.session = session;

        //将获取到的session放入到集合里面
        //逐渐规则如xia

        String queryString = session.getQueryString();

        if (StringUtils.isNotBlank(queryString)){
             id = StringUtils.split(queryString,"=")[1];

            if (userWebSocketMap.contains(id)){
                userWebSocketMap.get(id).add(this);
            }else {
                Set<WebSocketServer> socketServer = new HashSet<>();
                socketServer.add(this);
                userWebSocketMap.put(id,socketServer);
            }


        }
        onlineCount.incrementAndGet();

        logger.info("有新连接加入！当前在线人数为" + onlineCount);
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            logger.error("websocket IO异常");
        }

    }

    @OnClose
    public void onClose(){
//        webSocketSet.remove(this);
        Set<WebSocketServer> socketServers = userWebSocketMap.get(id);

        socketServers.remove(this);

        if (socketServers.size()==0){
            userWebSocketMap.remove(id);
        }


        onlineCount.decrementAndGet();
        logger.info("有一连接关闭！当前在线人数为" + onlineCount);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message,Session session){
        logger.info("来之客户端的消息\t"+message);

        Set<WebSocketServer> servers = userWebSocketMap.get(id);

        //向查询出来的客户端发送消息
        for (WebSocketServer item : servers) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("发生错误");
        error.printStackTrace();
    }


    //发送消息
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    //群发消息
    public static void sendInfo(String message)throws IOException{

        sendInfo(message,null);
    }

    /**
     *  向指定客户端发送信息，自定义消息
     * */
    public static void sendInfo(String message,String queryParam) throws IOException {
        logger.info(message);
        Set<WebSocketServer> data = new HashSet<>();
        if (queryParam!=null){
            data = userWebSocketMap.get(queryParam);
        }else {
            //要不然群发消息
            for (Set<WebSocketServer> item: userWebSocketMap.values()
                 ) {
                data.addAll(item);
            }
        }

        try {
            for (WebSocketServer item : data){

                item.sendMessage(message);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
