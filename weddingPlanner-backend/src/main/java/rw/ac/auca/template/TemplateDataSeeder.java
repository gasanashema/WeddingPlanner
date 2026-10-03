package rw.ac.auca.template;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rw.ac.auca.wedding.WeddingSide;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TemplateDataSeeder implements CommandLineRunner {

    private final PlanningTemplateRepository templateRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (templateRepository.count() == 0) {
            log.info("Seeding initial Rwandan wedding starter templates...");

            // Template 1: Rwandan Traditional Gusaba & Inkwano Home Essentials
            PlanningTemplate traditionalTemplate = PlanningTemplate.builder()
                    .name("Rwandan Traditional Gusaba & Inkwano Home Essentials")
                    .category(TemplateCategory.HOME_PREP)
                    .description("Curated household checklist representing traditional Rwandan wedding provisions for bride and groom sides.")
                    .isActive(true)
                    .build();

            List<TemplateItem> items1 = List.of(
                    TemplateItem.builder()
                            .template(traditionalTemplate)
                            .title("Living Room Sofa Set & Coffee Table")
                            .itemType("FURNITURE")
                            .defaultSide(WeddingSide.GROOM_SIDE)
                            .estimatedBudget(new BigDecimal("1200000.00"))
                            .build(),
                    TemplateItem.builder()
                            .template(traditionalTemplate)
                            .title("Master Bedroom Suite & Bedding")
                            .itemType("BEDDING")
                            .defaultSide(WeddingSide.BRIDE_SIDE)
                            .estimatedBudget(new BigDecimal("850000.00"))
                            .build(),
                    TemplateItem.builder()
                            .template(traditionalTemplate)
                            .title("Kitchenware & Traditional Dining Set (Agaseke & Dishes)")
                            .itemType("KITCHENWARE")
                            .defaultSide(WeddingSide.BRIDE_SIDE)
                            .estimatedBudget(new BigDecimal("450000.00"))
                            .build(),
                    TemplateItem.builder()
                            .template(traditionalTemplate)
                            .title("Double-Door Refrigerator & Microwave")
                            .itemType("APPLIANCES")
                            .defaultSide(WeddingSide.GROOM_SIDE)
                            .estimatedBudget(new BigDecimal("950000.00"))
                            .build(),
                    TemplateItem.builder()
                            .template(traditionalTemplate)
                            .title("Smart TV & Home Theater System")
                            .itemType("APPLIANCES")
                            .defaultSide(WeddingSide.GROOM_SIDE)
                            .estimatedBudget(new BigDecimal("650000.00"))
                            .build(),
                    TemplateItem.builder()
                            .template(traditionalTemplate)
                            .title("First Month Apartment Rent & Caution Deposit")
                            .itemType("RENT_UTILITIES")
                            .defaultSide(WeddingSide.SHARED)
                            .estimatedBudget(new BigDecimal("500000.00"))
                            .build(),
                    TemplateItem.builder()
                            .template(traditionalTemplate)
                            .title("Automatic Washing Machine")
                            .itemType("APPLIANCES")
                            .defaultSide(WeddingSide.SHARED)
                            .estimatedBudget(new BigDecimal("550000.00"))
                            .build()
            );

            traditionalTemplate.setItems(items1);
            templateRepository.save(traditionalTemplate);

            // Template 2: Starter Home Core Procurement
            PlanningTemplate starterTemplate = PlanningTemplate.builder()
                    .name("Starter Home Core Procurement")
                    .category(TemplateCategory.HOME_PREP)
                    .description("Essential starter kit focused on high-priority home setup items.")
                    .isActive(true)
                    .build();

            List<TemplateItem> items2 = List.of(
                    TemplateItem.builder()
                            .template(starterTemplate)
                            .title("Basic Kitchen Utensils & Cookware")
                            .itemType("KITCHENWARE")
                            .defaultSide(WeddingSide.SHARED)
                            .estimatedBudget(new BigDecimal("250000.00"))
                            .build(),
                    TemplateItem.builder()
                            .template(starterTemplate)
                            .title("Orthopedic Mattress & Premium Sheet Sets")
                            .itemType("BEDDING")
                            .defaultSide(WeddingSide.BRIDE_SIDE)
                            .estimatedBudget(new BigDecimal("400000.00"))
                            .build(),
                    TemplateItem.builder()
                            .template(starterTemplate)
                            .title("Gas Cooker & Gas Cylinder")
                            .itemType("APPLIANCES")
                            .defaultSide(WeddingSide.GROOM_SIDE)
                            .estimatedBudget(new BigDecimal("350000.00"))
                            .build(),
                    TemplateItem.builder()
                            .template(starterTemplate)
                            .title("Living Room Curtains & Window Decor")
                            .itemType("FURNITURE")
                            .defaultSide(WeddingSide.BRIDE_SIDE)
                            .estimatedBudget(new BigDecimal("200000.00"))
                            .build()
            );

            starterTemplate.setItems(items2);
            templateRepository.save(starterTemplate);

            log.info("Successfully seeded 2 Rwandan wedding starter templates with {} items.", items1.size() + items2.size());
        }
    }
}
