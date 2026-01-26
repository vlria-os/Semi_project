package com.example.demo.mapper;

import com.example.demo.dto.GuestBoardDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GuestBoardMapper {
    List<GuestBoardDto> selectList();
    GuestBoardDto selectOne(int id);
    int insert(GuestBoardDto dto);
    int delete(@Param("id") int id,@Param("password") String password);
    int increaseViewCount(int id);

    //검색용
    List<GuestBoardDto> searchByCategory(String category);
    List<GuestBoardDto> searchByTitle(String keyword);

    int updateBoard(GuestBoardDto dto);

    int checkPassword(@Param("id") int id,
                      @Param("password") String password);

    List<GuestBoardDto> searchAll(@Param("keyword") String keyword);
}
