package com.grad.project.nc.controller.api;

import com.grad.project.nc.controller.api.dto.TypeaheadItem;
import com.grad.project.nc.controller.api.dto.catalog.FrontendCatalogProduct;
import com.grad.project.nc.model.Product;
import com.grad.project.nc.service.product.ProductService;
import com.grad.project.nc.support.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/products")
public class ProductsController {

    private final ProductService productService;

    @Autowired
    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    public FrontendCatalogProduct getCatalogProduct(@PathVariable("productId") Long productId) {
        return FrontendCatalogProduct.fromEntity(productService.findCatalogProduct(productId));
    }

    @RequestMapping(params = {"productTypeId", "regionId", "page", "amount"}, method = RequestMethod.GET)
    public Page<FrontendCatalogProduct> getCatalogProducts(@RequestParam("productTypeId") Long productTypeId,
                                                    @RequestParam("regionId") Long regionId,
                                                    @RequestParam("page") int page,
                                                    @RequestParam("amount") int amount) {
        Page<Product> productPage = productService.findActiveByProductTypeIdAndRegionIdPaginated(productTypeId,
                regionId, page, amount);

        List<FrontendCatalogProduct> content = productPage.getContent()
                .stream()
                .map(FrontendCatalogProduct::fromEntity)
                .collect(Collectors.toList());

        return new Page<>(content, productPage.getTotalPages());
    }

    @RequestMapping(value = "/search", params = {"query", "productTypeId", "regionId"}, method = RequestMethod.GET)
    public List<TypeaheadItem> fetchProductTypeaheadItemsByQuery(@RequestParam("query") String query,
                                                                 @RequestParam("productTypeId") Long productTypeId,
                                                                 @RequestParam("regionId") Long regionId) {
        return productService.findByNameContaining(query, productTypeId, regionId)
                .stream()
                .map(product -> new TypeaheadItem(product.getProductId(), product.getProductName()))
                .collect(Collectors.toList());
    }
}
