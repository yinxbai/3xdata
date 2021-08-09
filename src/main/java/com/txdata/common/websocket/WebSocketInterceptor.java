package com.txdata.common.websocket;

import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
 
/**
 * WebSocket链接时的拦截器
 * @author 01
 */
public class WebSocketInterceptor extends HttpSessionHandshakeInterceptor {
	
	/**
	 * 当客户端与服务器端握手之前之前执行的方法
	 * 取出当前存在session的用户信息将dunId，封装到WebSocket对象中的map中；
	 * 由Handler处理器中获取id
	 * @return
	 */
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler,
			Map<String, Object> attribute) throws Exception {
			//将增强的request转换httpservletRequest	
		  if (request instanceof ServletServerHttpRequest) {
	            ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
	            HttpSession session = serverHttpRequest.getServletRequest().getSession(false);
	            if (session != null) {
	            		String shiroSessionId = (String) session.getAttribute("SHIRO_SESSION_ID");
//		            	String userId = serverHttpRequest.getServletRequest().getParameter("userId"); 
		            	if (shiroSessionId == null) {
		            		shiroSessionId = "default-system";
		            	}
		            	attribute.put("userId", shiroSessionId);
	            }
	        }
		  	//放行
	        return true;
	}
		
	/**
	 * 与服务器websoket建立握手之后执行的方法
	 */
	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler, Exception exception) {
		super.afterHandshake(request, response, handler, exception);
	}
 
}
