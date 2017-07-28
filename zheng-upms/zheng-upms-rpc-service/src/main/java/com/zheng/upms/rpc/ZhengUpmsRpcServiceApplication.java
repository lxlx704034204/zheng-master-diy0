package com.zheng.upms.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 服务启动类
 * Created by ZhangShuzheng on 2017/2/3.
 *
 * http://upms.zhangshuzheng.cn:1111/manage/index
 * http://upms.zhangshuzheng.cn:1111/manage/index;jsessionid=vj7oxrinmgzy1kfzymrvryi4n
 * http://upms.zhangshuzheng.cn:1111/sso/login
 */
public class ZhengUpmsRpcServiceApplication {

	private static Logger _log = LoggerFactory.getLogger(ZhengUpmsRpcServiceApplication.class);

	public static void main(String[] args) {
		_log.info(">>>>> zheng-upms-rpc-service 正在启动 <<<<<");
		new ClassPathXmlApplicationContext("classpath:META-INF/spring/*.xml");
		_log.info(">>>>> zheng-upms-rpc-service 启动完成 <<<<<");
	}

}
