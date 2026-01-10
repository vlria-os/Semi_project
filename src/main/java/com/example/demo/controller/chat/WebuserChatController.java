package com.example.demo.controller.chat;

import com.example.demo.ChatPresenceStore;
import com.example.demo.dto.*;
import com.example.demo.service.ChattingService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class WebuserChatController {
    private final ChattingService service;
    private final SimpMessagingTemplate template;
    private final ChatPresenceStore store;

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

    @GetMapping("/chat/group/new")
    public String newGropChat(Model model,HttpSession session){

        int myId=(int)session.getAttribute("webuser_id");

        List<WebuserDto> userList=service.getAllExceptMe(myId);

        model.addAttribute("users",userList);
        return "groupChatCreate";
    }

    @GetMapping("/chat/start/{targetId}")
    public String startChat(@PathVariable int targetId, HttpSession session){
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
        model.addAttribute("displayRoomName",
                service.getDisplayRoomName(roomId,myId));
        model.addAttribute("roomUserCount",
                            service.getRoomUserCount(roomId));

        model.addAttribute("messages",service.getMessages(roomId,myId));

        if(!service.isParticipant(roomId, myId)){
            return "redirect:/chat/list";
        }

        return "chatRoom";
    }

    @PostMapping("/chat/message/send")
    public String sendMessage(@RequestParam int room_id,
                              @RequestParam int sender_id,
                              @RequestParam String content){

        ChatMessageDto dto=new ChatMessageDto(0,room_id,sender_id,null,content,null,0);
        service.sendMessage(dto);

        return "redirect:/chat/room/" + room_id;
    }

    @PostMapping("/chat/group/create")
    public String createGroupChat(@RequestParam String roomName, @RequestParam List<Integer> userIds,
                                  HttpSession session){

        int myId = (int) session.getAttribute("webuser_id");

        //방 만든 사람도 자동 포함
        userIds.add(myId);

        int roomId=service.createGroupRoom(roomName,userIds);

        return "redirect:/chat/room/"+roomId;
    }

    @PostMapping("/chat/room/{roomId}/leave")
    @ResponseBody
    public ChatLeaveResultDto leaveGroupRoom(@PathVariable int roomId, HttpSession session){
        int userId=(int)session.getAttribute("webuser_id");

        ChatLeaveResultDto result=service.leaveGroupRoom(roomId,userId);

        result.getRemainingUserIds().removeIf(uid -> uid == userId);

        store.leaveRoom(roomId,userId);

        template.convertAndSend("/topic/presence/" + roomId,
                store.getUserCount(roomId));

        template.convertAndSend("/topic/chat-room-removed/" + userId, roomId);

        for(Integer uid: result.getRemainingUserIds()){
            ChatListUpdateDto update=service.getChatListUpdate(roomId,uid);
            template.convertAndSend("/topic/chat-list/" + uid, update);
        }

        return result;
    }
}
