package com.newroad.tripmaster.service.openmessage.demo;

import com.newroad.tripmaster.service.openmessage.utils.ConfigLoader;
import com.newroad.tripmaster.service.openmessage.config.AppConfig;
import com.newroad.tripmaster.service.openmessage.lib.ADDRESSBOOKMessage;

public class AddressBookMessageSubscribe {

	public static void main(String[] args) {

		AppConfig config = ConfigLoader.load(ConfigLoader.ConfigType.Message);
		ADDRESSBOOKMessage addressbook = new ADDRESSBOOKMessage(config);
		//addressbook.setAddress("186****7150");
		addressbook.subscribe();
	}	
}
