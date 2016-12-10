package skyglass.demo.data.security;

import org.springframework.data.jpa.repository.JpaRepository;

import skyglass.demo.data.model.security.User;


public interface UserData extends JpaRepository<User, Long> {
    User findByLogin(String login);

}
