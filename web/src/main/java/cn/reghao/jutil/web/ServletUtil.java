package cn.reghao.jutil.web;

import cn.reghao.jutil.jdk.serializer.JsonConverter;
import cn.reghao.jutil.web.log.GatewayLog;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author reghao
 * @date 2021-06-02 13:16:58
 */
public class ServletUtil {
    public static Map<String, String> getCookies() {
        HttpServletRequest request = getRequest();
        Cookie[] cookies = request.getCookies();
        Map<String, String> map = new HashMap<>();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                String value = cookie.getValue();
                map.put(name, value);
            }
        }
        return map;
    }

    public static String getCookie(String name) {
        Map<String, String> map = getCookies();
        return map.get(name);
    }

    public static Cookie getCookie1(String name, ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String name1 = cookie.getName();
                if (name1.equals(name)) {
                    return cookie;
                }
            }
        }

        return null;
    }

    public static String getCookie(String name, ServletRequest servletRequest) {
        Map<String, String> map = getCookies(servletRequest);
        return map.get(name);
    }

    public static Map<String, String> getCookies(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Cookie[] cookies = request.getCookies();
        Map<String, String> map = new HashMap<>();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                String value = cookie.getValue();
                map.put(name, value);
            }
        }
        return map;
    }

    public static String getHeader(String key) {
        return getRequest().getHeader(key);
    }

    public static String getBearerToken() {
        String auth = getRequest().getHeader("Authorization");
        if (auth == null) {
            return null;
        }
        return auth.replace("Bearer ", "");
    }

    public static String getBearerToken(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String auth = request.getHeader("Authorization");
        if (auth == null) {
            return null;
        }
        return auth.replace("Bearer ", "");
    }

    @Deprecated
    public static String getUserId() {
        String userId = getRequest().getHeader("x-user-id");
        return  userId != null ? userId : "-1";
    }

    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static String getBody() throws IOException {
        HttpServletRequest request = getRequest();
        StringBuffer sb = new StringBuffer();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    public static Object getBody(HttpServletRequest servletRequest, Class<?> clazz) throws IOException {
        StringBuilder body = new StringBuilder();
        BufferedReader reader = servletRequest.getReader();
        String line;
        while (null != (line = reader.readLine())) {
            body.append(line);
        }
        reader.close();
        return JsonConverter.jsonToObject(body.toString(), clazz);
    }

    public static String getSessionId() {
        return getRequest().getSession().getId();
    }

    /**
     * 获取 query 参数值
     *
     * @param
     * @return
     * @date 2021-06-02 下午1:19
     */
    public static String getRequestParam(String param, String defaultValue){
        String parameter = getRequest().getParameter(param);
        return StringUtils.isEmpty(parameter) ? defaultValue : parameter;
    }

    public static HttpServletRequest getRequest(){
        return getServletRequest().getRequest();
    }

    public static HttpServletResponse getResponse(){
        return getServletRequest().getResponse();
    }

    private static ServletRequestAttributes getServletRequest(){
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    public static GatewayLog getGatewayLog() {
        HttpServletRequest request = ServletUtil.getRequest();
        return getGatewayLog(request);
    }

    public static GatewayLog getGatewayLog(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        int remotePort = request.getRemotePort();

        Map<String, String> requestHeaders = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            requestHeaders.put(headerName, headerValue);
        }

        String requestId = (String) request.getAttribute("x-request-id");
        long requestTime = (Long) request.getAttribute("x-request-time");
        String targetRoute = "";
        String targetService = "";
        String requestUrl = request.getRequestURI();
        String requestMethod = request.getMethod();
        String requestBody = "";

        int statusCode = getResponse().getStatus();
        Map<String, String> responseHeaders = new HashMap<>();
        String responseBody = "";
        long responseTime = System.currentTimeMillis();
        long executeTime = responseTime - requestTime;

        String realRemoteAddr = requestHeaders.get("X-Real-Remote");
        if (realRemoteAddr != null && !realRemoteAddr.isBlank()) {
            remoteAddr = realRemoteAddr;
        }

        GatewayLog gatewayLog = new GatewayLog(requestId, requestTime, requestUrl, requestMethod, requestHeaders,
                remoteAddr, remotePort, statusCode, responseHeaders, responseTime);
        gatewayLog.setExecuteTime(executeTime);
        return gatewayLog;
    }
}
