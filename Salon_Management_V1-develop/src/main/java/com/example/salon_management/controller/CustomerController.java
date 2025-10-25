package com.example.salon_management.controller;

import com.example.salon_management.dto.CustomerForm;
import com.example.salon_management.entity.Customer;
import com.example.salon_management.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService service;

    // LIST
    @GetMapping({"", "/", "/list"})
    public String list(@RequestParam(value = "q", required = false) String q,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "name") String sortBy,
                       @RequestParam(defaultValue = "asc") String dir,
                       Model model) {

        Sort sort = "asc".equalsIgnoreCase(dir)
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Customer> data = service.search(q, pageable);

        String nextDir = "asc".equalsIgnoreCase(dir) ? "desc" : "asc";

        model.addAttribute("data", data);
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("dir", dir);
        model.addAttribute("sortByNameUrl", buildUrl(q, 0, size, "name", nextDir));
        model.addAttribute("sortByPointUrl", buildUrl(q, 0, size, "point", nextDir));

        model.addAttribute("hasPrev", data.hasPrevious());
        model.addAttribute("hasNext", data.hasNext());
        model.addAttribute("currentPage", data.getNumber() + 1);
        model.addAttribute("totalPages", data.getTotalPages());
        model.addAttribute("prevUrl", data.hasPrevious() ? buildUrl(q, data.getNumber() - 1, size, sortBy, dir) : null);
        model.addAttribute("nextUrl", data.hasNext() ? buildUrl(q, data.getNumber() + 1, size, sortBy, dir) : null);

        return "customer/list";
    }
    // CREATE
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("form", new CustomerForm());
        model.addAttribute("pageTitle", "Thêm khách hàng");
        model.addAttribute("formAction", "/customers/create");
        model.addAttribute("submitLabel", "Lưu mới");
        model.addAttribute("isEdit", false);
        return "customer/form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("form") CustomerForm form,
                         BindingResult br, RedirectAttributes ra, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("pageTitle", "Thêm khách hàng");
            model.addAttribute("formAction", "/customers/create");
            model.addAttribute("submitLabel", "Lưu mới");
            model.addAttribute("isEdit", false);
            return "customer/form";
        }
        service.create(form);
        ra.addFlashAttribute("msg", "Đã thêm khách hàng mới!");
        return "redirect:/customers";
    }
    // EDIT
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        var c = service.get(id);
        CustomerForm f = new CustomerForm();
        f.setName(c.getName());
        f.setPhone(c.getPhone());
        f.setEmail(c.getEmail());
        f.setMemberType(c.getMemberType());
        f.setPoint(c.getPoint());

        model.addAttribute("form", f);
        model.addAttribute("pageTitle", "Chỉnh sửa khách hàng");
        model.addAttribute("formAction", "/customers/" + id + "/edit");
        model.addAttribute("submitLabel", "Cập nhật");
        model.addAttribute("isEdit", true);
        return "customer/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("form") CustomerForm form,
                         BindingResult br, RedirectAttributes ra, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("pageTitle", "Chỉnh sửa khách hàng");
            model.addAttribute("formAction", "/customers/" + id + "/edit");
            model.addAttribute("submitLabel", "Cập nhật");
            model.addAttribute("isEdit", true);
            return "customer/form";
        }
        service.update(id, form);
        ra.addFlashAttribute("msg", "Đã cập nhật khách hàng!");
        return "redirect:/customers";
    }
    // DELETE
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("msg", "Đã xoá khách hàng!");
        return "redirect:/customers";
    }

    // Helpers
    private String buildUrl(String q, int page, int size, String sortBy, String dir) {
        String kw = (q == null || q.isBlank()) ? "" : q.trim().replace(" ", "%20");
        return "/customers?q=" + kw + "&page=" + page + "&size=" + size + "&sortBy=" + sortBy + "&dir=" + dir;
    }
}