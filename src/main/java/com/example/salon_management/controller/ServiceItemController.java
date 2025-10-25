package com.example.salon_management.controller;

import com.example.salon_management.dto.ServiceItemRequest;
import com.example.salon_management.dto.ServiceSearchRequest;
import com.example.salon_management.service.ServiceItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport;

@Controller
@RequestMapping("/services")
@RequiredArgsConstructor
public class ServiceItemController {

    private final ServiceItemService service;

    /** Cắt trim toàn bộ String khi bind (tránh " tên ") */
    @InitBinder
    void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override public void setAsText(String text) {
                setValue(text == null ? null : text.trim());
            }
        });
    }

    // ========================= LIST =========================
    @GetMapping({"", "/", "/list"})
    public String list(@ModelAttribute("q") ServiceSearchRequest q, Model model) {
        normalize(q); // đảm bảo sort/page/size hợp lệ

        Sort sort = "desc".equalsIgnoreCase(q.getDir())
                ? Sort.by(q.getSortBy()).descending()
                : Sort.by(q.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(q.getPage(), q.getSize(), sort);

        model.addAttribute("page", service.search(q.getKeyword(), q.getType(), pageable));
        model.addAttribute("q", q);
        return "serviceitem/list";
    }
    // ========================= CREATE =========================
    @GetMapping("/create")
    public String createForm(Model model) {
        prepareForm(model,
                new ServiceItemRequest(),
                "Thêm dịch vụ",
                "/services/create",
                "Lưu");
        return "serviceitem/form";
    }

    @PostMapping("/create")
    public String createSubmit(@Valid @ModelAttribute("form") ServiceItemRequest form,
                               BindingResult br,
                               Model model,
                               RedirectAttributes ra) {
        if (br.hasErrors()) {
            prepareForm(model, form, "Thêm dịch vụ", "/services/create", "Lưu");
            return "serviceitem/form";
        }
        service.create(form);
        ra.addFlashAttribute("msg", "Đã thêm dịch vụ thành công!");
        return "redirect:/services";
    }

    // ========================= EDIT =========================
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        var s = service.findById(id).orElseThrow(); // có thể thay bằng custom NotFound nếu muốn
        prepareForm(model,
                ServiceItemRequest.from(s),
                "Cập nhật dịch vụ",
                "/services/" + id + "/edit",
                "Cập nhật");
        model.addAttribute("id", id); // nếu view cần id hiển thị/khác
        return "serviceitem/form";
    }

    @PostMapping("/{id}/edit")
    public String editSubmit(@PathVariable Long id,
                             @Valid @ModelAttribute("form") ServiceItemRequest form,
                             BindingResult br,
                             Model model,
                             RedirectAttributes ra) {
        if (br.hasErrors()) {
            prepareForm(model, form, "Cập nhật dịch vụ", "/services/" + id + "/edit", "Cập nhật");
            model.addAttribute("id", id);
            return "serviceitem/form";
        }
        service.update(id, form);
        ra.addFlashAttribute("msg", "Đã cập nhật dịch vụ");
        return "redirect:/services";
    }

    // ========================= DELETE =========================
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("msg", "Đã xóa dịch vụ");
        return "redirect:/services";
    }
    // ========================= Helpers =========================
    private void prepareForm(Model model,
                             ServiceItemRequest form,
                             String title,
                             String actionUrl,
                             String submitLabel) {
        model.addAttribute("form", form);
        model.addAttribute("title", title);
        model.addAttribute("actionUrl", actionUrl);
        model.addAttribute("submitLabel", submitLabel);
    }

    private void normalize(ServiceSearchRequest q) {
        if (q.getSortBy() == null || q.getSortBy().isBlank()) q.setSortBy("name");
        if (q.getDir() == null || q.getDir().isBlank()) q.setDir("asc");
        if (q.getPage() < 0) q.setPage(0);
        if (q.getSize() <= 0 || q.getSize() > 100) q.setSize(10);
    }
}
