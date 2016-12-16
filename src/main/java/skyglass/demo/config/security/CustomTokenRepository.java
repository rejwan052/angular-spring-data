package skyglass.demo.config.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import skyglass.demo.data.security.TokenData;
import skyglass.demo.data.security.UserData;
import skyglass.demo.model.security.Token;
import skyglass.demo.model.security.User;

@Component
public class CustomTokenRepository implements PersistentTokenRepository {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
    @Autowired
    protected TokenData tokenData;

    @Autowired
    protected UserData userData;
    
    @Autowired
    protected HttpServletRequest request;
    
    @Autowired
    protected HttpServletResponse response;

	@Override
	public void createNewToken(PersistentRememberMeToken newToken) {
        String login = newToken.getUsername();
        logger.debug("Creating new persistent login for user {" + login + "}");
        User user = userData.findByLogin(login);
        Token token = new Token();
        token.setSeries(newToken.getSeries());
        token.setUserLogin(user.getLogin());
        token.setValue(newToken.getTokenValue());
        token.setDate(newToken.getDate());
        token.setIpAddress(request.getRemoteAddr());
        token.setUserAgent(request.getHeader("User-Agent"));
        saveToken(token);		
	}

	@Override
	public void updateToken(String series, String tokenValue, Date lastUsed) {
		Token token = tokenData.findOne(series);
        token.setDate(lastUsed);
        token.setValue(tokenValue);
        saveToken(token);		
	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		Token token = tokenData.findOne(seriesId);
		if (token == null) {
			return null;
		}
		return new PersistentRememberMeToken(token.getUserLogin(),
				token.getSeries(), token.getValue(), token.getDate());
	}

	@Override
	public void removeUserTokens(String username) {
		tokenData.findByUserLogin(username).forEach(token -> tokenData.delete(token.getSeries()));	
	}
    
    private void saveToken(Token token) {
        try {
            tokenData.save(token);
        } catch (DataAccessException e) {
        	logger.error("Failed to save persistent token ", e);
        }
    }

}
