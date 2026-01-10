package com.example.demo.service;

import com.example.demo.ChatPresenceStore;
import com.example.demo.dto.*;
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
        enterAndMarkReadAll(room_id,user_id);
        return mapper.getMessages(room_id);
    }

    public void sendMessage(ChatMessageDto dto){
        //insert
        mapper.sendMessage(dto);

        int roomId = dto.getRoom_id();
        int messageId = dto.getMessage_id();
        int senderId = dto.getSender_id();

        //보낸 사람은 이 메시지까지 읽은 상태로 기록
        Map<String,Object> senderMap=new HashMap<>();
        senderMap.put("roomId",roomId);
        senderMap.put("userId",senderId);
        senderMap.put("lastReadMessageId",messageId);
        mapper.updateLastReadMessageId(senderMap);

        //지금 방에 접속 중인 사람들도 읽은 상태로 기록
        List<Integer> onlineUserIds=store.getUsersInRoomExcept(roomId,senderId);
        if(!onlineUserIds.isEmpty()){
            Map<String,Object> onlineMap=new HashMap<>();
            onlineMap.put("roomId",roomId);
            onlineMap.put("messageId",messageId);
            onlineMap.put("userIds",onlineUserIds);
            mapper.updateLastReadForOnlineUsers(onlineMap);
        }

        //이 메시지를 읽은 사람 수(보낸 사람 + 접속 중인 유저 제외)
        Map<String,Object> p=new HashMap<>();
        p.put("roomId",roomId);
        p.put("senderId",senderId);
        p.put("messageId",messageId);
        int readers=mapper.countReadersForMessage(p);

        //전체 인원 - 1(보낸 사람) - readers = 안 읽은 사람 수
        int total=mapper.getRoomUserCount(dto.getRoom_id());
        int unreadPeople= (total - 1) - readers;
        dto.setRead_count(Math.max(unreadPeople,0));
    }

    public int enterAndMarkReadAll(int roomId, int userId){
        int maxId = mapper.selectMaxMessageId(roomId);
        Map<String,Object> map = new HashMap<>();
        map.put("roomId",roomId);
        map.put("userId",userId);
        map.put("lastReadMessageId",maxId);
        mapper.updateLastReadMessageId(map);
        return maxId;
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

        mapper.insertChatRoom("one","1:1 채팅");
        int newRoomId = mapper.getLastRoomId();

        mapper.insertChatRoomUser(newRoomId, myId);
        mapper.insertChatRoomUser(newRoomId, targetId);

        return newRoomId;
    }

    public List<WebuserDto> getAllExceptMe(int myId){
        return mapper.getAllExceptMe(myId);
    }

    @Transactional
    public int createGroupRoom(String roomName, List<Integer> userIds){
        //방 생성
        mapper.insertChatRoom("group",roomName);

        int roomId=mapper.getLastRoomId();

        for(Integer userId:userIds){
            mapper.insertChatRoomUser(roomId,userId);
        }

        return roomId;
    }

    public String getUserName(int userId){
        return mapper.selectUserName(userId);
    }

    public int getUnReadCount(int roomId){
        int total=mapper.getRoomUserCount(roomId);
        return total - 1;
    }

    public List<Integer> getRoomUserIds(int roomId){
        return mapper.selectRoomUserIds(roomId);
    }

    public String getDisplayRoomName(int roomId, int userId){
        return mapper.selectDisplayRoomName(roomId, userId);
    }

    public int getRoomUserCount(int roomId){
        return mapper.getRoomUserCount(roomId);
    }

    @Transactional
    public ChatLeaveResultDto leaveGroupRoom(int roomId, int userId){
        //1. group 방인지 체크
        String roomType = mapper.selectRoomType(roomId);
        if(!"group".equals(roomType)){
            throw new IllegalStateException("단체 채팅방만 퇴장할 수 있어요!");
        }

        //2. 퇴장 전 멤버 목록 확보
        List<Integer> beforeMembers=mapper.selectRoomUserIds(roomId);
        if(!beforeMembers.contains(userId)){
            return new ChatLeaveResultDto(roomId, userId, List.of());
        }

        //3. 삭제
        Map<String,Object> del=new HashMap<>();
        del.put("roomId",roomId);
        del.put("userId",userId);
        mapper.deleteChatRoomUser(del);

        List<Integer> remaining = mapper.selectRoomUserIds(roomId);

        return new ChatLeaveResultDto(roomId, userId, remaining);
    }


}
