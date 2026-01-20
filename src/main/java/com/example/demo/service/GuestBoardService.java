package com.example.demo.service;

import com.example.demo.dto.GuestBoardDto;
import com.example.demo.mapper.GuestBoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GuestBoardService {
    private final GuestBoardMapper mapper;

    public List<GuestBoardDto> getList() {
        return mapper.selectList();
    }

    public GuestBoardDto getOne(int id) {
        return mapper.selectOne(id);
    }

    //글쓰기 성공 여부 boolean반환
    public boolean write(GuestBoardDto dto) {
        int result = mapper.insert(dto);
        return result > 0; //1이면 성공, 0이면 실패
    }

    //삭제 성공 여부 boolean 반환
    public boolean delete(int id, String password) {
        return mapper.delete(id, password) > 0;
    }

    public void increaseViewCount(int id) {
        mapper.increaseViewCount(id);
    }

    //검색메서드
    public List<GuestBoardDto> searchByCategory(String category) {
        return mapper.searchByCategory(category);
    }

    public List<GuestBoardDto> searchByTitle(String keyword) {
        return mapper.searchByTitle(keyword);
    }
    // 글 수정 성공 여부 boolean반환
    public boolean update(GuestBoardDto dto) {
        //반환값:update 성공 시 1, 실패 시 0
        return mapper.updateBoard(dto) > 0;
    }

    public boolean checkPassword(int id, String password) {
        return mapper.checkPassword(id,password)==1;
    }

    //page: 0부터 시작, pageSize: 한 페이지 글 수
    public List<GuestBoardDto> getListByPage(int page, int pageSize){
        int start=page*pageSize;
        int end=start+pageSize;

        List<GuestBoardDto> all=mapper.selectList();//전체 글 가져오기
        if(start>=all.size()) return List.of();//글이 없는 경우
        if(end>all.size()) end=all.size();

        return all.subList(start,end);
    }

    //전체 글 수 가져오기 (페이징 버튼 계산용)
    public int getTotalCount(){
        return mapper.selectList().size();
    }

    public List<GuestBoardDto> searchAll(String keyword){
        return mapper.searchAll(keyword);
    }

}
