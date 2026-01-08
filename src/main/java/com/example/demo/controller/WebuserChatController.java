package com.example.demo.controller;

import com.example.demo.dto.ChatMessageDto;
import com.example.demo.dto.ChatRoomDto;
import com.example.demo.service.ChattingService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebuserChatController {
    private final ChattingService service;

    @GetMapping("/webuser/chatRoomList")
    public String chatRoomList(HttpSession session, Model model){
        int user_id = (int) session.getAttribute("webuser_id");

        List<ChatRoomDto> list=service.chatRoomListSummary(user_id);
        model.addAttribute("list",list);

        return "chatRoomList";
    }
    @GetMapping("/chat/room/{roomId}")
    public String chatRoom(@PathVariable int roomId,
                           HttpSession session,
                           Model model){
        int userId = (int) session.getAttribute("webuser_id");

        List<ChatMessageDto> messages=service.getMessages(roomId,userId);

        model.addAttribute("messages",messages);
        model.addAttribute("room_id",roomId);
        model.addAttribute("user_id",userId);
        return "chatRoom";
    }

    @PostMapping("/chat/message/send")
    public String sendMessage(@RequestParam int room_id,
                              @RequestParam int sender_id,
                              @RequestParam String content){

        ChatMessageDto dto=new ChatMessageDto(0,room_id,sender_id,content,null,0);
        service.sendMessage(dto);

        return "redirect:/chat/room/" + room_id;
    }
}
