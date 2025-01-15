package com.ilp.ilp_submission_2.controllers;

import com.ilp.ilp_submission_2.constant.OrderStatus;
import com.ilp.ilp_submission_2.constant.OrderValidationCode;
import com.ilp.ilp_submission_2.data.Order;
import com.ilp.ilp_submission_2.model.OrderValidation;
import com.ilp.ilp_submission_2.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ValidateOrderPostController.class)
class ValidateOrderPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService; // Mock the service

    @Test
    void testValidateOrder() throws Exception {
        // Arrange
        Order mockOrder = new Order("12345", null, null, null, 2000, null, null);
        OrderValidation mockValidation = new OrderValidation(OrderStatus.VALID, OrderValidationCode.NO_ERROR);
        when(orderService.validateIncomingOrder(any(Order.class))).thenReturn(mockValidation);

        // Act & Assert
        mockMvc.perform(post("/validateOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"orderNo\": \"12345\", \"priceTotalInPence\": 2000 }"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"orderStatus\":\"VALID\",\"orderValidationCode\":\"NO_ERROR\"}"));
    }
}
