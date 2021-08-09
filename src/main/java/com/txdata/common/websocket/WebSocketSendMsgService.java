package com.txdata.common.websocket;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.txdata.common.redis.shiro.RedisSessionDAO;

@Service
public class WebSocketSendMsgService {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RedisSessionDAO sessionDao;
	
    public boolean sendMessageToUser(String userId, TextMessage message) {
//   		User user = UserUtils.get(userId);
   		Collection<Session> sessions = sessionDao.getActiveSessions(false, userId, null);
       try {
	        	for (Session session : sessions) {
	        		WebSocketSession socketSession = MyHandler.users.get(session.getId());
	        		if (socketSession != null && socketSession.isOpen()) {
	        			socketSession.sendMessage(message);
	        			logger.info("sendMessage:" + message);
	        		}
	        	}
       } catch (IOException e) {
           e.printStackTrace();
           return false;
       }
       return true;
   }
   
   /**
    * 广播信息（发送给所有人）
    * @param message
    * @return
    * 遍历出map中所有的websocket发送在线消息
    */
   public boolean sendMessageToAllUsers(TextMessage message) {
       boolean allSendSuccess = true;
       Set<String> userIds = MyHandler.users.keySet();
       for (String userId : userIds) {
       		sendMessageToUser(userId, message);
       }
       return  allSendSuccess;
   }
   
   public void closeConnection(String userId) {
	   WebSocketSession socketSession = MyHandler.users.get(userId);
	   if (socketSession != null && socketSession.isOpen()) {
		   String clientId = (String) socketSession.getAttributes().get("userId");
			MyHandler.users.remove(clientId);
			logger.info("websocket连接已关闭");
		}
   }
}
