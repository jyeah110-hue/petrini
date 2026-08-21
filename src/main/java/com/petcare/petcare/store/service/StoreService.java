package com.petcare.petcare.store.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.petcare.petcare.store.mapper.StoreMapper;
import com.petcare.petcare.store.vo.BrandVO;
import com.petcare.petcare.store.vo.CategoryVO;
import com.petcare.petcare.store.vo.ProductVO;
import com.petcare.petcare.store.vo.SearchVO;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreMapper storeMapper;

    private List<CategoryVO> categoryList;
    private List<BrandVO> brandList;

    @PostConstruct
    public void init() throws Exception {
        categoryList = storeMapper.selectCategoryList();
        brandList = storeMapper.selectBrandList();
    }
    
    public List<ProductVO> getProductList(SearchVO vo) throws Exception {
        List<ProductVO> result = storeMapper.selectProductList(vo);
        return result;
    }

    public List<CategoryVO> getCategoryList() {
        return categoryList;
    }

    public List<BrandVO> getBrandList() {
        return brandList;
    }

    // 관리자가 카테고리나 브랜드를 수정했을 때 호출
    public void refreshCache() throws Exception {
        categoryList = storeMapper.selectCategoryList();
        brandList = storeMapper.selectBrandList();
    }
}
