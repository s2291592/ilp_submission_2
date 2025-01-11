package com.ilp.ilp_submission_2.validators;

import com.ilp.ilp_submission_2.constant.OrderValidationCode;
import com.ilp.ilp_submission_2.data.CreditCardInformation;
import com.ilp.ilp_submission_2.utils.CreditCardUtils;
import org.springframework.stereotype.Component;

@Component
public class CreditCardValidator {

    /**
     * Validate card number, expiry, and CVV.
     * Return an OrderValidationCode if invalid, or null if OK.
     */
    public OrderValidationCode validateCreditCard(CreditCardInformation ccInfo) {
        if (ccInfo == null) {
            return OrderValidationCode.CARD_NUMBER_INVALID;
        }

        // Check credit card number (16 digits)
        if (!CreditCardUtils.isValidCreditCardNumber(ccInfo.getCreditCardNumber())) {
            return OrderValidationCode.CARD_NUMBER_INVALID;
        }

        // Check expiry date format "MM/YY" (you can add logic for future date)
        if (!CreditCardUtils.isValidExpiry(ccInfo.getCreditCardExpiry())) {
            return OrderValidationCode.EXPIRY_DATE_INVALID;
        }

        // Check CVV is 3 digits
        if (!CreditCardUtils.isValidCvv(ccInfo.getCvv())) {
            return OrderValidationCode.CVV_INVALID;
        }

        return null;
    }
}
