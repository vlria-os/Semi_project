package com.example.demo.mapper;

import com.example.demo.dto.ChatListUpdateDto;
import com.example.demo.dto.ChatMessageDto;
import com.example.demo.dto.ChatRoomDto;
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
}
