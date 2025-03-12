package com.backendproject.hotel_system.services;

public interface BookingServices {

}
/*
* public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    CustomerSessionRepository customerSessionRepository;

    public OrderServiceImpl(OrderRepository orderRepository, CustomerSessionRepository customerSessionRepository) {
        this.orderRepository = orderRepository;
        this.customerSessionRepository = customerSessionRepository;
    }

    public Bill generateBill(long userId) throws CustomerSessionNotFound{
        Optional<CustomerSession> customerSessionOp=customerSessionRepository.findActiveCustomerSessionByUserId(userId);
        if(customerSessionOp.isEmpty()){
            throw new CustomerSessionNotFound("Customer Session Not Found ");
        }
        CustomerSession customerSession=customerSessionOp.get();
        List <Order> orders=orderRepository.findOrdersByCustomerSession(customerSession.getId());
        customerSession.setCustomerSessionStatus(CustomerSessionStatus.ENDED);
        customerSessionRepository.save(customerSession);
        Bill bill=new Bill();
        Map<MenuItem, Integer> consolidatedOrderedItems = new HashMap<>();
        for (Order order : orders) {
            for (Map.Entry<MenuItem, Integer> entry : order.getOrderedItems().entrySet()) {
                consolidatedOrderedItems.put(entry.getKey(), consolidatedOrderedItems.getOrDefault(entry.getKey(), 0) + entry.getValue());
            }
        }
        double totalAmount = 0;
        for (Map.Entry<MenuItem, Integer> entry : consolidatedOrderedItems.entrySet()) {
            totalAmount += entry.getKey().getPrice() * entry.getValue();
        }
        double gst=totalAmount*0.05;
        double charge=totalAmount*0.1;
        bill.setOrderedItems(consolidatedOrderedItems);
        bill.setGst(gst);
        bill.setServiceCharge(charge);
        bill.setTotalAmount(totalAmount+gst+charge);
        return bill;
    }
}

* */