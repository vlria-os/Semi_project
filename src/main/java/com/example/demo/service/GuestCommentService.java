package com.example.demo.service;

import com.example.demo.dto.GuestCommentDto;
import com.example.demo.mapper.GuestCommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GuestCommentService {
    private final GuestCommentMapper mapper;


    //댓글 목록
    public List<GuestCommentDto> getComments(int boardId){
        return mapper.selectByBoardId(boardId);
    }

    //댓글 등록
    public void addComment(GuestCommentDto dto){
        mapper.insertComment(dto);
    }
    //댓글 삭제
    public boolean deleteComment(int id, String password) {
//        GuestCommentDto comment = mapper.findById(id,password);
//        if (comment == null) return false; // 댓글이 없으면 false
//        if (!comment.getPassword().equals(password)) return false; // 비밀번호 틀리면 false
        return mapper.delete(id, password) > 0;
    }

}

