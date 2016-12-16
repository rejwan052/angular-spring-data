package skyglass.demo.data.security;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import skyglass.demo.model.security.Token;

public interface TokenData extends JpaRepository<Token, String> {
	
    List<Token> findByUserLogin(String userLogin);
	
}
