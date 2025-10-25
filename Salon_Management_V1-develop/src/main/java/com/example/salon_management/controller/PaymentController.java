package com.example.salon_management.controller;

import com.example.salon_management.dto.PaymentForm;
import com.example.salon_management.dto.PaymentSearchRequest;
import com.example.salon_management.entity.PaymentMethod;
import com.example.salon_management.repository.*;
import com.example.salon_management.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;
    private final PromotionRepository promoRepo;
    private final BookingRepository bookingRepo;
    private final CustomerRepository customerRepo;
    private final ServiceItemRepository serviceItemRepo;
    private final ProductUsageRepository productUsageRepo;

    // ============== LIST ==============
    @GetMapping
    public String list(@ModelAttribute("q") PaymentSearchRequest q, Model model) {
        model.addAttribute("payments", service.search(q.getFrom(), q.getTo()));
        return "payment/list";
    }

    // ============== CREATE FORM ==============
    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("form", new PaymentForm());

        // Các biến meta mà template thường dùng
        model.addAttribute("pageTitle", "Thêm thanh toán");
        model.addAttribute("formAction", "/payments");
        model.addAttribute("submitLabel", "Lưu");

        // Dữ liệu cho các select
        // Dùng PageRequest giới hạn để tránh load quá nhiều (tuỳ ý)
        model.addAttribute("bookings", bookingRepo.findAll());                 // nếu template iterate .content thì đổi sang PageRequest.of(...)
        model.addAttribute("customers", customerRepo.findAll());
        model.addAttribute("promotions", promoRepo.findAll());
        model.addAttribute("methods", PaymentMethod.values());
        model.addAttribute("services", serviceItemRepo.findAll());
        model.addAttribute("products", productUsageRepo.findAll());
        return "payment/form";
    }

    // ============== SUBMIT CREATE ==============
    @PostMapping
    public String create(@ModelAttribute("form") PaymentForm form,
                         RedirectAttributes ra,
                         Model model) {
        service.create(form);
        ra.addFlashAttribute("msg", "Đã tạo thanh toán thành công!");
        return "redirect:/payments";
    }
}
