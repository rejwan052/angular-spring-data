package skyglass.demo.service.impl.security;

import org.springframework.stereotype.Service;

import skyglass.demo.data.security.AuthorityData;
import skyglass.demo.model.security.Authority;
import skyglass.demo.service.AbstractNameService;
import skyglass.demo.service.security.AuthorityService;

@Service
public class AuthorityServiceImpl extends AbstractNameService<Authority, Long, AuthorityData> 
			implements AuthorityService {	

}
