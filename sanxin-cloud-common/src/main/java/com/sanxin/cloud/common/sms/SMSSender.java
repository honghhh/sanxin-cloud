package com.sanxin.cloud.common.sms;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.sanxin.cloud.common.radoms.RandNumUtils;
import com.sanxin.cloud.common.regular.RpxUtils;
import com.sanxin.cloud.common.rest.RestResult;
import com.sanxin.cloud.config.redis.RedisUtilsService;
import com.sanxin.cloud.config.redis.SpringBeanFactoryUtils;
import com.sanxin.cloud.enums.RandNumType;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class SMSSender {

    private final NotificationMessagingTemplate notificationMessagingTemplate;

    @Autowired
    public SMSSender(AmazonSNS amazonSNS) {
        this.notificationMessagingTemplate = new NotificationMessagingTemplate(amazonSNS);
    }

    // https://cloud.spring.io/spring-cloud-aws/reference/html/#sns-support
    public void send(String subject, String message) {
        this.notificationMessagingTemplate.sendNotification("Message2U", message, subject);
    }

    // https://docs.aws.amazon.com/sns/latest/dg/sms_publish-to-phone.html#sms_publish_sdk
    public static void sendSMSMessage(String message,
                                      String phoneNumber) {
        AmazonSNSClient snsClient = new AmazonSNSClient();
        Map<String, MessageAttributeValue> smsAttributes = new HashMap<>();
        PublishResult result = snsClient.publish(new PublishRequest()
                .withMessage(message)
                .withPhoneNumber(phoneNumber)
                .withMessageAttributes(smsAttributes));
        System.out.println(result); // Prints the message ID.
    }

    //
    public static void sendSMSMessage2(String message, String phoneNumber) {
        AmazonSNS amazonSNS = AmazonSNSClientBuilder.standard().withRegion("ap-southeast-1").build();
        Map<String, MessageAttributeValue> attributes = new HashMap<>();
        Map<String, MessageAttributeValue> smsAttributes =
                new HashMap<String, MessageAttributeValue>();
//        smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue()
//                .withStringValue("mySenderID") //The sender ID shown on the device.
//                .withDataType("String"));
        smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue()
                .withStringValue("5.0") //Sets the max price to 0.50 USD.
                .withDataType("Number"));
        smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue()
                .withStringValue("Transactional") //Sets the type to promotional.
                .withDataType("String"));
        PublishResult result = amazonSNS.publish(new PublishRequest()
                .withMessage(message)
                .withPhoneNumber(phoneNumber)
                .withMessageAttributes(smsAttributes));
        System.out.println(result); // Prints the message ID.
    }


    public static void main(String[] args) {
        try {
            sendSMSDtac("Hello World", "66993456432");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static RestResult sendSms(String phone) {
        String validCode = RandNumUtils.getInstance().get(RandNumType.NUMBER, 6);
        //防止重复点击发送验证码
        String send_flag = "sendflag_sms_" + phone;
        boolean flag = getRedisUtilsService().setIncrSecond(send_flag, 60);
        if (!flag) {
            return RestResult.fail("sms_fail");
        }
        try {
            sendSMSDtac(validCode, phone);
        } catch (IOException e) {
            e.printStackTrace();
            return RestResult.fail("fail");
        }
        //保存验证码到缓存
        String key = "send_sms_" + phone;
        getRedisUtilsService().setKey(key, validCode, 300);
        return RestResult.success("");
    }


    /**
     * 校验验证码
     * @param phone
     * @param validcode
     * @return
     */
    public static RestResult validSms(String phone,String validcode){
        String validphone= RpxUtils.getInstance().valid_phone(phone);
        if(!StringUtils.isBlank(validphone)) {
            return RestResult.fail(validphone);
        }
        if(StringUtils.isEmpty(validcode)) {
            return RestResult.fail("sms_empty");
        }
        //保存验证码到缓存
        String key="send_sms_"+phone;
        String val=getRedisUtilsService().getKey(key);
        if(StringUtils.isEmpty(val)) {
            return RestResult.fail("sms_expired");
        }
        if(!val.equals(validcode)) {
            return RestResult.fail("sms_error");
        }
        //校验成功后删除
        getRedisUtilsService().deleteKey(key);
        return RestResult.success("sms_pass");
    }


    /**
     * 注入redis
     *
     * @return
     */
    private static RedisUtilsService getRedisUtilsService() {
        return SpringBeanFactoryUtils.getApplicationContext().getBean(RedisUtilsService.class);
    }


    /**
     * @param message
     * @param phoneNumber format 66xxxxxxxxx
     */
    public static void sendSMSDtac(String message, String phoneNumber) throws IOException {
        // https://dtacsmsapi.dtac.co.th/servlet/com.iess.socket.SmsCorplink
        // RefNo 1-15 digits // unique
        // Msn 668xxxxxxx
        // Msg
        // Encoding=25
        // MsgType=T
        // TimeStamp yymmddhh24miss
        // User
        // Password
        // Sender
        final String url = "https://dtacsmsapi.dtac.co.th/servlet/com.iess.socket.SmsCorplink"; // TODO: config or env var
        final String user = "api1618934"; // TODO: config or env var
        final String password = "llttqe8h"; // TODO: config or env var
        final String sender = "PowerPlusIT"; // TODO: config or env var

        String timeStamp = new SimpleDateFormat("yyMMddHHmmss").format(new Date());

        String refNo = timeStamp + String.valueOf(new Random().nextInt(999));

        String postData = "";
        postData += "&RefNo=" + refNo;
        postData += "&Sender=" + URLEncoder.encode(sender);
        postData += "&Msn=" + phoneNumber;
        postData += "&MsgType=H";
        postData += "&Msg=" + URLEncoder.encode(message);
        postData += "&Encoding=25";
        postData += "&TimeStamp=" + timeStamp;
        postData += "&User=" + URLEncoder.encode(user);
        postData += "&Password=" + URLEncoder.encode(password);
        System.out.println(postData);

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("RefNo", refNo));
        urlParameters.add(new BasicNameValuePair("Sender", sender));
        urlParameters.add(new BasicNameValuePair("Msn", phoneNumber));
        urlParameters.add(new BasicNameValuePair("MsgType", "T"));
        urlParameters.add(new BasicNameValuePair("Msg", message));
        urlParameters.add(new BasicNameValuePair("Encoding", "25"));
        urlParameters.add(new BasicNameValuePair("TimeStamp", timeStamp));
        urlParameters.add(new BasicNameValuePair("User", user));
        urlParameters.add(new BasicNameValuePair("Password", password));

        HttpPost post = new HttpPost(url);
        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            System.out.println(EntityUtils.toString(response.getEntity()));
        }

    }
}
