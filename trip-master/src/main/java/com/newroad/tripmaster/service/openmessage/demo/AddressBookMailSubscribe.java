package com.newroad.tripmaster.service.openmessage.demo;

import com.newroad.tripmaster.service.openmessage.utils.ConfigLoader;
import com.newroad.tripmaster.service.openmessage.config.AppConfig;
import com.newroad.tripmaster.service.openmessage.lib.ADDRESSBOOKMail;

public class AddressBookMailSubscribe {

	public static void main(String[] args) {

		AppConfig config = ConfigLoader.load(ConfigLoader.ConfigType.Mail);
		ADDRESSBOOKMail addressbook = new ADDRESSBOOKMail(config);
		addressbook.setAddress("leo@apple.cn", "leo");
		addressbook.subscribe();
	}	
}
