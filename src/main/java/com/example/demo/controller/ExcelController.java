package com.example.demo.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.example.demo.dto.ApprovalDto;
import com.example.demo.dto.ProgressInDto;
import com.example.demo.dto.ProgressOutDto;
import com.example.demo.service.ExcelService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExcelController {
    private final ExcelService excelService;

    @GetMapping("/download")
    public void download(HttpServletResponse response) throws IOException {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = URLEncoder.encode("approval_list"+date+".xlsx", "UTF-8").replaceAll("\\+", "%20");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        List<ProgressInDto> list=excelService.download();

        List<ProgressOutDto> list_out=excelService.download_out();

        try(ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build()){
            // 첫 번째 시트
            WriteSheet sheetIn = EasyExcel.writerSheet(0, "Inbound List").head(ProgressInDto.class).build();
            excelWriter.write(list, sheetIn);

            // 두 번째 시트
            WriteSheet sheetOut = EasyExcel.writerSheet(1, "Outbound List").head(ProgressOutDto.class).build();
            excelWriter.write(list_out, sheetOut);
        }

    }
}
