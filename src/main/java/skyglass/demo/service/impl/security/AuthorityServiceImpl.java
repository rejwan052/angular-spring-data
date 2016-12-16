package skyglass.demo.service.impl.security;

import org.springframework.stereotype.Service;

import skyglass.demo.data.security.AuthorityData;
import skyglass.demo.model.security.Authority;
import skyglass.demo.service.AbstractService;
import skyglass.demo.service.ServiceException;
import skyglass.demo.service.security.AuthorityService;

@Service
public class AuthorityServiceImpl extends AbstractService<Authority, Long, AuthorityData> 
			implements AuthorityService {
	
	@Override
	public Authority save(Authority authority) throws ServiceException {
    	if (authority.getId() != null) {
    		Authority oldAuthority = findOne(authority.getId());
    		if (!oldAuthority.getName().equals(authority.getName())) {
    			checkNameExists(authority);
    		}
    	} else {
			checkNameExists(authority);    		
    	}
    	return super.save(authority);
	}
	
	private void checkNameExists(Authority authority) throws ServiceException {
		Authority test = repository.findByName(authority.getName());
		if (test != null) {
	        throw new ServiceException("saveRoleError",
	        		"Role with the name '" + authority.getName() + "' already exists");
		}		
	}	

}
