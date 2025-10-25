package com.example.salon_management.controller;

import com.example.salon_management.dto.BookingForm;
import com.example.salon_management.entity.Booking;
import com.example.salon_management.repository.CustomerRepository;
import com.example.salon_management.repository.ServiceItemRepository;
import com.example.salon_management.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
        List<Booking> bookings = bookingService.findAll(); // có thể rỗng

        // Chuẩn bị map để render tên nhân viên & danh sách dịch vụ theo id booking (tránh NPE ở view)
        Map<Long, String> employeeNamesById = new HashMap<>();
        Map<Long, String> serviceNamesById  = new HashMap<>();

        for (Booking b : bookings) {
            // ---- Nhân viên (tùy entity của bạn có field employee hoặc employeeName) ----
            String emp = "---";
            try {
                var m = Booking.class.getMethod("getEmployee"); // nếu có quan hệ Employee
                Object empObj = m.invoke(b);
                if (empObj != null) {
                    var getName = empObj.getClass().getMethod("getName");
                    Object nameVal = getName.invoke(empObj);
                    if (nameVal != null) emp = String.valueOf(nameVal);
                }
            } catch (Exception ignored) { /* không có getEmployee() */ }

            if ("---".equals(emp)) {
                try {
                    var m2 = Booking.class.getMethod("getEmployeeName"); // fallback nếu lưu sẵn tên
                    Object v2 = m2.invoke(b);
                    if (v2 != null) emp = String.valueOf(v2);
                } catch (Exception ignored) { /* không có getEmployeeName() */ }
            }
            employeeNamesById.put(b.getId(), emp);

            // ---- Dịch vụ ----
            List<String> names = new ArrayList<>();
            try {
                if (b.getServiceItems() != null) {
                    b.getServiceItems().forEach(si -> {
                        if (si != null && si.getName() != null) names.add(si.getName());
                    });
                }
            } catch (Exception ignored) { }
            serviceNamesById.put(b.getId(), names.isEmpty() ? "---" : String.join(", ", names));
        }

        model.addAttribute("bookings", bookings);
        model.addAttribute("employeeNamesById", employeeNamesById);
        model.addAttribute("serviceNamesById", serviceNamesById);
        model.addAttribute("hasBookings", bookings != null && !bookings.isEmpty());
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
