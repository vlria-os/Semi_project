package com.example.demo.controller;

import com.example.demo.dto.ChatMessageDto;
import com.example.demo.dto.ChatRoomDto;
import com.example.demo.service.ChattingService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebuserChatController {
    private final ChattingService service;

    @GetMapping("/chat/list")
    public String chatRoomList(HttpSession session, Model model){
        int myId = (int) session.getAttribute("webuser_id");

        List<ChatRoomDto> list=service.chatRoomListSummary(myId);
        model.addAttribute("list",list);
        model.addAttribute("user_id",myId);

        return "chatRoomList";
    }

    @GetMapping("/chat/new")
    public String newChat(HttpSession session, Model model){

        int myId = (int) session.getAttribute("webuser_id");

        model.addAttribute("users",service.getAllExceptMe(myId));

        return "newChat";
    }

    @PostMapping("/chat/start")
    public String startChat(@RequestParam int targetId, HttpSession session){
        int myId = (int) session.getAttribute("webuser_id");

        int roomId = service.getOrCreateOneToOneRoom(myId,targetId);

        return "redirect:/chat/room/" + roomId;
    }

    @GetMapping("/chat/room/{roomId}")
    public String chatRoom(@PathVariable int roomId,
                           HttpSession session,
                           Model model){
        int myId = (int) session.getAttribute("webuser_id");

        model.addAttribute("room_id",roomId);
        model.addAttribute("user_id",myId);

        model.addAttribute("messages",service.getMessages(roomId,myId));
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
