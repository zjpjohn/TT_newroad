package com.newroad.tripmaster.service.openmessage.demo;

import com.newroad.tripmaster.service.openmessage.utils.ConfigLoader;
import com.newroad.tripmaster.service.openmessage.config.AppConfig;
import com.newroad.tripmaster.service.openmessage.lib.MAILXSend;

public class MailXSendDemo {

	public static void main(String[] args) {

		AppConfig config = ConfigLoader.load(ConfigLoader.ConfigType.Mail);
		MAILXSend submail = new MAILXSend(config);
		submail.addTo("leo@submail.cn","leo");
//		submail.setSender("no-reply@submail.cn","SUBMAIL");
		submail.setProject("uigGk1");
		submail.addVar("name", "leo");
		submail.addVar("age", "32");
		submail.addLink("developer", "http://submail.cn/chs/developer");
		submail.addLink("store", "http://submail.cn/chs/store");
		submail.addHeaders("X-Accept", "zh-cn");
		submail.addHeaders("X-Mailer", "leo App");
		submail.xsend();

	}	
}
