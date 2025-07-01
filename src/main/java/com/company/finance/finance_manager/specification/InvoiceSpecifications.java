package com.company.finance.finance_manager.specification;

import com.company.finance.finance_manager.enums.EStatus;
import com.company.finance.finance_manager.entity.Invoice;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InvoiceSpecifications {
    public static Specification<Invoice> withFilters(Map<String, String> filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            filters.forEach((key, value) -> {
                switch (key) {
                    case "companyName":
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("companyName")), "%" + value.toLowerCase() + "%"));
                        break;
                    case "location":
                        predicates.add(criteriaBuilder.equal(root.get("location"), value));
                        break;
                    case "fgsStatus":
                        predicates.add(criteriaBuilder.equal(root.get("fgsStatus"), EStatus.valueOf(value)));
                        break;
                    case "financeStatus":
                        predicates.add(criteriaBuilder.equal(root.get("financeStatus"), EStatus.valueOf(value)));
                        break;
                    case "start_date":
                        try {
                            ZonedDateTime startDate = ZonedDateTime.parse(value);
                            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDate));
                        } catch (Exception ignored) {}
                        break;
                    case "end_date":
                        try {
                            ZonedDateTime endDate = ZonedDateTime.parse(value);
                            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endDate));
                        } catch (Exception ignored) {}
                        break;
                    // Add more filters as needed
                }
            });

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
