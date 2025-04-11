package HeoJin.demoBlog.util;

import jakarta.servlet.http.HttpServletResponse;

public class CustomUtil {
    public static HttpServletResponse setUTF(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        return response;
    }
}
