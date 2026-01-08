package com.example.demo.controller;

import com.example.demo.ChatPresenceStore;
import com.example.demo.dto.ChatListUpdateDTO;
import com.example.demo.dto.ChatMessageDTO;
import com.example.demo.dto.ChatPresenceDTO;
import com.example.demo.service.ChattingService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {
    private final ChattingService service;
    private final SimpMessagingTemplate template;
    private final ChatPresenceStore store;

    @MessageMapping("/chat/message")
    public void sendMessage(ChatMessageDTO dto){

        //db 저장
        service.sendMessage(dto);

        //같은 방에 메시지 뿌리기
        template.convertAndSend(
                "/topic/chat/" + dto.getRoom_id(), dto
        );

        //상대방 userId 찾기
        int targetUserId =service.getOpponentUserId(dto.getRoom_id(),dto.getSender_id());

        //상대가 지금 방에 없으면 unread 증가
        if(store.isUserInRoom(dto.getRoom_id(),targetUserId)){
            ChatListUpdateDTO update=
                    service.getChatListUpdate(dto.getRoom_id(),targetUserId);

            template.convertAndSend(
                    "/topic/chat-list/" + targetUserId,
                    update
            );
        }
    }

    //채팅방 입장
    @MessageMapping("/chat/enter")
    public void enter(ChatPresenceDTO dto,
                      SimpMessageHeaderAccessor headerAccessor) {
        //세션에 저장
        headerAccessor.getSessionAttributes().put("roomId", dto.getRoomId());
        headerAccessor.getSessionAttributes().put("userId", dto.getUserId());

        // ChatPresenceStore에 사용자 입장 기록
        store.enterRoom(dto.getRoomId(), dto.getUserId());

        //읽음 처리
        service.markMessageAsRead(dto.getRoomId(), dto.getUserId());

        //상대방에게 읽음 처리됨 알림
        template.convertAndSend(
                "/topic/read/" + dto.getRoomId(),
                dto.getUserId()
        );

        //접속 인원 알림
        template.convertAndSend(
                "/topic/presence/" + dto.getRoomId(),
                store.getUserCount(dto.getRoomId())
        );
    }

    //채팅방 퇴장
    @MessageMapping("/chat/leave")
    public void leave(ChatPresenceDTO dto){
        store.leaveRoom(dto.getRoomId(), dto.getUserId());

        //접속 인원 알림
        template.convertAndSend(
                "/topic/presence/" + dto.getRoomId(),
                store.getUserCount(dto.getRoomId())
        );
    }

}
