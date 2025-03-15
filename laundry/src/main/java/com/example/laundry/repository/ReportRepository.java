package com.example.laundry.repository;

import com.example.laundry.models.order.Payment;
import com.example.laundry.models.report.Report;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository {
    Report generateReport(String shopName, String reportName, String startDate, String endDate);
}
