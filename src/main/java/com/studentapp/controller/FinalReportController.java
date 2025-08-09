package com.studentapp.controller;

import com.studentapp.service.FinalReportService;
import com.studentapp.model.FinalReport;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class FinalReportController {

    private final FinalReportService finalReportService;

    @GetMapping("/admin/reports")
    public String viewReports(Model model) {
        List<FinalReport> reports = finalReportService.getAllReports();
        model.addAttribute("reports", reports);
        return "admin/finalReport";  // JSP name
    }
}