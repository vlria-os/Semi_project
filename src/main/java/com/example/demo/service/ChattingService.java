package com.example.demo.service;

import com.example.demo.ChatPresenceStore;
import com.example.demo.dto.ChatMessageDTO;
import com.example.demo.dto.ChatRoomDTO;
import com.example.demo.mapper.ChattingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class ChattingService {
    private final ChattingMapper mapper;
    private final ChatPresenceStore store;

    public List<ChatRoomDTO> chatRoomAll(int user_id){
        return mapper.chatRoomAll(user_id);
    }

    public boolean isParticipant(int room_id, int user_id){
        Map<String, Object> map=new HashMap<>();
        map.put("room_id",room_id);
        map.put("user_id",user_id);
        return mapper.isParticipant(map) > 0;
    }

    public ChatRoomDTO getRoom(int room_id){
        return mapper.getRoom(room_id);
    }

    public List<ChatMessageDTO> getMessages(int room_id){
        return mapper.getMessages(room_id);
    }

    public void sendMessage(ChatMessageDTO dto){
        int n=mapper.sendMessage(dto);
        System.out.println("insert => " + n);
    }

    public void readMessages(int roomId, int readerId){
        mapper.readMessages(roomId,readerId);
    }

    public Integer markLastMessageAsRead(int roomId, int senderId, int readerId){
        //상대방이 현재 접속 중인지 확인
        boolean isReaderPresent=store.isUserInRoom(roomId,readerId);

        //상대방이 접속 중이면 읽음 처리 안 함
        if(isReaderPresent){
            System.out.println("상대방이 채팅방에 접속 중이라 읽음 처리 안 함");
            return null;
        }

        //내가 보낸 메시지 중 읽히지 않은 마지막 메시지 조회
        Map<String,Object> map=new HashMap<>();
        map.put("roomId",roomId);
        map.put("senderId",senderId);

        Integer lastUnreadId=mapper.getLastUnreadMessageId(map);

        //메시지가 있으면 읽음 처리
        if(lastUnreadId != null){
            mapper.markMessageRead(lastUnreadId);
            System.out.println("읽음 처리된 메시지 id: " + lastUnreadId);
        }
        return lastUnreadId;
    }
}
