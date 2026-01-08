package com.example.demo.controller;

import com.example.demo.ChatPresenceStore;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {
    private final ChattingService service;
    private final SimpMessagingTemplate template;
    private final ChatPresenceStore store;

    @MessageMapping("/chat/message")
    public void sendMessage(ChatMessageDTO dto){
        System.out.println("ws 수신: " + dto);

        //db 저장
        service.sendMessage(dto);

        //같은 방에 메시지 뿌리기
        template.convertAndSend(
                "/topic/chat/" + dto.getRoom_id(), dto
        );

        //상대방 id 찾기
        Optional<Integer> otherUserId=store.getUsersInRoom(dto.getRoom_id()).stream()
                .filter(id -> !id.equals(dto.getSender_id()))
                .findFirst();

        if(otherUserId.isPresent()){
            Integer readId=service.markLastMessageAsRead(dto.getRoom_id(),dto.getSender_id(),otherUserId.get());
            if(readId != null){
                //읽음 처리가 되었다는 사실을 나(sender)에게만 알려줘야 함
                template.convertAndSendToUser(
                        String.valueOf(dto.getSender_id()),"/queue/read-status" + dto.getRoom_id()
                                        ,readId
                );
            }
        }


    }

    //채팅방 입장
    @MessageMapping("/chat/enter")
    public void enter(ChatPresenceDTO dto,
                      SimpMessageHeaderAccessor headerAccessor){
        //세션에 저장
        headerAccessor.getSessionAttributes().put("roomId",dto.getRoomId());
        headerAccessor.getSessionAttributes().put("userId",dto.getUserId());

        store.enterRoom(dto.getRoomId(), dto.getUserId());

        Set<Integer> usersInRoom=store.getUsersInRoom(dto.getRoomId());
        Optional<Integer> otherUserId=usersInRoom.stream().filter(id -> !id.equals(dto.getUserId()))
                                        .findFirst();

        if(otherUserId.isPresent()){
            //내가 입장했을 때 상대방(otherUserId.get())이 나(dto.getUserId())에게 보낸 메시지들 읽음 처리
            Integer lastReadMessageIdByMe=service.markLastMessageAsRead(dto.getRoomId(),otherUserId.get(),dto.getUserId());
            if(lastReadMessageIdByMe != null){
                //상대방에게 읽음 표시를 갱신하라고 알림
                template.convertAndSendToUser(
                        String.valueOf(otherUserId.get()),"/queue/read-status" + dto.getRoomId(),
                        lastReadMessageIdByMe
                );
            }
        }

        //접속 인원 알림
        //상대방 접속 여부에 따라 '읽음' 표시를 갱신해야 할 수도 있으므로 이 부분에서 '읽음' 상태 재확인
        template.convertAndSend(
                "/topic/presence/" + dto.getRoomId(),
                store.getUserCount(dto.getRoomId())
        );

        //내가 접속했으므로 내가 보낸 메시지 중 상대방이 '읽음' 처리된 것이 있는지 다시 확인하고 갱신
        //이 부분이 상대방이 접속했을 때 내가 보낸 메시지가 '읽음' 처리되는 로직임
        if(otherUserId.isPresent()){ //상대방이 있다면
            Integer lastReadIdForMyMessages=service.markLastMessageAsRead(dto.getRoomId(),dto.getUserId(),otherUserId.get());
            if(lastReadIdForMyMessages != null){
                //나(dto.getUserId)에게 읽음 표시 갱신 요청
                template.convertAndSendToUser(
                        String.valueOf(dto.getUserId()),"/queue/read-status/" + dto.getRoomId(),
                        lastReadIdForMyMessages
                );
            }
        }
    }

    //채팅방 퇴장
    @MessageMapping("/chat/leave")
    public void leave(ChatPresenceDTO dto){
        store.leaveRoom(dto.getRoomId(), dto.getUserId());

        template.convertAndSend(
                "/topic/presence/" + dto.getRoomId(),
                store.getUserCount(dto.getRoomId())
        );

        //퇴장 시 내가 보낸 메시지에 대한 '읽음' 상태가 변경될 수 있음(상대방이 나갔으므로 '읽음' 처리될 가능성)
        //상대방 id 찾아야 함
        Optional<Integer> otherUserId=store.getUsersInRoom(dto.getRoomId()).stream()
                                        .filter(id -> !id.equals(dto.getUserId()))
                                        .findFirst();
        if(otherUserId.isPresent()){ //상대방이 여전히 남아있다면
            //상대방이 보낸 메시지 중 내가 퇴장했으므로 '읽음' 처리될 수 있는 메시지를 찾음
            Integer lastReadIdForOthersMessages = service.markLastMessageAsRead(dto.getRoomId(),
                    otherUserId.get(),dto.getUserId());
            if(lastReadIdForOthersMessages != null){
                //상대방에게 읽음 표시 갱신 요청
                template.convertAndSendToUser(
                        String.valueOf(otherUserId.get()),"/queue/read-status"+dto.getRoomId(),
                        lastReadIdForOthersMessages
                );
            }
        }
    }

}
