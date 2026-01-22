package org.example.gainzone.service;

import org.example.gainzone.dto.request.BlogPostRequest;
import org.example.gainzone.dto.response.BlogPostResponse;

import java.util.List;

public interface BlogPostService {
    BlogPostResponse createBlogPost(BlogPostRequest blogPostRequest);

    BlogPostResponse updateBlogPost(Long id, BlogPostRequest blogPostRequest);

    void deleteBlogPost(Long id);

    List<BlogPostResponse> getAllBlogPosts();

    BlogPostResponse getBlogPostById(Long id);
}
