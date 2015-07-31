/**
 * NotifySchoolMsgHandler.java com.xiaohao.push.handler Copyright (c) 2014,
 * 北京微课创景教育科技有限公司版权所有.
 */

package com.xiaohao.push.handler;

import java.util.List;

import javax.inject.Inject;

import com.xiaohao.mqclient.handler.MqHandler;
import com.xiaohao.standalone.manage.NotifyMessageDto;

import org.cometd.annotation.Session;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ServerChannel;
import org.cometd.bayeux.server.ServerMessage;
import org.cometd.bayeux.server.ServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.vko.core.web.cache.RedisCache;

import com.alibaba.fastjson.JSON;


/**
 * 学校群发消息的处理
 * 
 * <p>
 * @author   haozipu
 * @date	 2014-12-11 
 * @version  1.0.0	 
 */
@Component
public class NotifySchoolMsgHandler extends MqHandler {

	private final static Logger logger = LoggerFactory.getLogger(NotifyClassMsgHandler.class);

	@Autowired
	protected RedisCache redis;

	@Inject
	private BayeuxServer bayeux;

	@Session
	private ServerSession serverSession;


	@Override
	public void callback( byte[] data ) {

		NotifyMessageDto dto = null;
		try {
			//解析json
			dto = JSON.parseObject(new String(data), NotifyMessageDto.class);
		} catch ( Exception e ) {
			logger.error("NotifyMessageDto json parse error. data: {}", new String(data));
			logger.error(e.getMessage(),e);
		}

		if ( dto != null ) {
			ServerChannel channel = bayeux.getChannel("/school/" + dto.getTargetId());
			ServerMessage.Mutable forward = bayeux.newMessage();
			forward.setData(dto.getContent());
			forward.setLazy(true);
			forward.setChannel("/school/" + dto.getTargetId());
			List<ServerSession> sessions = bayeux.getSessions();
			ServerSession s = sessions.get(0);
			logger.debug("school msg:" + dto.getContent());
			channel.publish(s, forward);
		}

	}
}
