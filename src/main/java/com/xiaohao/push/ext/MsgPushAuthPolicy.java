/**
 * MsgPushAuth.java com.xiaohao.servlet Copyright (c) 2014,
 * 北京微课创景教育科技有限公司版权所有.
 */

package com.xiaohao.push.ext;

import java.util.Map;

import com.xiaohao.push.util.MsgPushUtil;

import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ServerMessage;
import org.cometd.bayeux.server.ServerSession;
import org.cometd.server.DefaultSecurityPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import cn.vko.core.web.cache.RedisCache;

/**
 * 自定一个握手操作
 * <p>
 * @author   haozipu
 * @date	 2014-12-9 
 * @version  1.0.0	 
 */
@Component
public class MsgPushAuthPolicy extends DefaultSecurityPolicy implements ServerSession.RemoveListener, InitializingBean {

	private final static Logger logger = LoggerFactory.getLogger(MsgPushAuthPolicy.class);

	@Autowired
	protected RedisCache redis;

	@Override
	public boolean canHandshake(BayeuxServer server, ServerSession session, ServerMessage message) {

		@SuppressWarnings("unchecked")
		Map<Object, Object> map = (Map<Object, Object>) message.get("ssoToken");
		if (map == null) {
			return false;
		}
		String ssoToken = (String) map.get("token");

		Long userId = MsgPushUtil.getUserIdFromCookie(ssoToken);
		logger.debug("UserId:" + userId);
		if (userId != null) {
			String sessionId = (String) redis.get("push_userid_" + userId);
			logger.debug("SessionId:" + sessionId);
			redis.del("push_" + sessionId);
			redis.set("push_" + session.getId(), userId);
			logger.debug("new sessionId:" + session.getId());
			redis.set("push_userid_" + userId, session.getId());
			return true;
		}
		return false;

	}

	@Override
	public void removed(ServerSession session, boolean flag) {
		//删除redis中 session和用户的对应关系 
		Long userId = (Long) redis.get("push_" + session.getId());
		redis.del("push_userid_" + userId);
		redis.del("push_" + session.getId());

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(redis, "redis not inject!");
	}

}
