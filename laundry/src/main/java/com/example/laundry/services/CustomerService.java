package com.example.laundry.services;

<<<<<<< HEAD
import com.example.laundry.dto.CustomerResponseDTO;
import com.example.laundry.dto.RegisterRequest;
import com.example.laundry.dto.RegisterResponse;
=======
>>>>>>> 84721bd55a92f8a6da77804fa8a257fe7820d08a
import com.example.laundry.models.user.Customer;

public interface CustomerService extends UserService{
    Customer addCustomer(Customer customer);
<<<<<<< HEAD
    CustomerResponseDTO register(RegisterRequest registerRequest);
=======
>>>>>>> 84721bd55a92f8a6da77804fa8a257fe7820d08a
//    Order bookOrder(Customer customer, LaundryShop laundryShop, Service service, String instructions);
//    void trackOrder(Customer customer, Order order);
//    void makePayment(Customer customer, Order order, String paymentMethod, double amount);
//    List<Order> getOrderHistory(Customer customer);
//    List<LaundryShop> searchShops(Customer customer, String location);
}
