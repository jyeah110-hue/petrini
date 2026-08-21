package com.petcare.petcare.store.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.petcare.petcare.store.vo.BrandVO;
import com.petcare.petcare.store.vo.CategoryVO;
import com.petcare.petcare.store.vo.ProductVO;
import com.petcare.petcare.store.vo.SearchVO;

@Mapper
public interface StoreMapper {
    public List<ProductVO> selectProductList(SearchVO vo) throws Exception;
    public List<CategoryVO> selectCategoryList() throws Exception;
    public List<BrandVO> selectBrandList() throws Exception;
}
