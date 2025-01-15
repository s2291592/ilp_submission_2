package com.ilp.ilp_submission_2.data;

import com.ilp.ilp_submission_2.constant.OrderValidationCode;
import com.ilp.ilp_submission_2.constant.OrderStatus;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

/**
 * defines an order in the PizzaDronz service
 */
public final class Order {

    /**
     * the order #
     */
    private String orderNo = "";

    /**
     * the order date
     */
    private LocalDate orderDate = LocalDate.MIN;

    /**
     * the order status
     */
    private OrderStatus orderStatus = OrderStatus.UNDEFINED;

    /**
     * if an order is invalid the reason code for it
     */
    private OrderValidationCode orderValidationCode = OrderValidationCode.UNDEFINED;

    /**
     * the order total in pence
     */
    private int priceTotalInPence = 0;

    /**
     * the pizzas this order consists of
     */
    private Pizza[] pizzasInOrder = new Pizza[0];

    /**
     * the credit card to use for this order
     */
    private CreditCardInformation creditCardInformation = null;

    /**
     * full constructor for an order
     *
     * @param orderNo               the number
     * @param orderDate             is when the order is due (to be delivered)
     * @param orderStatus           is the status
     * @param orderValidationCode   is a reason code for the status (extended explanation)
     * @param priceTotalInPence     is the total price of the order
     * @param pizzasInOrder         is the list of pizzas in the order
     * @param creditCardInformation is the credit card to use
     */
    public Order(String orderNo, LocalDate orderDate, OrderStatus orderStatus, OrderValidationCode orderValidationCode, int priceTotalInPence, Pizza[] pizzasInOrder, CreditCardInformation creditCardInformation) {
        this(orderNo, orderDate, priceTotalInPence, pizzasInOrder, creditCardInformation);

        this.setOrderStatus(orderStatus);
        this.setOrderValidationCode(orderValidationCode);
    }

    /**
     * full constructor for an order
     *
     * @param orderNo               the number
     * @param orderDeliveryDate     is when the order is due (to be delivered)
     * @param priceTotalInPence     is the total price of the order
     * @param pizzasInOrder         is the list of pizzas in the order
     * @param creditCardInformation is the credit card to use
     */
    public Order(String orderNo, LocalDate orderDeliveryDate, int priceTotalInPence, Pizza[] pizzasInOrder, CreditCardInformation creditCardInformation) {
        System.out.println("Order Constructor Called:");
        System.out.println("OrderNo: " + orderNo);
        System.out.println("OrderDate: " + orderDeliveryDate);
        System.out.println("PriceTotalInPence: " + priceTotalInPence);
        this.setOrderNo(orderNo);
        this.setOrderDate(orderDeliveryDate);
        this.setPriceTotalInPence(priceTotalInPence);
        this.setPizzasInOrder(pizzasInOrder);
        this.setCreditCardInformation(creditCardInformation);
    }

    /**
     * bare bone default constructor
     */
    public Order() {

    }

    public Order(String number, LocalDate now, Pizza[] pizzas, Object o, int totalPrice, Object o1) {
        this.orderNo = number;
        this.orderDate = now;
        System.out.println("Order created with status: " + orderStatus);
    }

//    public Order(String number, LocalDate now, Pizza[] pizzas, Object o, int i, Object o1) {
//        this.setOrderNo(number);
//        this.setOrderDate(now);
//        this.setPizzasInOrder(pizzas);
//    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderNo(), getOrderDate(), getOrderStatus(), getOrderValidationCode(), getPriceTotalInPence(), Arrays.hashCode(getPizzasInOrder()));
    }

    /**
     * get the order number
     * @return the order number
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * set the oder number
     * @param orderNo is the number to set
     */
    public void setOrderNo(String orderNo) {

        this.orderNo = orderNo;
        System.out.println("Setting OrderStatus to: " + orderStatus);
    }

    /**
     * get date
     * @return order date
     */
    public LocalDate getOrderDate() {
        return orderDate;
    }

    /**
     * set date
     * @param orderDeliveryDate is the new delivery date
     */
    public void setOrderDate(LocalDate orderDeliveryDate) {
        this.orderDate = orderDeliveryDate;
    }

    /**
     * get the order status
     * @return status
     */
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    /**
     * set the order status
     * @param orderStatus the new status
     */
    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * get the validation result
     * @return the validation code
     */
    public OrderValidationCode getOrderValidationCode() {
        return orderValidationCode;
    }

    /**
     * set the validation result
     * @param orderValidationCode the validation code
     */
    public void setOrderValidationCode(OrderValidationCode orderValidationCode) {
        this.orderValidationCode = orderValidationCode;
    }


    /**
     * get the total price
     * @return the total price of the order in pence
     */
    public int getPriceTotalInPence() {
        return priceTotalInPence;
    }

    /**
     * set the price
     * @param priceTotalInPence is the total order price
     */
    public void setPriceTotalInPence(int priceTotalInPence) {
        this.priceTotalInPence = priceTotalInPence;
    }

    /**
     * which pizzas
     * @return the pizzas in the order
     */
    public Pizza[] getPizzasInOrder() {
        return pizzasInOrder;
    }

    /**
     * set the pizzas
     * @param pizzasInOrder are the pizzas to deliver
     */
    public void setPizzasInOrder(Pizza[] pizzasInOrder) {
        this.pizzasInOrder = pizzasInOrder;
    }

    /**
     * the credit card information
     * @return the credit card info
     */
    public CreditCardInformation getCreditCardInformation() {
        return creditCardInformation;
    }

    /**
     * set the credit card info
     * @param creditCardInformation is the info to be used for credit card
     */
    public void setCreditCardInformation(CreditCardInformation creditCardInformation) {
        this.creditCardInformation = creditCardInformation;
    }
}