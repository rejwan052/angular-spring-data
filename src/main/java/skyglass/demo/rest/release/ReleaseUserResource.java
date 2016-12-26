package skyglass.demo.rest.release;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import skyglass.data.model.security.User;
import skyglass.data.query.QueryResult;
import skyglass.demo.service.release.ReleaseUserService;

@RestController
@RequestMapping("/rest/security/release/user")
public class ReleaseUserResource {

    @Autowired
    protected ReleaseUserService releaseUserService;
    
    @RequestMapping(method = RequestMethod.GET, path  = "/notPublishers")
    @PreAuthorize("hasAuthority('SECURITY')")
    public QueryResult<User> getNotPublishers(HttpServletRequest request)
    throws Exception {
        return releaseUserService.findNotPublishers(request);
    }
    
    @RequestMapping(method = RequestMethod.GET, path  = "/notSubscribers")
    @PreAuthorize("hasAuthority('SECURITY')")
    public QueryResult<User> getNotSubscribers(HttpServletRequest request)
    throws Exception {
        return releaseUserService.findNotSubscribers(request);
    }
}

 
