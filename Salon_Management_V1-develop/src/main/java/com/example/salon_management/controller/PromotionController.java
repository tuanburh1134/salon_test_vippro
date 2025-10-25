package com.example.salon_management.controller;

import com.example.salon_management.dto.PromotionForm;
import com.example.salon_management.entity.Promotion;
import com.example.salon_management.service.PromotionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/promotions")
public class PromotionController {

    private final PromotionService service;

    public PromotionController(PromotionService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("promotions", service.findAll());
        model.addAttribute("form", new PromotionForm());
        return "promotion/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new PromotionForm());
        return "promotion/form";
    }

    @PostMapping
    public String create(@ModelAttribute("form") PromotionForm form,
                         BindingResult br,
                         RedirectAttributes ra) {
        if (br.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.form", br);
            ra.addFlashAttribute("form", form);
            return "redirect:/promotions/new";
        }
        Promotion p = new Promotion();
        p.setCode(form.getCode());
        p.setName(form.getName());
        p.setPercent(form.getPercent());
        p.setStartAt(form.getStartAt());
        p.setEndAt(form.getEndAt());
        // UI: ACTIVE/INACTIVE -> DB: active boolean
        p.setActive(form.getStatus() == null || "ACTIVE".equalsIgnoreCase(form.getStatus()));

        service.save(p);
        ra.addFlashAttribute("msg", "Đã lưu khuyến mãi");
        return "redirect:/promotions";
    }
}
