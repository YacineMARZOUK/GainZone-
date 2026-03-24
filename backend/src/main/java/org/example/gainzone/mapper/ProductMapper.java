package org.example.gainzone.mapper;

import org.example.gainzone.dto.request.ProductRequest;
import org.example.gainzone.dto.response.ProductResponse;
import org.example.gainzone.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductRequest request);

    ProductResponse toResponse(Product product);

    List<ProductResponse> toResponseList(List<Product> products);

    void updateEntityFromRequest(ProductRequest request, @MappingTarget Product product);
}
