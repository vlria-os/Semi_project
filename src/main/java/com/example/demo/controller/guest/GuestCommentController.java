package com.example.demo.controller.guest;

import com.example.demo.dto.GuestCommentDto;
import com.example.demo.service.GuestCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController//Ajax controller
@RequiredArgsConstructor
@RequestMapping("/guest/comment")
public class GuestCommentController {
    private final GuestCommentService service;

    //댓글 목록 조회
    @GetMapping("/{boardId}")
    public List<GuestCommentDto> list(@PathVariable int boardId){
        return service.getComments(boardId);
    }
    //댓글 등록
    @PostMapping
    public String add(@RequestBody GuestCommentDto dto){//@RequestBody는 json으로 받겠다는 것. dto자동 변환
        service.addComment(dto);
        return "success";
    }
    //댓글 삭제(비밀번호 확인 포함)
    @DeleteMapping("/{id}")
    public Map<String,Boolean> deleteComment(@PathVariable int id, @RequestParam String password){
        boolean result=service.deleteComment(id,password);
        return Map.of("success",result); //Ajax에서 결과 확인
    }

}
