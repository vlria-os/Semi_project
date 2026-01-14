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

        int roomId = dto.getRoom_id();
        int senderId = dto.getSender_id();

        if (!service.isParticipant(roomId, senderId)) {
            template.convertAndSend("/topic/error/" + senderId, "NOT_PARTICIPANT");
            return;
        }

        // 1) DB 저장 (selectKey로 message_id 세팅)
        service.sendMessage(dto);
        int mid = dto.getMessage_id();

        // ✅ 방 전체 참여자(정답 기준) 목록
        List<Integer> roomUserIds = service.getRoomUserIds(roomId);

        // 2) “방을 보고 있는(접속 중인)” 유저들은 서버에서 즉시 읽음 처리 + 온라인 수 계산
        int onlineExceptSender = 0;

        for (Integer uid : roomUserIds) {
            if (uid == null) continue;
            if (uid == senderId) continue;

            // ✅ store에 실제로 방 접속 중이면 = 지금 보고 있는 사람
            if (store.isUserInRoom(roomId, uid)) {
                onlineExceptSender++;

                if (mid > 0) {
                    service.updateLastReadUpTo(roomId, uid, mid);

                    // read_count UI 즉시 감소용
                    Map<String,Object> payload = new HashMap<>();
                    payload.put("userId", uid);
                    payload.put("lastReadId", mid);
                    template.convertAndSend("/topic/read/" + roomId,(Object) payload);
                }
            }
        }

        // 3) read_count(=표시용 “안 읽은 사람 수”) 확정
        int memberCount = roomUserIds.size(); // ✅ DB 기준 참여자 수로 고정
        int unread = (memberCount - 1) - onlineExceptSender;
        if (unread < 0) unread = 0;
        dto.setRead_count(unread);

        // 4) senderName 세팅
        dto.setSenderName(service.getUserName(senderId));

        // 5) 채팅방 브로드캐스트
        template.convertAndSend("/topic/chat/" + roomId, dto);

        // 6) 채팅 목록 업데이트는 “방 밖”만
        broadcastChatListUpdateToRoomUsers(roomId);
    }


    private void broadcastChatListUpdateToRoomUsers(int roomId) {
        List<Integer> userIds = service.getRoomUserIds(roomId);

        for (int uid : userIds) {
            ChatListUpdateDto update = service.getChatListUpdate(roomId, uid);
            update.setAction("upsert");

            // ✅ 채팅방을 보고 있는 중이면 목록의 unread는 무조건 0
            if (store.isUserInRoom(roomId, uid)) {
                update.setUnreadCount(0);
            }

            template.convertAndSend("/topic/chat-list/" + uid, update);
        }
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

        service.updateLastReadUpTo(dto.getRoomId(), dto.getUserId(), dto.getLastReadId());

        ChatListUpdateDto update = service.getChatListUpdate(dto.getRoomId(), dto.getUserId());
        update.setAction("upsert");

        // ✅ 방을 보고 있는 상태에서 read 보내는 거니까 목록은 0 확정
        update.setUnreadCount(0);

        template.convertAndSend("/topic/chat-list/" + dto.getUserId(), update);

        Map<String,Object> payload = new HashMap<>();
        payload.put("userId", dto.getUserId());
        payload.put("lastReadId", dto.getLastReadId());
        template.convertAndSend("/topic/read/" + dto.getRoomId(),(Object) payload);
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
