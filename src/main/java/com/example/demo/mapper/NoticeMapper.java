package com.example.demo.mapper;

import com.example.demo.dto.NoticeDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface NoticeMapper {
    void insertNotice(NoticeDto notice);
    List<NoticeDto> selectNoticeList(Map<String, Object> param);
    int selectNoticeCount(Map<String,Object>param);
    NoticeDto selectNoteicDetail(Long noticeId);
    void increaseViewCount(Long noticeId);
  }
