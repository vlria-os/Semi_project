package com.example.demo.controller.webuser;

import com.example.demo.dto.ConfirmInDetailDto;
import com.example.demo.dto.ConfirmOutDetailDto;
import com.example.demo.dto.ConfirmOutboundDto;
import com.example.demo.mapper.ConfirmMapper;
import com.example.demo.service.ConfirmListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ConfirmDetailController {
    private final ConfirmListService confirmListService;

    @GetMapping("/confirm/detail_in")
    public List<ConfirmInDetailDto> showDetail_in(int inbound_id){
        return confirmListService.Confirm_in_detailList(inbound_id);
    }

    @GetMapping("/confirm/detail_out")
    public List<ConfirmOutDetailDto> showDetail_out(int outbound_id){
        return confirmListService.Confirm_out_detailList(outbound_id);
    }
}
