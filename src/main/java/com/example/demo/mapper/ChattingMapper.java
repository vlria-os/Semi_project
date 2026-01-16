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

    int isParticipant(@Param("roomId") int roomId,
                      @Param("userId") int userId);

    ChatRoomDto getRoom(int room_id);
    List<ChatMessageDto> getMessages(
            @Param("roomId") int roomId,
            @Param("userId") int userId,
            @Param("systemId") int systemId
    );

    int sendMessage(ChatMessageDto dto);

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
    void insertChatRoom(@Param("roomType") String roomType, @Param("roomName") String roomName);

    //마지막으로 생성된 room_id
    int getLastRoomId();

    //채팅방 - 유저 연결
    void insertChatRoomUser(@Param("roomId") int roomId, @Param("userId") int userId);

    //나를 제외한 유저 목록
    List<WebuserDto> getAllExceptMe(@Param("myId") int myId);

    //이름 가져오기
    String selectUserName(@Param("userId") int userId);

    //인원 수 조회
    int getRoomUserCount(@Param("roomId") int roomId);

    Integer selectMaxMessageId(@Param("roomId") int roomId);

    int updateLastReadMessageId(@Param("messageId") Integer messageId,
                                @Param("roomId") int roomId,
                                @Param("userId") int userId);

    int countReadersForMessage(Map<String,Object> map);

    List<Integer> selectRoomUserIds(@Param("roomId") int roomId);

    String selectDisplayRoomName(@Param("roomId") int roomId,
                                 @Param("userId") int userId);

    String selectRoomType(@Param("roomId") int roomId);

    int deleteChatRoomUser(Map<String, Object> map);

    List<WebuserDto> selectInviteCandidates(@Param("roomId") int roomId, @Param("myId") int myId);

    int isAlreadyMember(@Param("roomId") int roomId, @Param("userId") int userId);

    int insertChatRoomUserWithLastRead(Map<String,Object> map);

    List<WebuserDto> selectRoomMembers(@Param("roomId") int roomId);

    int selectTotalUnread(int userId);
}
