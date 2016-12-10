package skyglass.demo.data.release;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import skyglass.demo.data.model.release.Release;
import skyglass.demo.data.model.release.Publisher;

public interface ReleaseData extends JpaRepository<Release, Long> {

    public Collection<Release> findByPublisher(Publisher publisher);

    public List<Release> findByCategoryIdIn(List<Long> ids);

}
