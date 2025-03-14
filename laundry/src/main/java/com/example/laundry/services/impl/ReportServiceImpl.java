package com.example.laundry.services.impl;

import com.example.laundry.models.report.Report;
import com.example.laundry.services.ReportService;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {
    @Override
    public Report generateReport(String shopName, String reportType, String startDate, String endDate) {
        return null;
    }
}
