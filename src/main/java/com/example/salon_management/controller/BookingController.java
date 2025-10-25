package com.example.salon_management.controller;

import com.example.salon_management.dto.BookingForm;
import com.example.salon_management.entity.Booking;
import com.example.salon_management.entity.Customer;
import com.example.salon_management.entity.ServiceItem;
import com.example.salon_management.repository.CustomerRepository;
import com.example.salon_management.repository.ServiceItemRepository;
import com.example.salon_management.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final CustomerRepository customerRepo;
    private final ServiceItemRepository serviceItemRepo;

    public BookingController(BookingService bookingService,
                             CustomerRepository customerRepo,
                             ServiceItemRepository serviceItemRepo) {
        this.bookingService = bookingService;
        this.customerRepo = customerRepo;
        this.serviceItemRepo = serviceItemRepo;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("bookings", bookingService.findAll());
        return "booking/list";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("form", new BookingForm());
        model.addAttribute("customers", customerRepo.findAll());
        model.addAttribute("services", serviceItemRepo.findAll());
        return "booking/form";
    }

    @PostMapping
    public String save(@ModelAttribute("form") BookingForm form) {
        Booking b = new Booking();
        b.setCode(form.getCode());
        b.setCustomer(customerRepo.findById(form.getCustomerId()).orElse(null));
        b.getServiceItems().addAll(serviceItemRepo.findAllById(form.getServiceItemIds()));
        b.setBookedAt(form.getBookedAt());
        b.setStatus(form.getStatus() == null ? "CHO_XU_LY" : form.getStatus());
        bookingService.save(b);
        return "redirect:/bookings";
    }
}
