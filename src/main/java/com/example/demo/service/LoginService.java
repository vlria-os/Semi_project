package com.example.demo.service;

import com.example.demo.dto.WebuserDTO;
import com.example.demo.mapper.LoginMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final LoginMapper mapper;

    public WebuserDTO selectWebuser(String id){
        return mapper.selectWebuser(id);
    }
}
