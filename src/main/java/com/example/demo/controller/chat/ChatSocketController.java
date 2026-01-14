package com.example.demo.controller.chat;

import com.example.demo.ChatPresenceStore;
import com.example.demo.dto.*;
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

        if (dto == null || dto.getRoom_id() <= 0 || dto.getSender_id() <= 0) return;

        if (!service.isParticipant(dto.getRoom_id(), dto.getSender_id())) {
            template.convertAndSend("/topic/error/" + dto.getSender_id(), "NOT_PARTICIPANT");
            return;
        }

        //db 저장
        service.sendMessage(dto);

        String senderName=service.getUserName(dto.getSender_id());
        dto.setSenderName(senderName);

        //채팅방 실시간
        template.convertAndSend(
                "/topic/chat/" + dto.getRoom_id(), dto
        );

        broadcastChatListUpdateToRoomUsers(dto.getRoom_id());
    }

    //채팅방 입장
    @MessageMapping("/chat/enter")
    public void enter(ChatPresenceDto dto,
                      SimpMessageHeaderAccessor headerAccessor) {

        if (dto == null || dto.getRoomId() <= 0 || dto.getUserId() <= 0) return;

        // ✅ 참여자 아니면 입장 차단
        if (!service.isParticipant(dto.getRoomId(), dto.getUserId())) {
            template.convertAndSend("/topic/error/" + dto.getUserId(), "NOT_PARTICIPANT");
            return;
        }

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

        ChatListUpdateDto update=service.getChatListUpdate(dto.getRoomId(),dto.getUserId());
        template.convertAndSend("/topic/chat-list/" + dto.getUserId(), update);
    }

    @MessageMapping("/chat/read")
    public void read(ChatReadDto dto){

        // 1) DB last_read 갱신
        service.updateLastReadUpTo(dto.getRoomId(), dto.getUserId(), dto.getLastReadId());

        // 2) ✅ "내 채팅 목록" unread 즉시 갱신 (0으로 떨어짐)
        ChatListUpdateDto update = service.getChatListUpdate(dto.getRoomId(), dto.getUserId());
        update.setAction("upsert");
        template.convertAndSend("/topic/chat-list/" + dto.getUserId(), update);

        // 3) ✅ 방에 "누가 어디까지 읽었는지" 브로드캐스트 (상대 read_count 감소용)
        Map<String,Object> payload = new HashMap<>();
        payload.put("userId", dto.getUserId());
        payload.put("lastReadId", dto.getLastReadId());
        template.convertAndSend("/topic/read/" + dto.getRoomId(), (Object) payload);
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

    private void broadcastChatListUpdateToRoomUsers(int roomId) {
        List<Integer> userIds = service.getRoomUserIds(roomId);
        for (int uid : userIds) {
            ChatListUpdateDto update = service.getChatListUpdate(roomId, uid);
            update.setAction("upsert");
            template.convertAndSend("/topic/chat-list/" + uid, update);
        }
    }


}
