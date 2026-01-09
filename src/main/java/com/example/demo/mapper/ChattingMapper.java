package com.example.demo.mapper;

import com.example.demo.dto.ChatListUpdateDto;
import com.example.demo.dto.ChatMessageDto;
import com.example.demo.dto.ChatRoomDto;
import com.example.demo.dto.WebuserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ChattingMapper {
    List<ChatRoomDto> chatRoomAll(int user_id);
    int isParticipant(Map<String, Object> map);
    ChatRoomDto getRoom(int room_id);
    List<ChatMessageDto> getMessages(int room_id);
    int sendMessage(ChatMessageDto dto);
    int markMessageAsRead(Map<String,Object> map);
    ChatMessageDto selectReadCount(int room_id);
    List<ChatRoomDto> chatRoomListSummary(int user_id);

    ChatListUpdateDto selectChatRoomSummary(
            @Param("roomId") int roomId,
            @Param("userId") int userId
    );


    int getOpponentUserId(@Param("roomId") int roomId,
                          @Param("userId") int userId);

    //1:1 채팅방 존재 여부 확인
    Integer findOneToOneRoom(@Param("myId") int myId, @Param("targetId") int targetId);

    //채팅방 생성
    void insertChatRoom(@Param("roomType") String roomType);

    //마지막으로 생성된 room_id
    int getLastRoomId();

    //채팅방 - 유저 연결
    void insertChatRoomUser(@Param("roomId") int roomId, @Param("userId") int userId);

    //나를 제외한 유저 목록
    List<WebuserDto> getAllExceptMe(@Param("myId") int myId);
}
