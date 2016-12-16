package skyglass.demo.rest.security;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import skyglass.demo.config.SecurityConfig;
import skyglass.demo.config.security.RestAuthenticationSuccessHandler;

@RestController
public class AuthResource {
	
	private String message = "Hello World";
	private List<Map<String, Object>> changes = new ArrayList<Map<String, Object>>();

	@RequestMapping(value="/resource", method=RequestMethod.GET)
	@ResponseBody
    @PreAuthorize("isAuthenticated()")
	public Map<String, Object> resource() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("id", UUID.randomUUID().toString());
		model.put("content", message);
		return model;
	}	
	
	@RequestMapping(value="/resource/changes", method=RequestMethod.GET)
	@ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
	public List<Map<String, Object>> changes() {
		return changes;
	}	
	
	@RequestMapping(value="/resource", method=RequestMethod.POST)
	@ResponseBody
    @PreAuthorize("hasAuthority('WRITER')")
	public Map<String, Object> update(@RequestBody Map<String, String> map, Principal principal) {
		Map<String, Object> model = new HashMap<String, Object>();
		if (map.get("content") != null) {
			message = map.get("content");
			Map<String, Object> change = new HashMap<String, Object>();
			change.put("timestamp", new Date());
			change.put("user", principal.getName());
			change.put("content", message);
			changes.add(change);
			if (changes.size() > 10) {
				changes = new ArrayList<Map<String, Object>>();
			}
			model.put("id", UUID.randomUUID().toString());
			model.put("content", message);
		}
		return model;
	}	
	
	@RequestMapping(value=SecurityConfig.AUTHENTICATE_URL, method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> authenticate(Principal user) {
		return null;
	}
	
	@RequestMapping(value=SecurityConfig.REMEMBER_ME_AUTHENTICATE_URL, method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> rememberMeAuthenticate(Principal user, HttpServletResponse response) throws IOException {
		if (user == null) {
	        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	        return null;
		}
		return RestAuthenticationSuccessHandler.getAuthenticationMap((Authentication)user);
	}	

}
