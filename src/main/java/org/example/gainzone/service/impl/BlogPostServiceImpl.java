package org.example.gainzone.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gainzone.dto.request.BlogPostRequest;
import org.example.gainzone.dto.response.BlogPostResponse;
import org.example.gainzone.entity.BlogPost;
import org.example.gainzone.mapper.BlogPostMapper;
import org.example.gainzone.repository.BlogPostRepository;
import org.example.gainzone.service.BlogPostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogPostServiceImpl implements BlogPostService {

    private final BlogPostRepository blogPostRepository;
    private final BlogPostMapper blogPostMapper;

    @Override
    @Transactional
    public BlogPostResponse createBlogPost(BlogPostRequest blogPostRequest) {
        BlogPost blogPost = blogPostMapper.toEntity(blogPostRequest);
        BlogPost savedBlogPost = blogPostRepository.save(blogPost);
        return blogPostMapper.toResponse(savedBlogPost);
    }

    @Override
    @Transactional
    public BlogPostResponse updateBlogPost(Long id, BlogPostRequest blogPostRequest) {
        BlogPost blogPost = blogPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BlogPost not found with id: " + id));
        blogPostMapper.updateEntityFromRequest(blogPostRequest, blogPost);
        BlogPost updatedBlogPost = blogPostRepository.save(blogPost);
        return blogPostMapper.toResponse(updatedBlogPost);
    }

    @Override
    @Transactional
    public void deleteBlogPost(Long id) {
        if (!blogPostRepository.existsById(id)) {
            throw new RuntimeException("BlogPost not found with id: " + id);
        }
        blogPostRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPostResponse> getAllBlogPosts() {
        List<BlogPost> blogPosts = blogPostRepository.findAll();
        return blogPostMapper.toResponseList(blogPosts);
    }

    @Override
    @Transactional(readOnly = true)
    public BlogPostResponse getBlogPostById(Long id) {
        BlogPost blogPost = blogPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BlogPost not found with id: " + id));
        return blogPostMapper.toResponse(blogPost);
    }
}
