package skyglass.demo.data.release;

import org.springframework.data.jpa.repository.JpaRepository;

import skyglass.demo.model.release.ReleaseUser;

public interface ReleaseUserData extends JpaRepository<ReleaseUser, Long> {

}
