/**
 * Sender.java com.xiaohao.push Copyright (c) 2014, 北京微课创景教育科技有限公司版权所有.
 */

package net.com.xiaohaoh;

import net.yunxcom.xiaohaot.MqTopic;
import net.yunxiaoycom.xiaohaoternal.KafkaMessage;
import net.yunxiaoyuan.com.xiaohaoal.KafkaProducer;
import net.yunxiaoyuan.standalone.manage.NotifyMessageDto;

import org.junit.Test;


/**
 * 用来模拟发送
 * <p>
 * @author   haozipu
 * @date	 2014-12-11 
 * @version  1.0.0	 
 */
public class Sender {


	@Test
	public void test() throws InterruptedException {
		KafkaProducer kp = new KafkaProducer();
		kp.setMetadataBrokerList("192.168.1.252:9092");
		kp.init();
		NotifyMessageDto dto = new NotifyMessageDto();
		dto.setContent("测试 测试 消消火");
		dto.setTargetId(232711701680619520L);
		dto.setType(1);
		//kp.send(new KafkaMessage(MqTopic.NOTIFY_CLASS_MSG, dto));
		for ( int i = 0 ; i < 100 ; i++ ) {
			kp.send(new KafkaMessage(MqTopic.NOTIFY_PERSON_MSG, dto));
			Thread.sleep(1000);
		}

		//kp.send(new KafkaMessage(MqTopic.NOTIFY_SCHOOL_MSG, dto));
	}
}
