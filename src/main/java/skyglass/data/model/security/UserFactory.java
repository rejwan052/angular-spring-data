package skyglass.data.model.security;

import org.springframework.stereotype.Service;

@Service("userFactory")
public interface UserFactory {
	
	public Long getCurrentUserId();
	
}
