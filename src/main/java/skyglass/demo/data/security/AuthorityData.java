package skyglass.demo.data.security;

import org.springframework.data.jpa.repository.JpaRepository;

import skyglass.demo.data.model.security.Authority;

public interface AuthorityData extends JpaRepository<Authority, Long> {
	
	public Authority findByName(String name);

}