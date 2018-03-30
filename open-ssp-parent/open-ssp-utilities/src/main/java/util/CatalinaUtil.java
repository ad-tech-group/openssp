package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;

/**
 * Utility class to determine some useful catalina environment data.
 * 
 * @author André Schmer
 *
 */
public class CatalinaUtil {

	private static final Logger log = (Logger) LoggerFactory.getLogger(CatalinaUtil.class);

	private static String ip;

	private static String ipSuffix;

	private static String pid;

	private static Integer port;

	private static String instanceName;

	private static String catalinaHome;

	private static String osName;

	private static String hostName;

	private CatalinaUtil() {
		throw new IllegalStateException("Utility class, use in static manner.");
	}

	public static int port() {
		if (port == null) {
			port = detectCatalinaHttpPort();
		}
		return port;
	}

	public static String instanceName() {
		if (instanceName == null) {
			instanceName = detectInstance();
		}
		return instanceName;
	}

	public static String hostName() {
		if (hostName == null) {
			hostName = detectHostName();
		}
		return hostName;
	}

	public static String catalinaHome() {
		if (catalinaHome == null) {
			catalinaHome = System.getProperty("catalina.home", System.getProperty("user.dir"));
		}
		return catalinaHome;
	}

	public static String osName() {
		if (osName == null) {
			osName = detectOsName();
		}
		return osName;
	}

	private static String detectOsName() {
		return System.getProperty("os.name").toLowerCase();
	}

	public static String ip() {
		if (ip == null) {
			ip = detectIPAddress();
		}
		return ip;
	}

	public static String ipSuffix() {
		if (ipSuffix == null) {
			ipSuffix = detectIPAddressSuffix();
		}
		return ipSuffix;
	}

	public static String pid() {
		if (pid == null) {
			pid = detectPID();
		}
		return pid;
	}

	private static String detectIPAddress() {
		String ipAddress = null;
		try {
			if (InetAddress.getLocalHost().isAnyLocalAddress() || InetAddress.getLocalHost().isLoopbackAddress() || InetAddress.getLocalHost().isSiteLocalAddress()) {
				ipAddress = InetAddress.getLoopbackAddress().getHostAddress();
			} else {
				ipAddress = InetAddress.getLocalHost().getHostAddress();
			}
		} catch (final UnknownHostException e) {
			log.error("{}", e.getMessage());
		}
		return ipAddress;
	}

	private static String detectIPAddressSuffix() {
		final String ip = ip();
		if (ip != null) {
			return ip.substring(ip.lastIndexOf('.') + 1, ip.length());
		}
		return null;
	}

	private static String detectInstance() {
		final String catalinaHome = catalinaHome();
		String instanceName = null;
		if (catalinaHome.contains("workspace") || catalinaHome.contains("git")) {
			instanceName = "localhost";
		} else {
			instanceName = catalinaHome.substring(catalinaHome.lastIndexOf('/') + 1, catalinaHome.length());
		}
		return instanceName;
	}

	private static String detectPID() {
		if ("linux".equals(detectOsName())) {
			final byte[] bo = new byte[180];
			final String[] cmd = { "bash", "-c", "echo $PPID" };
			try {
				final Process p = Runtime.getRuntime().exec(cmd);
				final InputStream is = p.getInputStream();
				is.read(bo);
				final String pid = new String(bo);
				final StringBuilder sb = new StringBuilder();
				for (int i = 0; i < pid.length(); i++) {
					if ((pid.charAt(i) != (byte) 0x00) && pid.charAt(i) != (byte) 0x0A) {
						sb.append(pid.charAt(i));
						continue;
					}
					break;
				}
				is.close();
				return sb.toString().trim();
			} catch (final IOException e) {
				log.error("{}", e.getMessage());
			}
		}
		return "1";
	}

	private static int detectCatalinaHttpPort() {
		int localPort = -1;
		final MBeanServer pms = ManagementFactory.getPlatformMBeanServer();
		ObjectName protocolHandlerObjectNamePattern;
		try {
			protocolHandlerObjectNamePattern = new ObjectName("Catalina:type=ProtocolHandler,port=*");
			for (final ObjectInstance oi : pms.queryMBeans(protocolHandlerObjectNamePattern, null)) {
				try {
					if ("org.apache.coyote.http11.Http11Protocol".equals(pms.getAttribute(oi.getObjectName(), "modelerType"))) {
						localPort = (Integer) pms.getAttribute(oi.getObjectName(), "localPort");
					}
				} catch (final AttributeNotFoundException | InstanceNotFoundException | MBeanException | ReflectionException e) {
					log.error("{}", e.getMessage());
				}
			}
		} catch (final MalformedObjectNameException e) {
			log.error("{}", e.getMessage());
		}

		return localPort;
	}

	public static boolean isPrivateAddress() {
		return InetAddress.getLoopbackAddress().getHostAddress().equals(ip);
	}

	public static boolean isOsName(final String fragment) {
		return (detectOsName() != null && detectOsName().contains(fragment));
	}

	private static String detectHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (final UnknownHostException e) {
			log.error("{}", e.getMessage());
		}
		return null;
	}
}
