package com.txdata.common.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * 处理类：Handler--建立连接，发送消息，关闭连接自动执行
 * @author 01
 *
 */
public class MyHandler extends TextWebSocketHandler{
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	 //在线用户列表
    public static final Map<String, WebSocketSession> users;
    
    private static final String USER_ID = "userId"; //用户标识
    
	 //类加载初始化一个map集合，存放用户的websocket对象
	static{
		users = new HashMap<String, WebSocketSession>();
	}
	
	/**
     * 获取用户标识,获取websocekt对象的map集合
     * @param session
     * @return
     */
    private String getClientId(WebSocketSession session) {
        try {
        		//获取存入websocket的userid
        		String clientId = (String) session.getAttributes().get(USER_ID);
            return clientId;
        } catch (Exception e) {
            return null;
        }
    }
		
	/**
	 * 成功建立连接触发的方法，
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		//取出拦截器放入的dunID，为当前的websoket绑定用户到map
		String userId = getClientId(session);
		logger.info("获得shiroSessionId为：{}的websocket链接", userId);
        if (userId != null) {
            users.put(userId, session);
            session.sendMessage(new TextMessage("register"));
            logger.info("用户：{}成功建立socket连接", userId);
        }
    }
	
	/**
	 * 当接收到客户端浏览器后接收的方法
	 */
	@Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
		logger.info(message.getPayload());
        WebSocketMessage message1 = new TextMessage("server:"+message);
        try {
            session.sendMessage(message1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    /**
     * 当链接发生异常后触发的方法，关闭出错会话的连接，和删除在Map集合中的记录
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    		//判断当前的链接是否在继续，关闭连接
    		if (session.isOpen()) {
            session.close();
        }
    		logger.info("websocket连接出错");
        users.remove(getClientId(session));
    }
    
    /**
     * 当链接关闭后触发的方法，连接已关闭，移除在Map集合中的记录。
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    		logger.info("websocket连接已关闭：" + status); //当前的状态码，并删除存储在map中的websocket的链接对象
        users.remove(getClientId(session));
    }
    
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
