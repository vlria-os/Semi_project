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
    private String color;

    private Map<String, Object> extendedProps;
}
