package com.example.salon_management.controller;

import com.example.salon_management.dto.EmployeeSearchRequest;
import com.example.salon_management.entity.Employee;
import com.example.salon_management.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService service;

    @GetMapping
    public String list(@ModelAttribute("q") EmployeeSearchRequest q, Model model) {
        Pageable pageable = PageRequest.of(q.getPage(), q.getSize(), q.getSort());
        Page<Employee> data = service.search(q.getKeyword(), pageable);

        model.addAttribute("data", data);
        model.addAttribute("q", q);
        model.addAttribute("pageTitle", "Danh sách nhân viên");
        return "employee/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("pageTitle", "Thêm nhân viên mới");
        model.addAttribute("formAction", "/employees/create");
        return "employee/form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("employee") Employee e, BindingResult br, RedirectAttributes ra) {
        if (br.hasErrors()) return "employee/form";
        service.create(e);
        ra.addFlashAttribute("msg", "Thêm nhân viên thành công!");
        return "redirect:/employees";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("employee", service.get(id));
        model.addAttribute("pageTitle", "Chỉnh sửa nhân viên");
        model.addAttribute("formAction", "/employees/" + id + "/edit");
        return "employee/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("employee") Employee e,
                         BindingResult br, RedirectAttributes ra) {
        if (br.hasErrors()) return "employee/form";
        service.update(id, e);
        ra.addFlashAttribute("msg", "Cập nhật nhân viên thành công!");
        return "redirect:/employees";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("msg", "Đã xoá nhân viên!");
        return "redirect:/employees";
    }
}
