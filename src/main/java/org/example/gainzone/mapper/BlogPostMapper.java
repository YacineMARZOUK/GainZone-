package org.example.gainzone.mapper;

import org.example.gainzone.dto.request.BlogPostRequest;
import org.example.gainzone.dto.response.BlogPostResponse;
import org.example.gainzone.entity.BlogPost;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BlogPostMapper {

    BlogPost toEntity(BlogPostRequest request);

    BlogPostResponse toResponse(BlogPost blogPost);

    List<BlogPostResponse> toResponseList(List<BlogPost> blogPosts);

    void updateEntityFromRequest(BlogPostRequest request, @MappingTarget BlogPost blogPost);
}
