package com.newroad.tripmaster.service.openmessage.lib.base;

import com.newroad.tripmaster.service.openmessage.entity.DataStore;

public abstract class SenderWapper {

	protected DataStore requestData = new DataStore();

	public abstract ISender getSender();
}
