package skyglass.demo.service.security.impl;

import org.springframework.stereotype.Service;

import skyglass.demo.data.model.security.Token;
import skyglass.demo.data.security.TokenData;
import skyglass.demo.service.GenericServiceImpl;
import skyglass.demo.service.security.TokenService;

@Service
public class TokenServiceImpl extends GenericServiceImpl<Token, String, TokenData> 
			implements TokenService {

}
