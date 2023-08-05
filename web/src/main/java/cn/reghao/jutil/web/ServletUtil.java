package cn.reghao.jutil.web;

import cn.reghao.jutil.jdk.serializer.JsonConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author reghao
 * @date 2021-06-02 13:16:58
 */
public class ServletUtil {
    public static Map<String, String> getCookies(HttpServletRequest request) {
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

    public static String getBearerToken() {
        String auth = getRequest().getHeader("Authorization");
        if (auth == null) {
            return null;
        }
        return auth.replace("Bearer ", "");
    }

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
}
