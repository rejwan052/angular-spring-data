package skyglass.demo.data.release;

import java.util.Collection;
import java.util.List;

import skyglass.demo.data.INameData;
import skyglass.demo.model.release.Publisher;
import skyglass.demo.model.release.Release;

public interface ReleaseData extends INameData<Release, Long> {

    public Collection<Release> findByPublisher(Publisher publisher);

    public List<Release> findByCategoryIdIn(List<Long> ids);

}
