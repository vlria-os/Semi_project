package com.example.demo.service;

import com.example.demo.ChatPresenceStore;
import com.example.demo.dto.ChatListUpdateDto;
import com.example.demo.dto.ChatMessageDto;
import com.example.demo.dto.ChatRoomDto;
import com.example.demo.dto.WebuserDto;
import com.example.demo.mapper.ChattingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ChattingService {
    private final ChattingMapper mapper;
    private final ChatPresenceStore store;

    public List<ChatRoomDto> chatRoomAll(int user_id){
        return mapper.chatRoomAll(user_id);
    }

    public boolean isParticipant(int room_id, int user_id){
        Map<String, Object> map=new HashMap<>();
        map.put("room_id",room_id);
        map.put("user_id",user_id);
        return mapper.isParticipant(map) > 0;
    }

    public ChatRoomDto getRoom(int room_id){
        return mapper.getRoom(room_id);
    }

    public List<ChatMessageDto> getMessages(int room_id, int user_id){
        Map<String,Object> map=new HashMap<>();
        map.put("room_id",room_id);
        map.put("user_id",user_id);
        mapper.markMessageAsRead(map);
        return mapper.getMessages(room_id);
    }

    public void sendMessage(ChatMessageDto dto){
        boolean opponentOnline=store.getUserCount(dto.getRoom_id()) > 1;
        dto.setRead_count(opponentOnline ? 0 : 1);

        int n=mapper.sendMessage(dto);
        System.out.println("insert => " + n);
    }

    public void markMessageAsRead(int room_id, int user_id){
        Map<String,Object> map=new HashMap<>();
        map.put("room_id",room_id);
        map.put("user_id",user_id);
        mapper.markMessageAsRead(map);
    }

    public List<ChatRoomDto> chatRoomListSummary(int user_id){
        return mapper.chatRoomListSummary(user_id);
    }

    public int getOpponentUserId(int roomId, int userId){
        return mapper.getOpponentUserId(roomId,userId);
    }

    public ChatListUpdateDto getChatListUpdate(int roomId, int userId) {
        return mapper.selectChatRoomSummary(roomId, userId);
    }

    public int getOrCreateOneToOneRoom(int myId, int targetId){
        Integer roomId = mapper.findOneToOneRoom(myId, targetId);

        if(roomId != null){
            return roomId;
        }

        mapper.insertChatRoom("one");
        int newRoomId = mapper.getLastRoomId();

        mapper.insertChatRoomUser(newRoomId, myId);
        mapper.insertChatRoomUser(newRoomId, targetId);

        return newRoomId;
    }

    public List<WebuserDto> getAllExceptMe(int myId){
        return mapper.getAllExceptMe(myId);
    }

}
