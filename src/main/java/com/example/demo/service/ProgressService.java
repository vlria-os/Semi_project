package com.example.demo.service;

import com.example.demo.dto.ProgressInDto;
import com.example.demo.dto.ProgressOutDto;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.ProgressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgressService {
    private final ProgressMapper progressMapper;

    public List<ProgressInDto> list(){
        return progressMapper.select_in();
    }

//    public List<ProgressOutDto> list_out(){
//
//    }
}
