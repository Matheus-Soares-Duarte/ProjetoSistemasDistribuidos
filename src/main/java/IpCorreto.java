package main.java;

import java.net.*;
import java.util.Enumeration;

public class IpCorreto {
    public static String getIpCorreto() throws SocketException {
        String addrs = null;
        if(System.getProperty("os.name").contains("Linux")){
            Enumeration<NetworkInterface> em = NetworkInterface.getNetworkInterfaces();
            while (em.hasMoreElements()) {
                NetworkInterface i = (NetworkInterface) em.nextElement();
                for (Enumeration en2 = i.getInetAddresses(); en2.hasMoreElements();) {
                    InetAddress addr = (InetAddress) en2.nextElement();
                    if (!addr.isLoopbackAddress()) {
                        if (addr instanceof Inet4Address) {
                            addrs = addr.getHostAddress();
                        }
                        if (addr instanceof Inet6Address) {
                            if (true) {
                                continue;
                            }
                            addrs = addr.getHostAddress();
                        }
                    }
                }
            }
        }else{
            try {
                addrs = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return addrs;
    }

}
