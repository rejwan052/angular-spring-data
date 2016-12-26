package skyglass.demo.service.release;

import org.springframework.stereotype.Service;

import skyglass.data.service.AbstractSecuredNameService;
import skyglass.demo.data.release.CategoryData;
import skyglass.demo.model.release.Category;

@Service
public class CategoryService extends AbstractSecuredNameService<Category, Long, CategoryData> {
	
}
