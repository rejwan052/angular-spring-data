package skyglass.demo.service.release;

import org.springframework.stereotype.Service;

import skyglass.demo.data.release.CategoryData;
import skyglass.demo.model.release.Category;
import skyglass.demo.service.AbstractNameService;

@Service
public class CategoryService extends AbstractNameService<Category, Long, CategoryData> {

}
