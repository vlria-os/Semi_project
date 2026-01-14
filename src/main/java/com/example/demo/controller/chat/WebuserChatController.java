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
        if(list != null) list.removeIf(r -> r == null);
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

        if (!service.isParticipant(roomId, myId)) {
            return "redirect:/chat/list"; // 또는 403 페이지
        }


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

        if(result.getRemainingUserIds() == null){
            result.setRemainingUserIds(List.of());
        }

        //presence store에서도 제거
        store.leaveRoom(roomId,userId);

        //접속 인원 갱신
        template.convertAndSend("/topic/presence/" + roomId,
                store.getUserCount(roomId));

        //내 채팅 목록에서 방 제거
        template.convertAndSend("/topic/chat-room-removed/" + userId, roomId);

        //남은 유저들 채팅 목록 업데이트
        for(Integer uid: result.getRemainingUserIds()){
            ChatListUpdateDto update=service.getChatListUpdate(roomId,uid);

            // ✅ 강제 보정 (여기!)
            update.setUserCount(service.getRoomUserCount(roomId));
            update.setRoomType("group");

            template.convertAndSend("/topic/chat-list/" + uid, update);
        }

        //퇴장 시스템 메시지 저장 + 채팅방에 broadcast
        //남은 사람이 없으면 굳이 안 보내도 됨
        try{
            if(!result.getRemainingUserIds().isEmpty()){
                ChatMessageDto leaveMsg=service.sendLeaveSystemMessage(roomId,userId);
                template.convertAndSend("/topic/chat/" + roomId, leaveMsg);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        int memberCount = service.getRoomUserCount(roomId);
        template.convertAndSend("/topic/member-count/" + roomId, memberCount);

        return result;
    }

    @GetMapping("/chat/room/{roomId}/invite/candidates")
    @ResponseBody
    public List<WebuserDto> inviteCandidates(@PathVariable int roomId,
                                             HttpSession session){
        int myId = (int) session.getAttribute("webuser_id");
        return service.getInviteCandidates(roomId,myId);
    }

    @PostMapping("/chat/room/{roomId}/invite")
    @ResponseBody
    public ChatInviteResultDto inviteUsers(@PathVariable int roomId,
                                           @RequestBody ChatInviteRequestDto req,
                                           HttpSession session){
        int myId = (int) session.getAttribute("webuser_id");

        ChatInviteResultDto result=service.inviteUsersToRoom(roomId,myId,req.getUserIds());
        if(!result.isOk())return result;

        //초대 성공했으면: 시스템 메시지 발송 + 목록 업데이트 + 인원 수 업데이트
        ChatMessageDto sys=service.sendInviteSystemMessage(roomId,myId,result.getInvitedUserIds());
        template.convertAndSend("/topic/chat/" + roomId, sys);

        //기존 멤버 + 초대된 멤버 모두 목록 업데이트
        List<Integer> allUserIds=service.getRoomUserIds(roomId);

        for(int uid:allUserIds){
            ChatListUpdateDto update=service.getChatListUpdate(roomId,uid);
            update.setAction("upsert");

            update.setUserCount(service.getRoomUserCount(roomId));
            update.setRoomType("group");

            template.convertAndSend("/topic/chat-list/" + uid, update);
        }

        int memberCount = service.getRoomUserCount(roomId);
        template.convertAndSend("/topic/member-count/" + roomId, memberCount);

        return result;
    }

    @GetMapping("/chat/room/{roomId}/members")
    @ResponseBody
    public List<WebuserDto> roomMembers(@PathVariable int roomId, HttpSession session){
        int myId = (int) session.getAttribute("webuser_id");

        if(!service.isParticipant(roomId,myId)){
            return List.of();
        }
        return service.getRoomMembers(roomId);
    }
}
