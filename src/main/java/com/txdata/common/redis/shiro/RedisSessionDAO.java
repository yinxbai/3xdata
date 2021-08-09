package com.txdata.common.redis.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.txdata.common.utils.DateUtils;
import com.txdata.common.utils.StringUtils;
import com.txdata.system.domain.UserDO;
import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author bootdo 1992lcg@163.com
 * @version V1.0
 */
public class RedisSessionDAO extends AbstractSessionDAO {

    private static Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);
    
    /**
     * shiro-redis的session对象前缀
     */
    private RedisManager redisManager;

    /**
     * The Redis key prefix for the sessions
     */
    private String keyPrefix = "shiro_redis_session:";

    @Override
    public void update(Session session) throws UnknownSessionException {
        this.saveSession(session);
    }

    /**
     * save session
     * @param session
     * @throws UnknownSessionException
     */
    private void saveSession(Session session) throws UnknownSessionException{
        if(session == null || session.getId() == null){
            logger.error("session or session id is null");
            return;
        }

        byte[] key = getByteKey(session.getId());
        byte[] value = SerializeUtils.serialize(session);
        session.setTimeout(redisManager.getExpire()*1000);
        this.redisManager.set(key, value, redisManager.getExpire());
    }

    @Override
    public void delete(Session session) {
        if(session == null || session.getId() == null){
            logger.error("session or session id is null");
            return;
        }
        redisManager.del(this.getByteKey(session.getId()));
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<Session> sessions = new HashSet<Session>();
        Set<byte[]> keys = redisManager.keys(this.keyPrefix + "*");
        if(keys != null && keys.size()>0){
            for(byte[] key:keys){
                Session s = (Session)SerializeUtils.deserialize(redisManager.get(key));
                sessions.add(s);
            }
        }
        return sessions;
    }
    
    /**
	 * 获取活动会话
	 * @param includeLeave 是否包括离线（最后访问时间大于3分钟为离线会话）
	 * @param principal 根据登录者对象获取活动会话
	 * @param filterSession 不为空，则过滤掉（不包含）这个会话。
	 * @return
	 */
	public Collection<Session> getActiveSessions(boolean includeLeave, String userId, Session filterSession) {
		// 如果包括离线，并无登录者条件。
		if (includeLeave && StringUtils.isBlank(userId)){
			return getActiveSessions();
		}
		Set<Session> sessions = Sets.newHashSet();
		for (Session session : getActiveSessions()){
			boolean isActiveSession = false;
			// 不包括离线并符合最后访问时间小于等于3分钟条件。
			if (includeLeave && DateUtils.pastMinutes(session.getLastAccessTime()) <= 3){
				isActiveSession = true;
			}
			// 符合登陆者条件。
			if (null != session && StringUtils.isNotBlank(userId)){
				PrincipalCollection pc = (PrincipalCollection)session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
				if(pc != null) {
					UserDO user = (UserDO)pc.getPrimaryPrincipal();
					if(userId.equals(user.getId())) {
						isActiveSession = true;
					}
				}
			}
			// 过滤掉的SESSION
			if (filterSession != null && filterSession.getId().equals(session.getId())){
				isActiveSession = false;
			}
			if (isActiveSession){
				sessions.add(session);
			}
		}
		return sessions;
	}

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.saveSession(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if(sessionId == null){
            logger.error("session id is null");
            return null;
        }
        Session s = (Session)SerializeUtils.deserialize(redisManager.get(this.getByteKey(sessionId)));
        return s;
    }

    /**
     * 获得byte[]型的key
     * @param key
     * @return
     */
    private byte[] getByteKey(Serializable sessionId){
        String preKey = this.keyPrefix + sessionId;
        return preKey.getBytes();
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }

    public void setRedisManager(RedisManager redisManager) {
        this.redisManager = redisManager;

        /**
         * 初始化redisManager
         */
        this.redisManager.init();
    }

    /**
     * Returns the Redis session keys
     * prefix.
     * @return The prefix
     */
    public String getKeyPrefix() {
        return keyPrefix;
    }

    /**
     * Sets the Redis sessions key
     * prefix.
     * @param keyPrefix The prefix
     */
    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }


}