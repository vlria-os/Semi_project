package com.example.demo.service;

import com.example.demo.dto.NoticeDto;
import com.example.demo.dto.NoticeReplyDto;
import com.example.demo.mapper.NoticeMapper;
import com.example.demo.pagination.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeMapper noticeMapper;

    public Map<String, Object> getNoticeList(
            int pageNum, String startDate, String endDate, String keyword) {
        int totalRowCount=noticeMapper.count(startDate, endDate, keyword);
        PageInfo pageInfo=new PageInfo(pageNum,10,5,totalRowCount);

        List<NoticeDto> list = noticeMapper.selectNoticeList(pageInfo, startDate, endDate, keyword);

        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("pageInfo", pageInfo);
        return map;
    }

    // 상단 고정 공지 조회
    public List<NoticeDto> getPinnedNotices() {
        return noticeMapper.selectPinnedNotices();
    }

    // 등록
    public int saveNotice(NoticeDto notice) {
        if("Y".equals(notice.getPinYn())){
            int count= noticeMapper.selectPinnedNotices().size();
            if(count>=3){
                notice.setPinYn("N");
            }
        }
        return noticeMapper.insertNotice(notice);
    }

    // 상세
    public NoticeDto getNoticeDetail(Long noticeId) {
        noticeMapper.increaseViewCount(noticeId);
        return noticeMapper.selectNoticeDetail(noticeId);
    }

    // 수정
    public int updateNotice(NoticeDto notice) {
        return noticeMapper.updateNotice(notice);
    }

    // 삭제
    @Transactional
    public void deleteNotice(Long noticeId) {
        noticeMapper.deleteRepliesByNoticeId(noticeId);

        NoticeDto notice= noticeMapper.selectNoticeDetail(noticeId);
        if(notice != null && notice.getSaveFileName() != null){
            File file= new File(notice.getFilePath() + notice.getSaveFileName());
            if(file.exists()){
                boolean deleted= file.delete();
                if(deleted){
                    System.out.println("File Deleted:" + notice.getSaveFileName());
                }
            }
        }
        noticeMapper.deleteNotice(noticeId);
    }

    public List<NoticeReplyDto> getRepliesByNoticeId(Long noticeId){return noticeMapper.selectRepliesbyNoticeId(noticeId);}

    public void saveReply(NoticeReplyDto dto) {
        if (dto.getParentReplyId() != null){
            int currentChildCount= noticeMapper.countChildReplies(dto.getParentReplyId());
            if(currentChildCount >= 5){
                return;
            }
        }
        noticeMapper.insertReply(dto);
    }

    public int updateReply(NoticeReplyDto replyDto){return noticeMapper.updateReply(replyDto);};
    public int deleteReply(Long replyId){return noticeMapper.deleteReply(replyId);};
}