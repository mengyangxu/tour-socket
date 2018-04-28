package com.xmy.socket.socket;

import com.google.gson.Gson;
import com.xmy.socket.utils.HttpClientUtil;
import com.xmy.socket.utils.UrlStatic;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/** 
 * @ServerEndpoint 
 */
@ServerEndpoint(value="/websocketTest/{userId}"/*,configurator = SpringConfigurator.class*/)
@Component
public class WebSocketController {

    private static ApplicationContext applicationContext;
    public static void setApplicationContext(ApplicationContext context){
        applicationContext = context;
    }

    private static int onlineCount = 0;  
    //存放所有登录用户的Map集合，键：每个用户的唯一标识（用户名）
    public String name;
    private static Map<String, WebSocketController> webSocketMap = new HashMap<String, WebSocketController>();
    //session作为用户简历连接的唯一会话，可以用来区别每个用户  
    private Session session;
    //httpsession用以在建立连接的时候获取登录用户的唯一标识（登录名）,获取到之后以键值对的方式存在Map对象里面  
    /*private static HttpSession httpSession;
      
    public static void setHttpSession(HttpSession httpSession){
        WebSocketTest.httpSession=httpSession;  
    }*/


    /**
     * 连接建立成功调用的方法 
     * @param session 
     * 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据 
     */  
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        Gson gson=new Gson();
        this.session = session;
        //User user = (User)httpSession.getAttribute("user");
        //this.name = user.getId().toString();
        //this.name = httpSession.getAttribute("username").toString();
        this.name = userId;
        webSocketMap.put(this.name, this);
        addOnlineCount();
        MessageDto md=new MessageDto();
        md.setMessageType("onlineCount");
        md.setData(onlineCount+"");
        sendOnlineCount(gson.toJson(md));
        System.out.println(getOnlineCount());
    }  
    /** 
     * 向所有在线用户发送在线人数 
     * @param message 
     */  
    public void sendOnlineCount(String message){  
        for (Entry<String, WebSocketController> entry  : webSocketMap.entrySet()) {
            try {  
                entry.getValue().sendMessage(message);  
            } catch (IOException e) {  
                continue;  
            }  
        }  
    }  
      
    /** 
     * 连接关闭调用的方法 
     */  
    @OnClose
    public void onClose() {  
        for (Entry<String, WebSocketController> entry  : webSocketMap.entrySet()) {
            if(entry.getValue().session==this.session){  
                webSocketMap.remove(entry.getKey());  
                break;  
            }  
        }  
        //webSocketMap.remove(httpSession.getAttribute("username"));  
        subOnlineCount(); //   
        System.out.println(getOnlineCount());  
    }  
  
    /** 
     * 服务器接收到客户端消息时调用的方法，（通过“@”截取接收用户的用户名） 
     *  
     * @param message 
     *            客户端发送过来的消息 
     * @param session 
     *            数据源客户端的session 
     */  
    @OnMessage
    public void onMessage(String message, Session session) {
        Gson gson=new Gson();  
        System.out.println("收到客户端的消息:" + message);
        StringBuffer messageStr=new StringBuffer(message);

        if(messageStr.indexOf("@")!=-1){
            String targetname=messageStr.substring(0, messageStr.indexOf("@"));
            String content = message.substring(messageStr.indexOf("@")+1);
            String sourcename="";
            int flag = 0;
            for (Entry<String, WebSocketController> entry  : webSocketMap.entrySet()) {
                //根据接收用户名遍历出接收对象
                if(targetname.equals(entry.getKey())){
                    try {
                        /*for (Entry<String,WebSocketTest> entry1  : webSocketMap.entrySet()) {
                            //session在这里作为客户端向服务器发送信息的会话，用来遍历出信息来源
                            if(entry1.getValue().session==session){
                                sourcename=entry1.getKey();
                            }
                        }*/
                        MessageDto md=new MessageDto();
                        md.setMessageType("message");
                        md.setData(content);
                        md.setFromId(name);
                        entry.getValue().sendMessage(gson.toJson(md));
                        flag = 1;

                        Map<String,String> map = new HashMap<>();
                        map.put("fromId",name);
                        map.put("toId",targetname);
                        map.put("content",content);
                        map.put("state","1");
                        try {
                            HttpClientUtil.getInstance().doPostJsonRequestByContentType(UrlStatic.serviceUrl+"saveChatLog",map,"");
                        }catch (Exception e){
                            e.printStackTrace();
                            continue;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        continue;
                    }
                }

            }
            if(0==flag){
                Map<String,String> map = new HashMap<>();
                map.put("fromId",name);
                map.put("toId",targetname);
                map.put("content",content);
                map.put("state","1");
                try {
                    HttpClientUtil.getInstance().doPostJsonRequestByContentType(UrlStatic.serviceUrl+"saveChatLog",map,"");
                }catch (Exception e){
                    e.printStackTrace();

                }
            }



        }  
          
    }  
  
    /** 
     * 发生错误时调用 
     *  
     * @param session 
     * @param error 
     */  
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();  
    }  
  
    /** 
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。 
     *  
     * @param message 
     * @throws IOException 
     */  
    public void sendMessage(String message) throws IOException {  
        this.session.getBasicRemote().sendText(message);  
        // this.session.getAsyncRemote().sendText(message);  
    }  
  
    public static synchronized int getOnlineCount() {  
        return onlineCount;  
    }  
  
    public static synchronized void addOnlineCount() {  
        WebSocketController.onlineCount++;
    }  
  
    public static synchronized void subOnlineCount() {  
        WebSocketController.onlineCount--;
    }

}