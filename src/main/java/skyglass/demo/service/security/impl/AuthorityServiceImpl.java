package skyglass.demo.service.security.impl;

import org.springframework.stereotype.Service;

import skyglass.demo.data.model.security.Authority;
import skyglass.demo.data.security.AuthorityData;
import skyglass.demo.service.GenericServiceImpl;
import skyglass.demo.service.error.ServiceException;
import skyglass.demo.service.security.AuthorityService;

@Service
public class AuthorityServiceImpl extends GenericServiceImpl<Authority, Long, AuthorityData> 
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
