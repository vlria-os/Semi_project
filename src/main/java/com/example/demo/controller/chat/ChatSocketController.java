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

        //db 저장
        service.sendMessage(dto);

        String senderName=service.getUserName(dto.getSender_id());
        dto.setSenderName(senderName);

        // ✅ 내 메시지면 "안 읽은 사람 수" 계산해서 읽음 숫자 띄우기
        if(dto.getSender_id() != 0){
            int unread = service.calcUnreadCountForMyMessage(dto.getRoom_id(), dto.getSender_id(), dto.getMessage_id());
            dto.setRead_count(unread);
        }else{
            dto.setRead_count(0);
        }

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

        ChatListUpdateDto update=service.getChatListUpdate(dto.getRoomId(),dto.getUserId());
        template.convertAndSend("/topic/chat-list/" + dto.getUserId(), update);
    }

    @MessageMapping("/chat/read")
    public void read(ChatReadDto dto){

        // 1) DB last_read 갱신
        service.updateLastReadUpTo(dto.getRoomId(), dto.getUserId(), dto.getLastReadId());

        // 2) ✅ "내 채팅 목록" unread 즉시 갱신 (0으로 떨어짐)
        ChatListUpdateDto update = service.getChatListUpdate(dto.getRoomId(), dto.getUserId());
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

}
