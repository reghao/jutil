package cn.reghao.jutil.tool.http.util;

import org.apache.http.conn.DnsResolver;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author reghao
 * @date 2021-03-15 21:51:03
 */
public class FakeDnsResolver implements DnsResolver {
    @Override
    public InetAddress[] resolve(String host) throws UnknownHostException {
        // Return some fake DNS record for every request, we won't be using it
        return new InetAddress[] { InetAddress.getByAddress(new byte[] { 1, 1, 1, 1 }) };
    }
}
