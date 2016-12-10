package skyglass.demo.data.release;

import org.springframework.data.jpa.repository.JpaRepository;

import skyglass.demo.data.model.release.Category;

public interface CategoryData extends JpaRepository<Category, Long> {
}
