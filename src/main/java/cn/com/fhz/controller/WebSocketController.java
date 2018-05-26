package cn.com.fhz.controller;

import cn.com.fhz.websocket.server.WebSocketServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by woni on 18/5/25.
 */
@RestController
public class WebSocketController {
    @RequestMapping(value="/pushMessage")
    public Map<String,Object> pushVideoListToWeb(String message,String param) {


        Map<String,Object> result =new HashMap<String,Object>();

        try {
            WebSocketServer.sendInfo("有新客户呼入,message:\t"+message,param);
            result.put("operationResult", true);
        }catch (IOException e) {
            result.put("operationResult", false);
        }
        return result;
    }
}
