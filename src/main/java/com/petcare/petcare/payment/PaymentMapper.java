package com.petcare.petcare.payment;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {
    public void insertPayment(PaymentVO vo) throws Exception;
}
