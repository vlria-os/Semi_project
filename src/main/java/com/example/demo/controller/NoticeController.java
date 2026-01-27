package com.example.demo.controller;

import com.example.demo.dto.NoticeDto;
import com.example.demo.dto.NoticeReplyDto;
import com.example.demo.service.NoticeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;


    // 목록
    @GetMapping("/notice/list")
    public String list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "") String keyword,
            HttpSession session,
            Model model) {

        String sDate= (startDate != null && !startDate.isEmpty() ? startDate : null);
        String eDate= (endDate != null && !endDate.isEmpty() ? endDate : null);
        String keyw =(keyword != null && !keyword.isEmpty() ? keyword : null);

        Map<String, Object> result =
                noticeService.getNoticeList(pageNum, sDate, eDate, keyw);

        var pinnedNoticeService = noticeService.getPinnedNotices();

        model.addAttribute("noticeList", result.get("list"));    //일반공지
        model.addAttribute("pinnedNotices",pinnedNoticeService); //상단고정 공지
        model.addAttribute("pageInfo", result.get("pageInfo"));
        model.addAttribute("keyword", keyword);

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        Integer roleId = (Integer) session.getAttribute("role_id");
        String nav= switch (roleId != null ? roleId : 1){
            case 2 -> "officeNav";
            case 3 -> "fieldNav";
            default -> "adminNav";
        };

        model.addAttribute("navFragment", "fragment/nav/" + nav);
        model.addAttribute("content", "notice/list");

        return "layout";
    }

    // 등록 화면
    @GetMapping("/notice/new")
    public String newForm(Model model, HttpSession session) {

        Integer roleId = (Integer) session.getAttribute("role_id");
        String nav= switch (roleId != null ? roleId : 1){
            case 2 -> "officeNav";
            case 3 -> "fieldNav";
            default -> "adminNav";
        };

        model.addAttribute("navFragment", "fragment/nav/" + nav);
        model.addAttribute("content", "notice/new");
        return "layout";
    }

    // 등록 처리
    @PostMapping("/notice/new")
    public String save(NoticeDto notice, HttpSession session, @RequestParam("file")MultipartFile file) throws IOException {
        if(!file.isEmpty()){
            String uploadDir= "D:/";
            File dir=new File(uploadDir);
            if(!dir.exists()) dir.mkdirs();

            String orgFileName= file.getOriginalFilename();
            String saveFileName= UUID.randomUUID() + "_" + orgFileName;

            file.transferTo(new File(uploadDir + saveFileName));

            notice.setOrgFileName(orgFileName);
            notice.setSaveFileName(saveFileName);
            notice.setFilePath(uploadDir);
        }

        Integer roleId= (Integer) session.getAttribute("role_id");
        if(roleId == null || roleId != 1){
            notice.setPinYn("N");
        }else if(notice.getPinYn() == null){
            notice.setPinYn("N");
        }

       noticeService.saveNotice(notice);
        return "redirect:/notice/list";
    }

    // 상세
    @GetMapping("/notice/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model, HttpSession session) {
        model.addAttribute("notice", noticeService.getNoticeDetail(id));
        model.addAttribute("replies", noticeService.getRepliesByNoticeId(id));


        Integer roleId = (Integer) session.getAttribute("role_id");
        String nav= switch (roleId != null ? roleId : 1){
            case 2 -> "officeNav";
            case 3 -> "fieldNav";
            default -> "adminNav";
        };

        model.addAttribute("navFragment", "fragment/nav/" + nav);
        model.addAttribute("content", "notice/detail");
        return "layout";
    }
    //수정
    @GetMapping("/notice/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, HttpSession session) {
        Integer roleId = (Integer) session.getAttribute("role_id");
        String nav= switch (roleId != null ? roleId : 1){
            case 2 -> "officeNav";
            case 3 -> "fieldNav";
            default -> "adminNav";
        };

        model.addAttribute("navFragment", "fragment/nav/" + nav);
        model.addAttribute("content", "notice/edit");
        model.addAttribute("notice", noticeService.getNoticeDetail(id));
        return "layout";
    }
    //수정처리 (POST)
     @PostMapping("/notice/edit")
    public String edit(NoticeDto notice, @RequestParam("file") MultipartFile file) throws IOException{
        if(file != null && !file.isEmpty()){
            NoticeDto oldNotice= noticeService.getNoticeDetail(notice.getNoticeId());
            if(oldNotice.getSaveFileName() != null){
                File oldFile= new File(oldNotice.getFilePath() + oldNotice.getSaveFileName());
                if(oldFile.exists()) oldFile.delete();
            }
            String uploadDir= "D:/";
            String orgFileName= file.getOriginalFilename();
            String saveFileName= UUID.randomUUID() + "_" + orgFileName;
            file.transferTo(new File(uploadDir + saveFileName));

            notice.setOrgFileName(orgFileName);
            notice.setSaveFileName(saveFileName);
            notice.setFilePath(uploadDir);
        }else {
            NoticeDto oldNotice= noticeService.getNoticeDetail(notice.getNoticeId());
            notice.setOrgFileName(oldNotice.getOrgFileName());
            notice.setSaveFileName(oldNotice.getSaveFileName());
            notice.setFilePath(oldNotice.getFilePath());
        }

        if(notice.getPinYn() == null) {
            notice.setPinYn("N");
        }
        noticeService.updateNotice(notice);
        return "redirect:/notice/detail/" + notice.getNoticeId();
    }
    @GetMapping ("/notice/delete/{id}")
    public String deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return "redirect:/notice/list";
    }

    @PostMapping("/notice/reply/new")
    public String saveReply(NoticeReplyDto replyDto){
        noticeService.saveReply(replyDto);
        return "redirect:/notice/detail/" + replyDto.getNoticeId();
    }

    @GetMapping("/notice/reply/delete/{replyId}/{noticeId}")
    public String deleteReply(@PathVariable Long replyId, @PathVariable Long noticeId){
        noticeService.deleteReply(replyId);
        return "redirect:/notice/detail/" + noticeId;
    }

    @PostMapping("/notice/reply/edit")
    public String updateReply(NoticeReplyDto replyDto){
        noticeService.updateReply(replyDto);
        return "redirect:/notice/detail/" + replyDto.getNoticeId();
    }
    @GetMapping("/notice/pinned-count")
    @ResponseBody
    public int getPinnedCount(){
        return noticeService.getPinnedNotices().size();
    }

    @GetMapping("/notice/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws IOException{
        NoticeDto notice= noticeService.getNoticeDetail(id);
        Path path= Paths.get(notice.getFilePath() + notice.getSaveFileName());
        Resource resource= new InputStreamResource(Files.newInputStream(path));

        String encodedName= UriUtils.encode(notice.getOrgFileName(), StandardCharsets.UTF_8);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).header(HttpHeaders.CONTENT_DISPOSITION, "attachmentl; filename=\"" + encodedName + "\"").body(resource);
    }
}
