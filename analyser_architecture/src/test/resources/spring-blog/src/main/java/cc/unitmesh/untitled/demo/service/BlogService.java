package cc.unitmesh.untitled.demo.service;

import cc.unitmesh.untitled.demo.entity.BlogPost;
import cc.unitmesh.untitled.demo.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlogService {
    @Autowired
    BlogRepository blogRepository;

    public BlogPost createBlog(BlogPost blogDto) {
        return blogRepository.save(blogDto);
    }

    public BlogPost getBlogById(Long id) {
        return blogRepository.findById(id).orElse(null);
    }

    public BlogPost updateBlog(Long id, BlogPost blogDto) {
        return blogRepository.findById(id).map(blog -> {
            blog.setTitle(blogDto.getTitle());
            blog.setContent(blogDto.getContent());
            return blogRepository.save(blog);
        }).orElse(null);
    }

    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
