package com.example.duan2salon.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// @Profile("dev") // bật dòng này nếu chỉ muốn chạy ở profile dev
import com.example.duan2salon.entity.ServiceItem;
import com.example.duan2salon.entity.ProductUsage;
import com.example.duan2salon.repository.ServiceItemRepository;
import com.example.duan2salon.repository.ProductUsageRepository;
import com.example.duan2salon.repository.CustomerRepository;
import com.example.duan2salon.entity.Customer;
import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataInitializer {

    // ===== Seed ServiceItem =====
    @Bean
    CommandLineRunner seedServiceItems(ServiceItemRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.saveAll(List.of(
                        makeService("Cắt tóc nam", "cắt tóc", 80000, 30),
                        makeService("Gội đầu thư giãn", "chăm sóc da đầu", 60000, 20),
                        makeService("Sơn gel cơ bản", "nail", 120000, 45),
                        makeService("Gội & Sấy", "cắt tóc", 70000, 25),
                        makeService("Massage mặt", "chăm sóc da", 150000, 40)
                ));
            }
        };
    }

    // ===== Seed ProductUsage =====
    @Bean
    CommandLineRunner seedProductUsage(ProductUsageRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(ProductUsage.builder()
                        .serviceCode("DV001")
                        .productName("Kem uốn tóc")
                        .quantityUsed(2)
                        .price(BigDecimal.valueOf(150000))
                        .build());

                repo.save(ProductUsage.builder()
                        .serviceCode("DV002")
                        .productName("Thuốc nhuộm A")
                        .quantityUsed(1)
                        .price(BigDecimal.valueOf(220000))
                        .build());
            }
        };
    }
    @Bean
    CommandLineRunner seedCustomers(CustomerRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(Customer.builder().name("Nguyễn Văn A").phone("0911111111")
                        .email("a@example.com").memberType("VIP").point(120).build());
                repo.save(Customer.builder().name("Trần Thị B").phone("0922222222")
                        .email("b@example.com").memberType("Thường").point(40).build());
                repo.save(Customer.builder().name("Lê Văn C").phone("0933333333")
                        .email("c@example.com").memberType("Vàng").point(300).build());
            }
        };
    }

    // -------- helper --------
    private ServiceItem makeService(String name, String type, int price, int minutes) {
        ServiceItem s = new ServiceItem();
        s.setName(name);
        s.setType(type);
        s.setPrice(BigDecimal.valueOf(price)); // ưu tiên valueOf
        s.setDurationMinutes(minutes);
        s.setActive(true);
        return s;
    }
}
