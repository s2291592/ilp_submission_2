package com.ilp.ilp_submission_2.validators;

import com.ilp.ilp_submission_2.constant.OrderValidationCode;
import com.ilp.ilp_submission_2.data.CreditCardInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreditCardValidatorTest {

    private CreditCardValidator creditCardValidator;

    @BeforeEach
    void setUp() {
        creditCardValidator = new CreditCardValidator();
    }

    @Test
    void testValidateCreditCard_NullCreditCardInfo() {
        CreditCardInformation nullCardInfo = null;
        OrderValidationCode result = creditCardValidator.validateCreditCard(nullCardInfo);
        assertEquals(OrderValidationCode.CARD_NUMBER_INVALID, result, "Null credit card info should return CARD_NUMBER_INVALID");
    }

    @Test
    void testValidateCreditCard_InvalidCardNumber() {
        CreditCardInformation ccInfo = new CreditCardInformation("123", "12/25", "123");
        OrderValidationCode result = creditCardValidator.validateCreditCard(ccInfo);
        assertEquals(OrderValidationCode.CARD_NUMBER_INVALID, result, "Invalid credit card number should return CARD_NUMBER_INVALID");
    }

    @Test
    void testValidateCreditCard_InvalidExpiryDate() {
        CreditCardInformation ccInfo = new CreditCardInformation("4485959141852684", "invalid", "123");
        OrderValidationCode result = creditCardValidator.validateCreditCard(ccInfo);
        assertEquals(OrderValidationCode.EXPIRY_DATE_INVALID, result, "Invalid expiry date should return EXPIRY_DATE_INVALID");
    }

    @Test
    void testValidateCreditCard_InvalidCvv() {
        CreditCardInformation ccInfo = new CreditCardInformation("4485959141852684", "12/25", "12");
        OrderValidationCode result = creditCardValidator.validateCreditCard(ccInfo);
        assertEquals(OrderValidationCode.CVV_INVALID, result, "Invalid CVV should return CVV_INVALID");
    }

    @Test
    void testValidateCreditCard_ValidCreditCard() {
        CreditCardInformation ccInfo = new CreditCardInformation("4485959141852684", "12/25", "123");
        OrderValidationCode result = creditCardValidator.validateCreditCard(ccInfo);
        assertNull(result, "Valid credit card info should return null");
    }
}
