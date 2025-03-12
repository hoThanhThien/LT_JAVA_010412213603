package com.example.laundry.models.report;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Builder;

import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "reportData")
@Table(name = "report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shop_name", nullable = false)
    private String shopName;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false)
    private ReportType reportType;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "generated_date", updatable = false)
    private Date generatedDate;

    @ElementCollection
    @CollectionTable(name = "report_data",
            joinColumns = @JoinColumn(name = "report_id"))
    @MapKeyColumn(name = "data_key")
    @Column(name = "data_value")
    private Map<String, String> reportData = new HashMap<>();

    public enum ReportType {
        FINANCIAL,
        OPERATIONAL,
        CUSTOMER_SATISFACTION,
        EMPLOYEE_PERFORMANCE
    }

    // Custom equals/hashCode để tránh vấn đề với proxy Hibernate
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Report)) return false;
        return id != null && id.equals(((Report) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // Business logic methods
    public void addDataPoint(String key, String value) {
        this.reportData.put(key, value);
    }

    public void removeDataPoint(String key) {
        this.reportData.remove(key);
    }
}
