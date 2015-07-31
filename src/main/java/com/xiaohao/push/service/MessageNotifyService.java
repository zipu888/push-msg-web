package com.xiaohao.push.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import net.com.xiaohaoage.entity.MessageStatistics;
import net.yunxcom.xiaohaomodule.IMessageUserModule;
import net.yunxiaoyuan.push.ext.MsgPushAuthPolicy;

import org.cometd.annotation.Configure;
import org.cometd.annotation.Listener;
import org.cometd.annotation.Service;
import org.cometd.annotation.Session;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ConfigurableServerChannel;
import org.cometd.bayeux.server.ServerMessage;
import org.cometd.bayeux.server.ServerSession;
import org.springframework.beans.factory.annotation.Autowired;

import cn.vko.core.common.response.Response;
import cn.vko.core.web.cache.RedisCache;

import com.alibaba.fastjson.JSON;

@Named
@Singleton
@Service("helloService")
public class MessageNotifyService {

	@Inject
	private BayeuxServer bayeux;

	@Session
	private ServerSession serverSession;

	@Autowired
	private MsgPushAuthPolicy policy;

	@Autowired
	protected RedisCache redis;

	@Resource(name = "messageUserModuleImpl")
	private IMessageUserModule msgUserModule;


	@PostConstruct
	public void init() {
	}


	@Configure("/service/hello")
	public void configure( ConfigurableServerChannel channel ) {
		channel.setLazy(true);
		bayeux.setSecurityPolicy(policy);
	}


	@Listener("/service/hello")
	public void processHello( ServerSession remote, ServerMessage.Mutable message ) {
		Long userId = (Long) redis.get("push_" + remote.getId());
		if ( userId != null ) {
			Response<MessageStatistics> msResp = msgUserModule.queryMyMsgNotify(userId);
			ServerMessage.Mutable forward = bayeux.newMessage();
			forward.setId(message.getId());
			forward.setData(JSON.toJSONString(msResp.getBody()));
			remote.deliver(serverSession, "/service/hello", forward, null);

		}

	}


	@Listener(value = { "/person/notify", "/class/*", "/school/*" })
	public void processNotify( ServerSession remote, ServerMessage.Mutable message ) {

	}

}
