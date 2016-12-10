package skyglass.demo.controller.security;

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

import skyglass.demo.controller.utils.ControllerUtils;
import skyglass.demo.data.model.security.Authority;
import skyglass.demo.service.error.ServiceException;
import skyglass.demo.service.security.AuthorityService;

@RestController
@RequestMapping("/rest/security/role")
public class RoleController {
	
    @Autowired
    protected AuthorityService authorityService;
	
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('SECURITY')")
    public Iterable<Authority> getAllRoles()
    throws Exception {
        return authorityService.findAll();
    }
    
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('SECURITY_WRITER')")
    public ResponseEntity<Authority> saveRole(@RequestBody Authority role, HttpServletResponse response)
    throws Exception {
    	try {
            return new ResponseEntity<Authority>(authorityService.save(role), HttpStatus.OK);    		
    	} catch (ServiceException se) {
    		ControllerUtils.sendError(response, se.getError());
    		return null;
    	}

    } 
    
    @RequestMapping(method = RequestMethod.DELETE, path  = "/{roleId}")
    @PreAuthorize("hasAuthority('SECURITY_WRITER')")
    public ResponseEntity<Long> deleteRole(@PathVariable Long roleId)
    throws Exception {
    	authorityService.delete(roleId);
        return new ResponseEntity<Long>(roleId, HttpStatus.OK);
    } 

}
