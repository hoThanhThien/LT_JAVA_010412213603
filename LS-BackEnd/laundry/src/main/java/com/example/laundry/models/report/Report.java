package com.example.laundry.models.report;

import jakarta.persistence.*;

import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "reportData")
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

    @JdbcTypeCode(SqlTypes.DATE)
    @Column(name = "start_date")
    private Date startDate;

    @JdbcTypeCode(SqlTypes.DATE)
    @Column(name = "end_date")
    private Date endDate;

    @CreationTimestamp
    @Column(name = "generated_date", updatable = false)
    private Date generatedDate;

    @Builder.Default
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
}
