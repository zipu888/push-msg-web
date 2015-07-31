/**
 * NotifyMsgHandler.java com.xiaohao.push Copyright (c) 2014,
 * 北京微课创景教育科技有限公司版权所有.
 */

package com.xiaohao.push.handler;

import javax.inject.Inject;

import com.xiaohao.mqclient.handler.MqHandler;
import com.xiaohao.standalone.manage.NotifyMessageDto;

import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ServerMessage;
import org.cometd.bayeux.server.ServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.vko.core.web.cache.RedisCache;

import com.alibaba.fastjson.JSON;


/**
 * 个人消息的处理
 * <p>
 * @author   haozipu
 * @date	 2014-12-11 
 * @version  1.0.0	 
 */
@Component
public class NotifyPersonMsgHandler extends MqHandler {

	private final static Logger logger = LoggerFactory.getLogger(NotifyClassMsgHandler.class);

	@Autowired
	protected RedisCache redis;

	@Inject
	private BayeuxServer bayeux;


	@Override
	public void callback( byte[] data ) {

		NotifyMessageDto dto = null;
		try {
			//解析json
			dto = JSON.parseObject(new String(data), NotifyMessageDto.class);
		} catch ( Exception e ) {
			logger.error("NotifyMessageDto json parse error. data: {}", new String(data));
			logger.error(e.getMessage(),e);
			return;
		}

		String sessionId = (String) redis.get("push_userid_" + dto.getTargetId());

		//为空表示不在线 不发送
		if ( sessionId != null ) {
			ServerSession session = bayeux.getSession(sessionId);
			if ( session != null ) {
				ServerMessage.Mutable forward = bayeux.newMessage();
				forward.setChannel("/person/notify");
				forward.setLazy(true);
				forward.setData(dto.getContent());
				logger.debug("person msg:" + dto.getContent());
				session.deliver(session, forward);
			}

		}
	}

}
