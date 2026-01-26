package com.example.demo.mapper;

import com.example.demo.dto.GuestCommentDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GuestCommentMapper {
    //댓글 조회
    List<GuestCommentDto> selectByBoardId(@Param("boardId")int boardId); //특정 게시글 댓글 조회
    //댓글 추가
    int insertComment(GuestCommentDto dto);  //댓글작성
    // 댓글 삭제
    int delete(@Param("id")int id,@Param("password")String password);
    int deleteComment(@Param("id")int id);//댓글 삭제
    int checkPassword(@Param("id") int id,@Param("password") String password);

    GuestCommentDto findById(@Param("id")int id,@Param("password") String password);
}
