package skyglass.demo.service.impl.security;

import org.springframework.stereotype.Service;

import skyglass.demo.data.security.TokenData;
import skyglass.demo.model.security.Token;
import skyglass.demo.service.AbstractService;
import skyglass.demo.service.security.TokenService;

@Service
public class TokenServiceImpl extends AbstractService<Token, String, TokenData> 
			implements TokenService {

}
