package com.example.demo.service;

import com.example.demo.dto.WebuserDto;
import com.example.demo.mapper.LoginMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final LoginMapper mapper;

    public WebuserDto selectWebuser(String id){
        return mapper.selectWebuser(id);
    }
}
