package com.elearn.blog.repositories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.elearn.blog.entities.Blog;
import com.elearn.blog.entities.Category;
import com.elearn.blog.entities.User;

public interface BlogRepo extends JpaRepository<Blog, Integer> {
	
	Page<Blog> findByUser(User user,Pageable pageable);
	
	Page<Blog> findByCategoryAndVisibleTrue(Category category,Pageable pageable);
	
	Page<Blog> findByTitleContainingAndVisibleTrue(String title,Pageable pageable);
	
	Page<Blog> findByVisible(Boolean visible, Pageable pageable);
}
