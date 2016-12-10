package skyglass.demo.controller.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.databind.ObjectMapper;

import skyglass.demo.controller.model.Response;
import skyglass.demo.service.error.Error;

/**
 * Utility class for Spring Security.
 */
public final class ControllerUtils {


    private static final ObjectMapper mapper = new ObjectMapper();


    private ControllerUtils() {
    }


    /**
     * Get the login of the current user.
     */
    public static String getCurrentLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        UserDetails springSecurityUser = null;
        String userName = null;

        if(authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                springSecurityUser = (UserDetails) authentication.getPrincipal();
                userName = springSecurityUser.getUsername();
            } else if (authentication.getPrincipal() instanceof String) {
                userName = (String) authentication.getPrincipal();
            }
        }

        return userName;
    }
    
    public static void sendError(HttpServletResponse response, Error error) throws IOException {
    	sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, error.getReason(), error.getMessage());
    }  
    
    public static void sendError(HttpServletResponse response, String reason, String message) throws IOException {
    	sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, reason, message);
    }
   
    public static void sendError(HttpServletResponse response, int status, String reason, String message) throws IOException {
    	sendError(response, message, status, reason, message);
    }
    
    public static void sendError(HttpServletResponse response, Exception exception, int status, String reason, String message) throws IOException {
    	sendError(response, exception.getMessage(), status, reason, message);
    }
    
    private static void sendError(HttpServletResponse response, String exceptionMessage, int status, String reason, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status);
        PrintWriter writer = response.getWriter();
        Error error = new Error(reason, exceptionMessage);
        writer.write(mapper.writeValueAsString(new Response(status, message, error)));
        writer.flush();
        writer.close();
    }    

    public static void sendResponse(HttpServletResponse response, int status, Object object) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(mapper.writeValueAsString(object));
        response.setStatus(status);
        writer.flush();
        writer.close();
    }

}
