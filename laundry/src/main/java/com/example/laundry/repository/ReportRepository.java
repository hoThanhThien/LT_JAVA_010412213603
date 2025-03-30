package com.example.laundry.repository;

import com.example.laundry.models.order.Payment;
import com.example.laundry.models.report.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report,Integer> {
//    Report generateReport(String shopName, String reportName, String startDate, String endDate);
}
