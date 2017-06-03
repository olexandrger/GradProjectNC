package com.grad.project.nc.controller.api.admin;

import com.grad.project.nc.controller.api.dto.FrontendProduct;
import com.grad.project.nc.controller.api.dto.TypeaheadItem;
import com.grad.project.nc.model.Product;
import com.grad.project.nc.service.product.ProductService;
import com.grad.project.nc.support.pagination.Page;
import com.grad.project.nc.support.validation.ProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductsController {

    private final ProductService productService;
    private final ProductValidator productValidator;

    @Autowired
    public AdminProductsController(ProductService productService, ProductValidator productValidator) {
        this.productService = productService;
        this.productValidator = productValidator;
    }

    @RequestMapping(value = "/{productId}", method = RequestMethod.PUT)
    public ResponseEntity<FrontendProduct> update(@RequestBody FrontendProduct frontendProduct) {
        productValidator.validate(frontendProduct);

        Product product = frontendProduct.toModel();
        productService.update(product);

        FrontendProduct updatedProduct = FrontendProduct.fromEntity(productService.find(product.getProductId()));
        return ResponseEntity.ok(updatedProduct);
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody FrontendProduct frontendProduct) {
        productValidator.validate(frontendProduct);

        Product result = productService.add(frontendProduct.toModel());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getProductId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    public FrontendProduct getById(@PathVariable("productId") Long productId) {
        return FrontendProduct.fromEntity(productService.find(productId));
    }

    @RequestMapping(params = { "page", "amount" }, method = RequestMethod.GET)
    public Page<FrontendProduct> getPaginated(@RequestParam("page") int page, @RequestParam("amount") int amount) {
        Page<Product> productPage = productService.findPaginated(page, amount);

        List<FrontendProduct> content = productPage.getContent()
                .stream()
                .map(FrontendProduct::fromEntity)
                .collect(Collectors.toList());


        return new Page<>(content, productPage.getTotalPages());
    }

    @RequestMapping(value = "/search", params = {"query"}, method = RequestMethod.GET)
    public List<TypeaheadItem> fetchProductTypeaheadItemsByQuery(@RequestParam("query") String query) {
        return productService.findByNameContaining(query)
                .stream()
                .map(product -> new TypeaheadItem(product.getProductId(), product.getProductName()))
                .collect(Collectors.toList());
    }
}
