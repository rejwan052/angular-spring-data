package skyglass.demo.service.release;

import org.springframework.stereotype.Service;

import skyglass.demo.data.release.CategoryData;
import skyglass.demo.model.release.Category;
import skyglass.demo.service.AbstractSecuredNameService;

@Service
public class CategoryService extends AbstractSecuredNameService<Category, Long, CategoryData> {
	
}
