package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CalendarDto {
    private String id;
    private String title;
    private String start;
    private String end;
    private boolean allDay;

    // ✅ FullCalendar가 제일 확실하게 먹는 색 필드들
    private String backgroundColor;
    private String borderColor;
    private String textColor;

    private Map<String, Object> extendedProps;
}
