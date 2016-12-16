package skyglass.demo.rest.security;

import java.net.URLDecoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import skyglass.demo.model.security.Token;
import skyglass.demo.service.security.TokenService;

@RestController
@RequestMapping("/rest/security/token")
public class TokenResource {
	
    @Autowired
    protected TokenService tokenService;
	
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('SECURITY')")
    public Iterable<Token> getAllTokens()
    throws Exception {
        return tokenService.findAll();
    }
    
    @RequestMapping(method = RequestMethod.DELETE, path  = "/{tokenId}")
    @PreAuthorize("hasAuthority('SECURITY_WRITER')")
    public ResponseEntity<String> deleteToken(@PathVariable String tokenId)
    throws Exception {
    	String decodedToken = URLDecoder.decode(tokenId, "UTF-8");
    	tokenService.delete(decodedToken);
        return new ResponseEntity<String>("", HttpStatus.OK);
    } 

}
