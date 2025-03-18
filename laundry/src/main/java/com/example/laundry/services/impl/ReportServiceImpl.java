package com.example.laundry.services.impl;

import com.example.laundry.models.report.Report;
import com.example.laundry.repository.ReportRepository;
import com.example.laundry.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportRepository reportRepository;

//    @Override
//    public Report generateReport(String shopName, String reportType, String startDate, String endDate) {
//        return reportRepository.generateReport(shopName, reportType, startDate, endDate);
//    }
}
