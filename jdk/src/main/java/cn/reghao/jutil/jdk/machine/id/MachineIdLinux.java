package cn.reghao.jutil.jdk.machine.id;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author reghao
 * @date 2021-05-20 15:45:28
 */
public class MachineIdLinux implements MachineId {
    private String machineId = "";
    private String machineIpv4 = "127.0.0.1";

    @Override
    public String id() {
        if (!machineId.isBlank()) {
            return machineId;
        }

        try {
            File file = new File("/etc/machine-id");
            BufferedReader in =  new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            machineId = in.readLine();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return machineId.isBlank() ? "no-machine-id" : machineId;
    }

    @Override
    public String ipv4() {
        String localhost = "127.0.0.1";
        if (!machineIpv4.equals(localhost)) {
            return machineIpv4;
        }

        List<String> ipv4List = detail();
        machineIpv4 = ipv4List.isEmpty() ? localhost : ipv4List.get(0);
        return machineIpv4;
    }

    private List<String> detail() {
        List<String> list = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            // 遍历主机的网络接口
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                String ifaceName = iface.getName();
                // 过滤掉 localhost 和虚拟网卡
                if (ifaceName.startsWith("enp")
                        || ifaceName.startsWith("wlp")
                        || ifaceName.startsWith("eth")
                        || ifaceName.startsWith("wlan")
                        || ifaceName.startsWith("em")) {
                    Enumeration<InetAddress> inetAddrs = iface.getInetAddresses();
                    while (inetAddrs.hasMoreElements()) {
                        InetAddress address = inetAddrs.nextElement();
                        if (!address.isLoopbackAddress()) {
                            if (address instanceof Inet4Address) {
                                list.add(address.getHostAddress());
                            } else if (address instanceof Inet6Address) {
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return list;
    }
}
