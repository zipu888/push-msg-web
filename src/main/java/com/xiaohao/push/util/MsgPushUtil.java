/**
 * MsgPushUtil.java com.xiaohao.servlet Copyright (c) 2014,
 * 北京微课创景教育科技有限公司版权所有.
 */

package com.xiaohao.push.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.vko.sso.SSOConfig;
import cn.vko.sso.SSOConstant;
import cn.vko.sso.common.encrypt.Encrypt;
import cn.vko.sso.common.util.ReflectUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * <p>
 *
 * @author haozipu
 * @date 2014-12-9
 * @version 1.0.0
 */
public class MsgPushUtil {
	private static final Logger logger = LoggerFactory.getLogger(MsgPushUtil.class);

	/**
	 * 根据cookie中的uid 计算userId
	 * <p>
	 *
	 * @param cookieStr
	 * @return TODO(这里描述每个参数,如果有返回值描述返回值,如果有异常描述异常)
	 */
	public static Long getUserIdFromCookie(String cookieStr) {
		Encrypt en = ReflectUtil.getConfigEncrypt();
		String jsonToken = null;
		try {
			jsonToken = en.decrypt(cookieStr, SSOConfig.getSecretKey());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		if (StringUtils.isBlank(jsonToken)) {
			return null;
		}
		String[] arrStr = jsonToken.split(SSOConstant.CUT_SYMBOL);
		JSONObject job = JSON.parseObject(arrStr[0]);
		Long userId = (Long) job.get("userId");
		return userId;
	}

	/**
	 * 把一个字符串数组 转成一个long数组
	 * <p>
	 *
	 * @param idsStr
	 * @return TODO(这里描述每个参数,如果有返回值描述返回值,如果有异常描述异常)
	 */
	public static Long[] getUserIdsByStrArr(String[] idsStr) {
		Long[] userIdsLong = new Long[idsStr.length];
		int i = 0;
		for (String string : idsStr) {
			userIdsLong[i++] = Long.valueOf(string);
		}
		return userIdsLong;
	}

}
