package com.newroad.cache.common.couchbase;

import net.spy.memcached.PersistTo;
import net.spy.memcached.ReplicateTo;

/**
 * @info Node Setting
 * @author tangzj1
 * @date Feb 14, 2014
 * @version
 */
public class NodeSetting {

	public static PersistTo getPersistToSetting(int persistNode) {
		PersistTo persistTo = null;
		switch (persistNode) {
		case 0:
			persistTo = PersistTo.ZERO;
			break;
		case 1:
			persistTo = PersistTo.ONE;
			break;
		case 2:
			persistTo = PersistTo.TWO;
			break;
		case 3:
			persistTo = PersistTo.THREE;
			break;
		case 4:
			persistTo = PersistTo.FOUR;
			break;
		default:
			persistTo = PersistTo.ZERO;
			break;
		}
		return persistTo;
	}

	public static ReplicateTo getReplicateToSetting(int replicateNode) {
		ReplicateTo replicateTo = null;
		switch (replicateNode) {
		case 0:
			replicateTo = ReplicateTo.ZERO;
			break;
		case 1:
			replicateTo = ReplicateTo.ONE;
			break;
		case 2:
			replicateTo = ReplicateTo.TWO;
			break;
		case 3:
			replicateTo = ReplicateTo.THREE;
			break;
		default:
			replicateTo = ReplicateTo.ZERO;
			break;
		}
		return replicateTo;
	}
}
