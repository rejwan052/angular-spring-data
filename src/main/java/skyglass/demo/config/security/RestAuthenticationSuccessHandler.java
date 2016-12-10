package skyglass.demo.config.security;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import skyglass.demo.controller.utils.ControllerUtils;
 
@Component
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
 
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        clearAuthenticationAttributes(request);
		ControllerUtils.sendResponse(response, HttpServletResponse.SC_OK, 
				getAuthenticationMap(authentication));
    }
    
    public static Map<String, Object> getAuthenticationMap(Authentication authentication) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("name", authentication.getName());
		map.put("roles", AuthorityUtils.authorityListToSet(authentication.getAuthorities()));  
		return map;
    }
}
