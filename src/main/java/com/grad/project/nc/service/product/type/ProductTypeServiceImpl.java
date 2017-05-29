package com.grad.project.nc.service.product.type;

import com.grad.project.nc.model.Category;
import com.grad.project.nc.model.Product;
import com.grad.project.nc.model.ProductCharacteristic;
import com.grad.project.nc.model.ProductType;
import com.grad.project.nc.persistence.CategoryDao;
import com.grad.project.nc.persistence.CrudDao;
import com.grad.project.nc.persistence.ProductCharacteristicDao;
import com.grad.project.nc.persistence.ProductTypeDao;
import com.grad.project.nc.service.AbstractService;
import com.grad.project.nc.support.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductTypeServiceImpl extends AbstractService<ProductType> implements ProductTypeService {

    private static final String DATA_TYPE_CATEGORY_TYPE_NAME = "DATA_TYPE";

    private final CategoryDao categoryDao;
    private final ProductTypeDao productTypeDao;
    private final ProductCharacteristicDao productCharacteristicDao;

    @Autowired
    public ProductTypeServiceImpl(CategoryDao categoryDao, ProductTypeDao productTypeDao,
                                  ProductCharacteristicDao productCharacteristicDao) {
        this.categoryDao = categoryDao;
        this.productTypeDao = productTypeDao;
        this.productCharacteristicDao = productCharacteristicDao;
    }

    @Override
    @Transactional
    public List<ProductType> findActive() {
        List<ProductType> productTypes = productTypeDao.findByActiveStatus(true);
        productTypes.forEach(pt -> pt.getProductCharacteristics().forEach(ProductCharacteristic::getDataType));
        return productTypes;
    }

    @Override
    @Transactional
    public List<ProductType> findLastN(int n) {
        return productTypeDao.findLastN(n);
    }

    @Override
    @Transactional
    public List<ProductType> findByNameContaining(String productTypeName) {
        return productTypeDao.findByNameContaining(productTypeName);
    }

    @Override
    public Page<ProductType> findPaginated(int page, int amount) {
        int totalPages = (int) Math.ceil(productTypeDao.countTotalProducts() / (double) amount);
        List<ProductType> productTypes = productTypeDao.findPaginated(page, amount);
        productTypes.forEach(pt -> pt.getProductCharacteristics().forEach(ProductCharacteristic::getDataType));

        return new Page<>(productTypes, totalPages);
    }

    @Override
    public List<Category> findProductCharacteristicDataTypes() {
        return categoryDao.findByCategoryTypeName(DATA_TYPE_CATEGORY_TYPE_NAME);
    }

    @Override
    @Transactional
    public ProductType add(ProductType productType) {
        productTypeDao.add(productType);

        productType.getProductCharacteristics()
                .forEach(c -> c.setProductType(new ProductType(productType.getProductTypeId())));

        productCharacteristicDao.persistBatch(productType.getProductCharacteristics());

        return productType;
    }

    @Override
    @Transactional
    public ProductType update(ProductType productType) {
        productTypeDao.update(productType);

        List<ProductCharacteristic> characteristicsToBeDeleted = productCharacteristicDao
                .findByProductTypeId(productType.getProductTypeId())
                .stream()
                .filter(c -> productType.getProductCharacteristics()
                        .stream()
                        .noneMatch(ptc -> c.getProductCharacteristicId().equals(ptc.getProductCharacteristicId())))
                .collect(Collectors.toList());
        productCharacteristicDao.deleteBatch(characteristicsToBeDeleted);

        productCharacteristicDao.updateBatch(productType.getProductCharacteristics()
                .stream()
                .filter(c -> c.getProductCharacteristicId() != null)
                .collect(Collectors.toList())
        );

        productCharacteristicDao.persistBatch(productType.getProductCharacteristics()
                .stream()
                .filter(c -> c.getProductCharacteristicId() == null)
                .collect(Collectors.toList())
        );

        return productType;
    }

    @Override
    @Transactional
    public ProductType find(Long id) {
        ProductType productType = productTypeDao.find(id);
        productType.getProductCharacteristics().forEach(ProductCharacteristic::getDataType);
        return productType;
    }

    @Override
    @Transactional
    public List<ProductType> findAll() {
        List<ProductType> productTypes = productTypeDao.findAll();
        productTypes.forEach(pt -> pt.getProductCharacteristics().forEach(ProductCharacteristic::getDataType));
        return productTypes;
    }

    @Override
    public CrudDao<ProductType> getBackingDao() {
        return productTypeDao;
    }
}
