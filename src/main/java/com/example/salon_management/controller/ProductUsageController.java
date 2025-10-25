package com.example.salon_management.controller;

import com.example.salon_management.dto.ProductUsageForm;
import com.example.salon_management.entity.ProductUsage;
import com.example.salon_management.service.ProductUsageService;
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
@RequestMapping("/productusage")
public class ProductUsageController {

    private final ProductUsageService service;

    // ======================== LIST ========================
    @GetMapping({"", "/", "/list"})
    public String list(@RequestParam(value = "q", required = false) String q,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "createdAt") String sortBy,
                       @RequestParam(defaultValue = "desc") String dir,
                       Model model) {

        Sort sort = "asc".equalsIgnoreCase(dir)
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductUsage> data = service.search(q, pageable);

        String nextDir = "asc".equalsIgnoreCase(dir) ? "desc" : "asc";

        model.addAttribute("sortByServiceCodeUrl", buildListUrl(q, 0, size, "serviceCode", nextDir));
        model.addAttribute("sortByProductNameUrl", buildListUrl(q, 0, size, "productName", nextDir));
        model.addAttribute("sortByQuantityUrl", buildListUrl(q, 0, size, "quantityUsed", nextDir));
        model.addAttribute("sortByPriceUrl", buildListUrl(q, 0, size, "price", nextDir));

        model.addAttribute("serviceCodeIcon", icon(sortBy, dir, "serviceCode"));
        model.addAttribute("productNameIcon", icon(sortBy, dir, "productName"));
        model.addAttribute("quantityIcon", icon(sortBy, dir, "quantityUsed"));
        model.addAttribute("priceIcon", icon(sortBy, dir, "price"));

        boolean hasPrev = data.hasPrevious();
        boolean hasNext = data.hasNext();

        model.addAttribute("data", data);
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("dir", dir);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("currentPage", data.getNumber() + 1);
        model.addAttribute("totalPages", data.getTotalPages());
        model.addAttribute("prevUrl", hasPrev ? buildListUrl(q, data.getNumber() - 1, size, sortBy, dir) : null);
        model.addAttribute("nextUrl", hasNext ? buildListUrl(q, data.getNumber() + 1, size, sortBy, dir) : null);

        return "productusage/list"; //  Trỏ đúng folder template
    }
    // ======================== CREATE ========================
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("form", new ProductUsageForm());
        model.addAttribute("pageTitle", "Thêm sản phẩm sử dụng");
        model.addAttribute("formAction", "/productusage/create");
        model.addAttribute("submitLabel", "Lưu mới");
        model.addAttribute("isEdit", false);
        return "productusage/form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("form") ProductUsageForm form,
                         BindingResult br,
                         RedirectAttributes ra,
                         Model model) {

        if (br.hasErrors()) {
            model.addAttribute("pageTitle", "Thêm sản phẩm sử dụng");
            model.addAttribute("formAction", "/productusage/create");
            model.addAttribute("submitLabel", "Lưu mới");
            model.addAttribute("isEdit", false);
            return "productusage/form";
        }

        service.create(form);
        ra.addFlashAttribute("msg", "Đã thêm sản phẩm sử dụng mới!");
        return "redirect:/productusage";
    }
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        ProductUsage p = service.get(id);
        ProductUsageForm f = new ProductUsageForm();
        f.setServiceCode(p.getServiceCode());
        f.setProductName(p.getProductName());
        f.setQuantityUsed(p.getQuantityUsed());
        f.setPrice(p.getPrice());

        model.addAttribute("form", f);
        model.addAttribute("pageTitle", "Chỉnh sửa sản phẩm sử dụng");
        model.addAttribute("formAction", "/productusage/" + id + "/edit");
        model.addAttribute("submitLabel", "Cập nhật");
        model.addAttribute("isEdit", true);

        return "productusage/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("form") ProductUsageForm form,
                         BindingResult br,
                         RedirectAttributes ra,
                         Model model) {

        if (br.hasErrors()) {
            model.addAttribute("pageTitle", "Chỉnh sửa sản phẩm sử dụng");
            model.addAttribute("formAction", "/productusage/" + id + "/edit");
            model.addAttribute("submitLabel", "Cập nhật");
            model.addAttribute("isEdit", true);
            return "productusage/form";
        }

        service.update(id, form);
        ra.addFlashAttribute("msg", "Đã cập nhật sản phẩm!");
        return "redirect:/productusage";
    }
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("msg", "Đã xoá sản phẩm!");
        return "redirect:/productusage";
    }
    // ======================== Helpers ========================
    private String buildListUrl(String q, int page, int size, String sortBy, String dir) {
        String query = (q == null || q.isBlank()) ? "" : q.trim().replace(" ", "%20");
        return "/productusage?q=" + query +
                "&page=" + page +
                "&size=" + size +
                "&sortBy=" + sortBy +
                "&dir=" + dir;
    }
    private String icon(String currentSortBy, String dir, String column) {
        if (!column.equals(currentSortBy)) return "";
        return "asc".equalsIgnoreCase(dir) ? "↑" : "↓";
    }
}

