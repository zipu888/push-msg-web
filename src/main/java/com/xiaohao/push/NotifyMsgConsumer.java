/**
 * NotifyMsgConsumer.java com.xiaohao.push Copyright (c) 2014,
 * 北京微课创景教育科技有限公司版权所有.
 */

package com.xiaohao.push;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.xiaohao.mqclient.MqTopic;
import com.xiaohao.mqclient.internal.KafkaConsumer;
import com.xiaohao.push.handler.NotifyClassMsgHandler;
import com.xiaohao.push.handler.NotifyPersonMsgHandler;
import com.xiaohao.push.handler.NotifySchoolMsgHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 消息提醒的消费者
 * <p>
 * @author   haozipu
 * @date	 2014-12-11 
 * @version  1.0.0	 
 */
public class NotifyMsgConsumer {

	private final static Logger logger = LoggerFactory.getLogger(NotifyMsgConsumer.class);

	private String zkConnect;

	private KafkaConsumer notifyPersonMsgConsumer;

	private KafkaConsumer notifyClassMsgConsumer;

	private KafkaConsumer notifySchoolMsgConsumer;

	@Autowired
	private NotifyPersonMsgHandler notifyPersonMsgHandler;

	@Autowired
	private NotifyClassMsgHandler notifyClassMsgHandler;

	@Autowired
	private NotifySchoolMsgHandler notifySchoolMsgHandler;


	@PostConstruct
	public void init() {
		logger.info(" kafka notifymsg consumer init ...");

		/*
		 *个人消息 
		 */
		notifyPersonMsgConsumer = new KafkaConsumer(this.zkConnect, MqTopic.NOTIFY_PERSON_MSG, notifyPersonMsgHandler);
		notifyPersonMsgConsumer.run();

		/*
		 * 班级消息
		 */
		notifyClassMsgConsumer = new KafkaConsumer(this.zkConnect, MqTopic.NOTIFY_CLASS_MSG, notifyClassMsgHandler);
		notifyClassMsgConsumer.run();

		/*
		 * 校级消息
		 */
		notifySchoolMsgConsumer = new KafkaConsumer(this.zkConnect, MqTopic.NOTIFY_SCHOOL_MSG, notifySchoolMsgHandler);
		notifySchoolMsgConsumer.run();

	}


	@PreDestroy
	public void destory() {

		if ( notifyPersonMsgConsumer != null ) {
			notifyPersonMsgConsumer.destory();
		}

		if ( notifyClassMsgConsumer != null ) {
			notifyClassMsgConsumer.destory();
		}

		if ( notifySchoolMsgConsumer != null ) {
			notifySchoolMsgConsumer.destory();
		}
	}


	public String getZkConnect() {
		return zkConnect;
	}


	public void setZkConnect( String zkConnect ) {
		this.zkConnect = zkConnect;
	}

}
