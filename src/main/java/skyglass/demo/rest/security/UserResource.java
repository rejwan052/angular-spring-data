package skyglass.demo.rest.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import skyglass.demo.model.security.Authority;
import skyglass.demo.model.security.User;
import skyglass.demo.service.ServiceException;
import skyglass.demo.service.security.UserService;
import skyglass.demo.utils.rest.RestUtils;

@RestController
@RequestMapping("/rest/security/user")
public class UserResource {

    @Autowired
    protected UserService userService;
    
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('SECURITY')")
    public Iterable<User> getAllUsers()
    throws Exception {
        return userService.findAll();
    }
    
    @RequestMapping(method = RequestMethod.GET, path  = "/notPublishers")
    @PreAuthorize("hasAuthority('SECURITY')")
    public Iterable<User> getNotPublishers()
    throws Exception {
        return userService.findNotPublishers();
    }
    
    @RequestMapping(method = RequestMethod.GET, path  = "/notSubscribers")
    @PreAuthorize("hasAuthority('SECURITY')")
    public Iterable<User> getNotSubscribers()
    throws Exception {
        return userService.findNotSubscribers();
    }
    
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('SECURITY_WRITER')")
    public ResponseEntity<User> saveUser(@RequestBody User user, HttpServletResponse response)
    throws Exception {
    	try {
            return new ResponseEntity<User>(userService.save(user), HttpStatus.OK);    		
    	} catch (ServiceException se) {
    		RestUtils.sendError(response, se.getError());
    		return null;
    	}

    }  
    
    @RequestMapping(method = RequestMethod.POST, path  = "/{userId}/setPassword")
    @PreAuthorize("hasAuthority('SECURITY_WRITER')")
    public ResponseEntity<User> setPassword(@PathVariable Long userId, @RequestBody PasswordWrapper passwordWrapper)
    throws Exception {
    	User user = userService.findOne(userId);
    	user.setPassword(passwordWrapper.password);
        return new ResponseEntity<User>(userService.save(user), HttpStatus.OK);
    } 
    
    @RequestMapping(method = RequestMethod.GET, path  = "/{userId}/roles")
    @PreAuthorize("hasAuthority('SECURITY')")
    public Iterable<Authority> getUserRoles(@PathVariable Long userId)
    throws Exception {
    	User user = userService.findOne(userId);
        return user.getAuthorities();
    }    
    
    @RequestMapping(method = RequestMethod.POST, path  = "/{userId}/setRoles")
    @PreAuthorize("hasAuthority('SECURITY_WRITER')")
    public ResponseEntity<User> setUserRoles(@PathVariable Long userId, @RequestBody RolesWrapper rolesWrapper)
    throws Exception {
    	User user = userService.setAuthorities(userId, rolesWrapper.roleIds);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }       
    
    @RequestMapping(method = RequestMethod.DELETE, path  = "/{userId}")
    @PreAuthorize("hasAuthority('SECURITY_WRITER')")
    public ResponseEntity<Long> deleteUser(@PathVariable Long userId)
    throws Exception {
    	userService.delete(userId);
        return new ResponseEntity<Long>(userId, HttpStatus.OK);
    }
    
    private static class RolesWrapper {
		public Long[] roleIds;
    }
    
    private static class PasswordWrapper {
		public String password;
    }

}

 
