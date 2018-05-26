package cn.com.fhz.controller;

import cn.com.fhz.utils.SignatureUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by woni on 18/5/24.
 */
@RestController
public class WechatController {

    /*生成logger*/
    private static final Logger logger = LoggerFactory.getLogger(WechatController.class);

    /**
     * 微信的认证，验证域名是否可用
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    @RequestMapping("auth.do")
    public String weChatAuth(String signature,String timestamp,String nonce,String echostr){

        logger.info("signature\t"+signature);
        logger.info("timestamp\t"+timestamp);
        logger.info("nonce\t"+nonce);

        String token = "woshishui";

        String tmpStr = SignatureUtils.getSignature(token,timestamp,nonce);

        if (null!=tmpStr&&tmpStr.equals(signature.toUpperCase())){
            return echostr;
        }else return tmpStr;

    }



}
