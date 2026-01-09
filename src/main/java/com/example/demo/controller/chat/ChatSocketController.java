package com.example.demo.controller.chat;

import com.example.demo.ChatPresenceStore;
import com.example.demo.dto.ChatListUpdateDto;
import com.example.demo.dto.ChatMessageDto;
import com.example.demo.dto.ChatPresenceDto;
import com.example.demo.service.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {
    private final ChattingService service;
    private final SimpMessagingTemplate template;
    private final ChatPresenceStore store;

    @MessageMapping("/chat/message")
    public void sendMessage(ChatMessageDto dto){

        //db 저장
        service.sendMessage(dto);

        //채팅방 실시간
        template.convertAndSend(
                "/topic/chat/" + dto.getRoom_id(), dto
        );

        //상대방 userId 찾기
        int targetUserId =service.getOpponentUserId(dto.getRoom_id(),dto.getSender_id());


        ChatListUpdateDto update=
                service.getChatListUpdate(dto.getRoom_id(),targetUserId);

        template.convertAndSend(
                "/topic/chat-list/" + targetUserId,
                update
        );

    }

    //채팅방 입장
    @MessageMapping("/chat/enter")
    public void enter(ChatPresenceDto dto,
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
    public void leave(ChatPresenceDto dto){
        store.leaveRoom(dto.getRoomId(), dto.getUserId());

        //접속 인원 알림
        template.convertAndSend(
                "/topic/presence/" + dto.getRoomId(),
                store.getUserCount(dto.getRoomId())
        );
    }

}
