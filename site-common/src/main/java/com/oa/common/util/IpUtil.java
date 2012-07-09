package com.oa.common.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 读取本地的IP地址
 * 兼容linux和windows
 * @since 2010-11-24
 * @version 1.0v
 */
public class IpUtil {
	
	public static String getLocalIp() throws SocketException{
		InetAddress ip = null;
		boolean bFindIP = false;
		Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
		while (netInterfaces.hasMoreElements()) {
			if (bFindIP) {
				break;
			}
			NetworkInterface ni = netInterfaces.nextElement();
			// ----------特定情况，可以考虑用ni.getName判断
			// 遍历所有ip
			Enumeration<InetAddress> ips = ni.getInetAddresses();
			while (ips.hasMoreElements()) {
				ip = (InetAddress) ips.nextElement();
				if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() // 127.开头的都是lookback地址
						&& ip.getHostAddress().indexOf(":") == -1) {
					bFindIP = true;
					break;
				}
			}
		}
		return ip.getHostAddress();
	}
}
