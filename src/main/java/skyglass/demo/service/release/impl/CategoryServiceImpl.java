package skyglass.demo.service.release.impl;

import org.springframework.stereotype.Service;

import skyglass.demo.data.model.release.Category;
import skyglass.demo.data.release.CategoryData;
import skyglass.demo.service.GenericServiceImpl;
import skyglass.demo.service.release.CategoryService;

@Service
public class CategoryServiceImpl extends GenericServiceImpl<Category, Long, CategoryData> 
			implements CategoryService {

}
