package com.petcare.petcare.payment;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PaymentVO {
    private Long paymentId;
    private String orderId;
    private String resvId;
    private String method;
    private int amount;
    private String paymentKey;
    private String status;
    private Date created;
}
