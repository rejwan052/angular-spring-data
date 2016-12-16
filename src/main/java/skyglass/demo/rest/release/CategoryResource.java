package skyglass.demo.rest.release;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import skyglass.demo.model.release.Category;
import skyglass.demo.rest.AbstractResource;
import skyglass.demo.service.release.CategoryService;

@RestController
@RequestMapping("/rest/release/category")
public class CategoryResource extends AbstractResource<Category, Long, CategoryService> {

}
