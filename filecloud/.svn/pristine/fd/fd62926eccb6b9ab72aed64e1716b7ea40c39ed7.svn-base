package com.newroad.fileext.service.storage.cache;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.fileext.utilities.ProcessUtil;

/**
 * @info : 节点监控
 * @author: xiangping_yu
 * @data : 2013-12-2
 * @since : 1.5
 */
public class NodeMonitor implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(NodeMonitor.class);

	@Override
	public void run() {
		List<Node> nodes = CacheCloud.getInstance().getNodeList();
		List<Node> copy = new ArrayList<Node>(nodes.size());
		for (Node node : nodes) {
			try {
				copy.add(node.clone());
			} catch (CloneNotSupportedException e) {
				logger.error("node CloneNotSupportedException:", e);
			}
		}

		List<Node> warnNode = new ArrayList<Node>(nodes.size());
		for (Node node : copy) {
			double used = getUsed(node);
			node.setUsed(used);
			if (node.isWarn()) {
				logger.warn("Cluster node warn, no available HD");
				warnNode.add(node);
			}
		}

		CacheCloud.getInstance().setNodeList(copy);
		CacheCloud.getInstance().setWarnList(warnNode);
	}

	/**
	 * 检测节点使用大小 (单们：MB)
	 */
	private double getUsed(Node node) {
		double usedSize = 0d;
		// String host = node.getHost();
		// String port = node.getPort();
		String path = node.getPath();
		// logger.debug("Monitor node used hard desk, node info ["+host+":"+port+path+"]");

		String osName = System.getProperty("os.name").toLowerCase();
		String[] cmdStr = null;
		if (osName.indexOf("win") >= 0) {
			cmdStr = new String[] { "dir", "/a/s", path };
		} else {
			cmdStr = new String[] { "du", "-hs", path, "-B", "M" };
		}
		String result = ProcessUtil.execCallableProcess(cmdStr);
		//logger.debug("Check disk capacity by command:" + result);

		if(result==null){
			throw new RuntimeException(
					"Couldn't get the disk capacity by command!");
		}
		int index = result.indexOf("M");
		if (index <= 0) {
			throw new RuntimeException(
					"Couldn't get the disk capacity by command!");
		} else {
			usedSize = Double.parseDouble(result.substring(0, index));
			logger.debug("The used disk size:" + result + "M");
		}
		return usedSize;
	}
}
