package com.ilp.ilp_submission_2.request;

import com.ilp.ilp_submission_2.constant.OrderStatus;
import com.ilp.ilp_submission_2.constant.OrderValidationCode;

/**
 * This class is a OrderValidation object that is used to validate an order.
 * It contains the order status and the order validation code.
 */
public record OrderValidation(OrderStatus orderStatus, OrderValidationCode orderValidationCode) {
}
