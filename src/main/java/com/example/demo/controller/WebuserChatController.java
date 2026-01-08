package com.example.demo.controller;

import com.example.demo.dto.ChatMessageDTO;
import com.example.demo.dto.ChatRoomDTO;
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

    @GetMapping("/webuser/chatRoomList")
    public String chatRoomList(@RequestParam("user_id") int user_id, Model model){
        List<ChatRoomDTO> list=service.chatRoomAll(user_id);
        model.addAttribute("list",list);
        System.out.println(user_id);
        System.out.println(list);

        return "chatRoomList";
    }

//    @GetMapping("/chat/room/{room_id}")
//    public String chatRoom(@PathVariable int room_id, HttpSession session, Model model){
//        int user_id = (int) session.getAttribute("webuser_id");
//
//        //참여자 확인
//        if(!service.isParticipant(room_id,user_id)){
//            return "redirect:/chatRoomlist";
//        }
//
//        //채팅방 정보
//        ChatRoomDTO room=service.getRoom(room_id);
//
//        //과거 메시지
//        List<ChatMessageDTO> messages=service.getMessages(room_id);
//
//        model.addAttribute("room",room);
//        model.addAttribute("room_id",room.getRoom_id());
//        model.addAttribute("messages",messages);
//        model.addAttribute("user_id",user_id);
//
//        return "chatRoom";
//
//    }

    @GetMapping("/chat/room/{roomId}")
    public String chatRoom(@PathVariable int roomId,
                           HttpSession session,
                           Model model){
        int userId = (int) session.getAttribute("webuser_id");

        List<ChatMessageDTO> messages=service.getMessages(roomId);

        model.addAttribute("messages",messages);
        model.addAttribute("room_id",roomId);
        model.addAttribute("user_id",userId);
        return "chatRoom";
    }

    @PostMapping("/chat/message/send")
    public String sendMessage(@RequestParam int room_id,
                              @RequestParam int sender_id,
                              @RequestParam String content){

        ChatMessageDTO dto=new ChatMessageDTO(0,room_id,sender_id,content,null,0);
        service.sendMessage(dto);

        return "redirect:/chat/room/" + room_id;
    }
}
