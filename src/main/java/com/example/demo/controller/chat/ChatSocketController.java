package com.example.demo.controller.chat;

import com.example.demo.ChatPresenceStore;
import com.example.demo.dto.ChatListUpdateDto;
import com.example.demo.dto.ChatMessageDto;
import com.example.demo.dto.ChatPresenceDto;
import com.example.demo.dto.Inbound_detailDto;
import com.example.demo.service.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        String senderName=service.getUserName(dto.getSender_id());
        dto.setSenderName(senderName);

        //채팅방 실시간
        template.convertAndSend(
                "/topic/chat/" + dto.getRoom_id(), dto
        );

        // (추가 추천) 채팅 리스트 실시간 업데이트 방송도 여기서 해야 함
        List<Integer> userIds = service.getRoomUserIds(dto.getRoom_id());
        for(int uid: userIds){
            ChatListUpdateDto update=service.getChatListUpdate(dto.getRoom_id(), uid);
            template.convertAndSend("/topic/chat-list/" + uid, update);
        }
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

        int lastReasId = service.enterAndMarkReadAll(dto.getRoomId(), dto.getUserId());

        Map<String,Object> payload = new HashMap<>();
        payload.put("userId",dto.getUserId());
        payload.put("lastReadId",lastReasId);

        template.convertAndSend("/topic/read/" + dto.getRoomId(), (Object) payload);

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
