package cn.com.fhz.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Created by woni on 18/5/17.
 * http请求工具类
 *
 */
public class HttpUtils {

    /*生成logger*/
    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private static final String APPLICATION_JSON="application/json;charset=UTF-8";

    private static final String UTF_8="UTF-8";

    /**
     *
     * get请求
     * @param params
     * @param url
     * @return
     */
    public static String doGet(Map<String,Object> params, String url){

        if (params!=null){
            url = url+"?"+concatParam(params);
        }

         return doGet(url);


    }

    public static String doGet(String url){
        CloseableHttpClient client = getHttpClient();

        HttpUriRequest httpGet = new HttpGet(url);

        String result = null;

        try {
            CloseableHttpResponse response = client.execute(httpGet);

            HttpEntity entity = response.getEntity();

            if (response!=null){
                result = EntityUtils.toString(entity,"utf-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            closeHttpClient(client);
            return result;
        }
    }

    /**
     * 发送post请求，將参数放在formDATA里面
     * @param url
     * @param params
     * @return
     */
    public static String doPostByFormData(String url, Map<String,Object> params){

        HttpPost post = new HttpPost(url);

        String result = null;

        try {

            List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
            if (params!=null){
                for (Map.Entry<String,Object> entry: params.entrySet()
                     ) {
                    pairList.add(new BasicNameValuePair(entry.getKey(),entry.getValue().toString()));
                }
            }
            post.setEntity(new UrlEncodedFormEntity(pairList,UTF_8));
            result =  executeRequest(post);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return result;
        }

    }


    /**
     * 发送post请求
     * @param url
     * @param params
     * @return
     */
    public static String doPost(String url, String params) {
        CloseableHttpClient client = getHttpClient();
        HttpPost httpPost= new HttpPost(url);
        String result  = null;

        try {
            httpPost.setEntity(new ByteArrayEntity(params.getBytes()));
//            httpPost.setHeader(HTTP.CONTENT_TYPE,APPLICATION_JSON);
            CloseableHttpResponse response = client.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if(entity!=null){
                result = EntityUtils.toString(entity,"utf-8");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeHttpClient(client);
            return result;
        }
    }

    private static String executeRequest(HttpRequestBase request){
        CloseableHttpClient client = getHttpClient();

        String result = null;

        CloseableHttpResponse response = null;

        try {
             response = client.execute(request);

            HttpEntity entity = response.getEntity();

            if (null!=entity&&response.getStatusLine().getStatusCode()==200){
                result = EntityUtils.toString(entity);
            }
            response.close();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeHttpClient(client);

            return result;
        }
    }

    /**
     * 將参数拼接成get请求模式
     * @param params
     * @return
     */
    private static String concatParam(Map<String,Object> params){

        String paramStr = "";

        Set<Map.Entry<String, Object>> entrySet = params.entrySet();

        Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();

        while (iterator.hasNext()){
            Map.Entry<String, Object> entry = iterator.next();
            paramStr.concat(entry.getKey()).concat("=").concat(entry.getValue().toString());
            if (iterator.hasNext()){
                paramStr.concat("&");
            }
        }

        return paramStr;

    }

    /**
     * 获取客户端连接
     * @return
     */
    public static CloseableHttpClient getHttpClient(){
        return HttpClientBuilder.create().build();
    }


    /**
     * 关闭客户端
     * @param httpClient
     */
    public static void closeHttpClient(CloseableHttpClient httpClient){
        try {
            if (httpClient!=null){
                httpClient.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /***
     * 获取微信的acess_token
     * 来源www.vxzsk.com
     * @return
     */
    public static String getAccessToken(){
        String appid="wxef34960fe522e3e8";//应用ID
        String appSecret="69e5b9f02d8ab6a103c0194173645d51";//(应用密钥)
        String url ="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+appSecret+"";
//        String backData=(url, "utf-8", 10000);
        String result = doGet(url);
        System.out.println("返回的结果是：\t"+result);
        String accessToken = JSONObject.parseObject(result).getString("access_token");
        return accessToken;
    }

    public static String getJsapiTicket(String accessToken){
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+accessToken+"&type=jsapi";

        String result = doGet(url);
        JSONObject resultData = JSONObject.parseObject(result);
        String jsapiTicket = null;
        if (0==resultData.getInteger("errcode")){
            jsapiTicket = resultData.getString("ticket");

        }

        return jsapiTicket;


    }


    public static void main(String[] args) throws Exception {
//        String fileType = "1";
//        String imageName="";
//        String imgUrl = "http://runonce.oss-cn-hangzhou.aliyuncs.com/upload/jpg/20180516/ab1b9b7c-cddb-4cd4-a49f-7361d0a1cf66.jpg";
//        String image = ImageUtils.getBreviaryImageStrFromUrl(imgUrl);
//        String image = ImageUtils.getImageBase64FromUrl(imgUrl);
//        String url = "http://hzycsl.zjzwfw.gov.cn/sqdj/material/uploadFile2.do";
//        String url = "http://localhost:9090/sqdj/bsx/common/submit.do";
//        String url = "http://localhost:9090/sqdj/material/uploadFile2.do";
//
//        JSONObject param = new JSONObject();

//
//        image.replace("\n|\t|\r","");

//        param.put("fileType",fileType);
//
//        param.put("imageName","1.jpg");
//
//        param.put("image",image);

//        param.put("formData","%7B%22basicFormData%22%3A%22%7B%5C%22ZP%5C%22%3A%5C%22%5C%22%2C%5C%22ZPUuid%5C%22%3A%5C%22%5C%22%2C%5C%22CONTEXTPATH%5C%22%3A%5C%22%2Fyzt%5C%22%2C%5C%22receiveDeptName%5C%22%3A%5C%22%E8%8C%B6%E4%BA%AD%E8%8B%91%E7%A4%BE%E5%8C%BA%5C%22%2C%5C%22receiveDeptCode%5C%22%3A%5C%2233011303800790180000%5C%22%2C%5C%22applyCardNumber%5C%22%3A%5C%22340823199002195612%5C%22%2C%5C%22applyName%5C%22%3A%5C%22cshi%5C%22%2C%5C%22age%5C%22%3A%5C%2228%5C%22%2C%5C%22ydzlbTxt%5C%22%3A%5C%22%E7%BA%A2%E6%9C%AC%5C%22%2C%5C%22ydzlb%5C%22%3A%5C%222%5C%22%2C%5C%22mobile%5C%22%3A%5C%22%5C%22%2C%5C%22applyTypeCode%5C%22%3A%5C%221%5C%22%2C%5C%22applyCardTypeCode%5C%22%3A%5C%2201001%5C%22%2C%5C%22formAttrName%5C%22%3A%5C%22jgqlnrydzbl%5C%22%2C%5C%22projPwd%5C%22%3A%5C%22%5C%22%2C%5C%22ocrCompareResult%5C%22%3A%7B%5C%22items%5C%22%3A%5B%5D%2C%5C%22ocrrate%5C%22%3A%5C%22%5C%22%7D%7D%22%2C%22applyForms%22%3A%22%5B%7B%5C%22formCode%5C%22%3A%5C%22FORM20180309005%5C%22%2C%5C%22formVersion%5C%22%3A%5C%220.1%5C%22%2C%5C%22formSign%5C%22%3A%5C%22FORM20180309005-0.1%5C%22%2C%5C%22signGuid%5C%22%3A%5C%22FORM20180309005-zyd3l%5C%22%2C%5C%22formName%5C%22%3A%5C%22%E7%94%B3%E8%AF%B7%E4%BA%BA%E5%9F%BA%E6%9C%AC%E4%BF%A1%E6%81%AF%5C%22%2C%5C%22desc%5C%22%3A%5C%22%E7%94%B3%E8%AF%B7%E4%BA%BA%E5%9F%BA%E6%9C%AC%E4%BF%A1%E6%81%AF%5C%22%2C%5C%22sortOrder%5C%22%3A%5C%221%5C%22%2C%5C%22formTypeCode%5C%22%3A%5C%221%5C%22%2C%5C%22itemValuesJson%5C%22%3A%5C%22%7B%5C%5C%5C%22ZP%5C%5C%5C%22%3A%5C%5C%5C%22%5C%5C%5C%22%2C%5C%5C%5C%22ZPUuid%5C%5C%5C%22%3A%5C%5C%5C%22%5C%5C%5C%22%2C%5C%5C%5C%22CONTEXTPATH%5C%5C%5C%22%3A%5C%5C%5C%22%2Fyzt%5C%5C%5C%22%2C%5C%5C%5C%22receiveDeptName%5C%5C%5C%22%3A%5C%5C%5C%22%E8%8C%B6%E4%BA%AD%E8%8B%91%E7%A4%BE%E5%8C%BA%5C%5C%5C%22%2C%5C%5C%5C%22receiveDeptCode%5C%5C%5C%22%3A%5C%5C%5C%2233011303800790180000%5C%5C%5C%22%2C%5C%5C%5C%22applyCardNumber%5C%5C%5C%22%3A%5C%5C%5C%22340823199002195612%5C%5C%5C%22%2C%5C%5C%5C%22applyName%5C%5C%5C%22%3A%5C%5C%5C%22cshi%5C%5C%5C%22%2C%5C%5C%5C%22age%5C%5C%5C%22%3A%5C%5C%5C%2228%5C%5C%5C%22%2C%5C%5C%5C%22ydzlbTxt%5C%5C%5C%22%3A%5C%5C%5C%22%E7%BA%A2%E6%9C%AC%5C%5C%5C%22%2C%5C%5C%5C%22ydzlb%5C%5C%5C%22%3A%5C%5C%5C%222%5C%5C%5C%22%2C%5C%5C%5C%22mobile%5C%5C%5C%22%3A%5C%5C%5C%22%5C%5C%5C%22%2C%5C%5C%5C%22applyTypeCode%5C%5C%5C%22%3A%5C%5C%5C%221%5C%5C%5C%22%2C%5C%5C%5C%22applyCardTypeCode%5C%5C%5C%22%3A%5C%5C%5C%2201001%5C%5C%5C%22%2C%5C%5C%5C%22formAttrName%5C%5C%5C%22%3A%5C%5C%5C%22jgqlnrydzbl%5C%5C%5C%22%2C%5C%5C%5C%22projPwd%5C%5C%5C%22%3A%5C%5C%5C%22%5C%5C%5C%22%2C%5C%5C%5C%22ocrCompareResult%5C%5C%5C%22%3A%7B%5C%5C%5C%22items%5C%5C%5C%22%3A%5B%5D%2C%5C%5C%5C%22ocrrate%5C%5C%5C%22%3A%5C%5C%5C%22%5C%5C%5C%22%7D%7D%5C%22%2C%5C%22itemValue%5C%22%3A%7B%7D%7D%5D%22%7D");
//        param.put("credData","%5B%7B%22attrCode%22%3A%22%22%2C%22attrFiles%22%3A%5B%5D%2C%22blankTableName%22%3A%22%22%2C%22byxms%22%3A%22%22%2C%22bz%22%3A%22%22%2C%22clbh%22%3A%22a9a954d4-81e5-48a1-a0df-e6a07cfbb2c0%22%2C%22clbyx%22%3A%222%22%2C%22clfz%22%3A%220%22%2C%22clkVersion%22%3A%221.0%22%2C%22clmc%22%3A%22%E6%9C%AC%E4%BA%BA%E8%BA%AB%E4%BB%BD%E8%AF%81%E6%88%96%E5%85%B6%E4%BB%96%E6%9C%89%E6%95%88%E8%BA%AB%E4%BB%BD%E8%AF%81%E6%98%8E%22%2C%22clmlCode%22%3A%22CL000000%22%2C%22clmlName%22%3A%22%E5%85%B6%E5%AE%83%E6%9D%90%E6%96%99%22%2C%22clsqxs%22%3A%22001%2C003%22%2C%22clsqzt%22%3A%22%22%2C%22cltj%22%3A%22%22%2C%22condition%22%3A%22%22%2C%22demoTextName%22%3A%22%22%2C%22flowPhaseId%22%3A%22%22%2C%22fyjbz%22%3A%22%22%2C%22fyjbzList%22%3A%5B%5D%2C%22id%22%3A%22%22%2C%22istake%22%3A%220%22%2C%22kbbg%22%3A%22%22%2C%22linkCode%22%3A%22%22%2C%22linkName%22%3A%22%22%2C%22ocrCompareMapping%22%3A%22%22%2C%22ownerCode%22%3A%22%22%2C%22ownerIdcode%22%3A%22340823199002195612%22%2C%22ownerName%22%3A%22cshi%22%2C%22projNo%22%3A%22%22%2C%22selectedMeterials%22%3A%22%5B%5D%22%2C%22sfwb%22%3A%22%22%2C%22shareName%22%3A%22%22%2C%22sortOrder%22%3A%221%22%2C%22state%22%3A%22%22%2C%22xxyq%22%3A%22%22%2C%22yjbz%22%3A%22%22%2C%22yjbzList%22%3A%5B%5D%2C%22%24cred%22%3A%22%22%7D%5D");
//
//        param.put("projNo","");
//        param.put("serviceId","33011303230100000000-SP-0001");
//        param.put("serviceCode","");
//        param.put("serviceVersion","1.0");
//        param.put("applyName","cshi");
//        param.put("applyCardNumber","340823199002195612");
//        param.put("operateFlag","1");
//        param.put("registerFlag","");
//        param.put("messageRequest","%7B%22nextPhaseNotify%22%3A%221%22%2C%22applyerNotify%22%3A%221%2C2%22%7D");

//
//        String paramStr = JSONObject.toJSONString(param);

//        AttachmentDto attachmentDto = new AttachmentDto();
//
//        attachmentDto.setCreateUser("sqyh1");
//        attachmentDto.setImage(image);

//
//        String param = "{\"createUser\":\"sqyh1\"," + "\"image\":\"" + image
//                + "\",\"fileName\":\"" + imageName
//                + "\",\"fileType\":\"" + "" + "\"}";
//        String json = "{\"attachmentDto\":" + param + "}";

//        String result = doPostByFormData(url,param);
//
//        System.out.println(result);

        String access_token = getAccessToken();








    }

}
