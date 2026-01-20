package com.example.demo.controller;

import com.example.demo.dto.GuestBoardDto;
import com.example.demo.service.GuestBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/guest")
public class GuestBoardController {
    private final GuestBoardService service;

    //카테고리 목록 상수화
    private final List<String> categories = List.of(
            "공지사항", "제품문의", "재고문의", "배송문의", "일반문의", "기타");

    //글쓰기 폼
    @GetMapping("/write")
    public String writeForm(Model model) {
        model.addAttribute("board", new GuestBoardDto()); //빈Dto
        model.addAttribute("categories", categories);
        model.addAttribute("mode", "write");//글쓰기 모드
        return "guest/write";//write.html사용
    }

    //글쓰기 처리
    @PostMapping("/write")
    public String write(GuestBoardDto dto, Model model) {
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            model.addAttribute("error", "비밀번호는 필수 입니다.");
            model.addAttribute("categories", categories);
            model.addAttribute("mode", "write");
            return "guest/write";
        }

        boolean success = service.write(dto);
        if (success) {
            return "redirect:/guest/list";//성공하면 목록으로
        } else {
            model.addAttribute("error", "글쓰기 실패!다시 시도해주세요.");
            model.addAttribute("categories", categories);
            model.addAttribute("mode", "write");
            return "guest/write";//실패하면 작성폼 다시 보여줌
        }
    }

    //목록+검색+페이징
    @GetMapping("/list")
    public String list(
            @RequestParam(defaultValue = "0")int page, //요청에 page가 없으면 0으로
            @RequestParam(required = false)String type,
            @RequestParam(required = false)String keyword,
            Model model) {

        int pageSize=5; //한 페이지 수
        List<GuestBoardDto> filteredList;

        //1.검색 여부 확인
        if (keyword != null && !keyword.isBlank()) {
            if ("category".equals(type)) {
                // 카테고리 검색
                filteredList = service.searchByCategory(keyword);
            } else {
                // 전체 검색: 제목, 작성자, 내용 등
                filteredList = service.searchAll(keyword);
            }
        } else {
            // 검색 키워드가 없으면 전체 목록
            filteredList = service.getList();
        }

        //2.전체 글 수와 페이지 수 계산
        int start=page*pageSize;
        int end=Math.min(start + pageSize,filteredList.size());
        List<GuestBoardDto> list=start<filteredList.size() ? filteredList.subList(start,end):List.of();
        int totalPages=(int)Math.ceil((double) filteredList.size()/pageSize);

        model.addAttribute("list", list);
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages",totalPages);
        model.addAttribute("type",type);
        model.addAttribute("keyword",keyword);
        model.addAttribute("categories",categories);
        return "guest/list";
    }

    //상세 보기
    @GetMapping("/view/{id}")
    public String view(@PathVariable int id, Model model) {
        service.increaseViewCount(id);
        model.addAttribute("board", service.getOne(id));
        return "guest/view";
    }

    //삭제
    @PostMapping("/delete")
    public String delete(@RequestParam int id,
                         @RequestParam String password, RedirectAttributes ra) {
        boolean success = service.delete(id, password);
        if (success) {
            return "redirect:/guest/list";
        } else {
            ra.addFlashAttribute("error", "삭제 실패!! 비밀번호를 확인하세요");
            return "redirect:/guest/view" + id;

            //만약 redirect를 안쓰고 싶다면
//            @PostMapping("/delete")
//            public String delete(@RequestParam int id,
//            @RequestParam String password, Model model){
//
//                boolean success=service.delete(id,password);
//
//                if(success){
//                    return "redirect:/guest/list";
//            else{
//                model.addAttribute("error","삭제 실패!! 비밀번호를 확인하세요");
//                model.addAttribute("board", service.getOne(id));
//                return "guest/view";
//            }

        }
    }

    //검색
    @GetMapping("/search")
    public String search(@RequestParam String type,
                         @RequestParam String keyword, Model model) {
        List<GuestBoardDto> list;
        if (type.equals("category")) {
            list = service.searchByCategory(keyword);
        } else { //title
            list = service.searchByTitle(keyword);
        }
        model.addAttribute("list", list);
        return "guest/list";
    }



    //1.수정 비밀번호 체크화면
    @GetMapping("/edit/check/{id}")
    public String editCheckForm(@PathVariable int id, Model model) {
        model.addAttribute("id", id);
        return "guest/edit-check";  //수정버튼 누르면 이동하고 비밀번호 입력 화면만 보여줌
    }

    //2.실제 수정 화면
    @GetMapping("/edit/{id:\\d+}")
    public String editForm(@PathVariable int id, Model model) {
        GuestBoardDto board = service.getOne(id);
        model.addAttribute("board", board);
        model.addAttribute("categories", categories);
        model.addAttribute("mode", "edit"); //여기서 모드 구분
        return "guest/edit";//글쓰기 화면과 동일한 템플릿 사용
    }
    //3. 수정 처리
    @PostMapping("/edit")//post요청 매핑 추가 필요
    public String edit(GuestBoardDto dto, Model model) {
        boolean success = service.update(dto);//<--여기 이름 수정
        if (!success) {
            model.addAttribute("error", "비밀번호가 틀리거나 글이 없습니다.");
            model.addAttribute("board", dto);
            model.addAttribute("categories", categories);
            model.addAttribute("mode", "edit"); //다시 edit모드
            return "guest/edit"; //다시 수정 품 보여줌
        }

        return "redirect:/guest/view/" + dto.getId();
    }

    //4.수정 비밀번호 검증 처리
    @PostMapping("/edit/check")
    public String edirCheck(@RequestParam int id,
                            @RequestParam String password,
                            RedirectAttributes ra) {

        boolean ok = service.checkPassword(id, password);

        if (!ok) {
            ra.addFlashAttribute("error", "비밀번호가 틀렸습니다.");
            return "redirect:/guest/edit/check/" + id;
        }
        return "redirect:/guest/edit/" + id;
    }

    //페이징처리


}