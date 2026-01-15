package com.example.demo.service;

import com.example.demo.ChatPresenceStore;
import com.example.demo.dto.*;
import com.example.demo.mapper.ChattingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ChattingService {
    private final ChattingMapper mapper;
    private final ChatPresenceStore store;
    private static final int SYSTEM_USER_ID = 0;

    public boolean isParticipant(int roomId, int userId) {
        return mapper.isParticipant(roomId, userId) > 0;
    }

    public ChatRoomDto getRoom(int room_id){
        return mapper.getRoom(room_id);
    }

    public List<ChatMessageDto> getMessages(int room_id, int user_id){
        enterAndMarkReadAll(room_id,user_id);
        return mapper.getMessages(room_id,user_id,SYSTEM_USER_ID);
    }

    public void sendMessage(ChatMessageDto dto){
        mapper.sendMessage(dto);

        int roomId = dto.getRoom_id();
        int messageId = dto.getMessage_id();
        int senderId = dto.getSender_id();

        if(senderId == SYSTEM_USER_ID){
            dto.setRead_count(0);
            return;
        }

        // 보낸 사람은 이 메시지까지 읽음 처리
        mapper.updateLastReadMessageId(roomId, senderId, messageId);

        // ✅ 여기서 read_count 계산하지 말기
        dto.setRead_count(0); // (선택) 그냥 0으로 초기화
    }


    public int enterAndMarkReadAll(int roomId, int userId){
        Integer maxId = mapper.selectMaxMessageId(roomId);
        // 없으면 null
        if (maxId == null) {
            return 0; // 메시지 없으면 읽음 처리할 것도 없음
        }

        mapper.updateLastReadMessageId(maxId, roomId, userId);

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

    public ChatMessageDto sendLeaveSystemMessage(int roomId, int leaverId){
        String name=mapper.selectUserName(leaverId);

        ChatMessageDto sys=new ChatMessageDto();
        sys.setRoom_id(roomId);
        sys.setSender_id(SYSTEM_USER_ID);
        sys.setSenderName("SYSTEM");
        sys.setContent(name + "님이 퇴장하였습니다.");

        //db 저장
        sendMessage(sys);

        return sys; //컨트롤러에서 웹소켓으로 뿌릴 용도
    }

    @Transactional
    public void updateLastReadUpTo(int roomId, int userId, int lastReadId){
        mapper.updateLastReadMessageId(roomId,userId,lastReadId);
    }

    public int calcUnreadCountForMyMessage(int roomId, int senderId, int messageId){
        int total = mapper.getRoomUserCount(roomId);   // 방 전체 인원
        Map<String,Object> map = new HashMap<>();
        map.put("roomId", roomId);
        map.put("senderId", senderId);
        map.put("messageId", messageId);

        int readers = mapper.countReadersForMessage(map); // 나 제외하고 "읽은 사람 수"
        int unread = (total - 1) - readers;
        return Math.max(unread, 0);
    }

    //초대 후보
    public List<WebuserDto> getInviteCandidates(int roomId, int myId){
        return mapper.selectInviteCandidates(roomId, myId);
    }

    //초대 처리
    @Transactional
    public ChatInviteResultDto inviteUsersToRoom(int roomId, int inviterId,
                                                 List<Integer> targetUserIds) {
        //방 참가자 체크(초대하는 사람도 방 멤버여야 함) => 어차피 초대 버튼은 방 안에 있는데..?
        if (!isParticipant(roomId,inviterId)) {
            return new ChatInviteResultDto(false, List.of(), "방 참가자가 아니라 초대할 수 없어요.");
        }

        //단체 채팅방만 => 초대 버튼 방마다 다 생겨...?
        String roomType = mapper.selectRoomType(roomId);
        if (!"group".equals(roomType)) {
            return new ChatInviteResultDto(false, List.of(), "단체 채팅방에서만 초대할 수 있어요.");
        }

        if (targetUserIds == null || targetUserIds.isEmpty()) {
            return new ChatInviteResultDto(false, List.of(), "초대할 사람을 선택하세요.");
        }

        //중복 제거
        targetUserIds = targetUserIds.stream().distinct().toList();

        int lastMessageId = mapper.selectMaxMessageId(roomId);

        //이미 멤버면 제외하고 insert
        List<Integer> invited = new ArrayList<>();

        for (Integer uid : targetUserIds) {
            int exists = mapper.isAlreadyMember(roomId, uid);
            if (exists > 0) continue;

            Map<String,Object> map=new HashMap<>();
            map.put("roomId",roomId);
            map.put("userId",uid);
            map.put("lastReadMessageId",lastMessageId);

            mapper.insertChatRoomUserWithLastRead(map);
            invited.add(uid);
        }

        if (invited.isEmpty()) {
            return new ChatInviteResultDto(false, List.of(), "이미 모두 방에 있거나 초대할 수 없어요.");
        }

        return new ChatInviteResultDto(true, invited, "초대 완료!");
    }

    //초대 시스템 메시지 생성 + 저장
    @Transactional
    public ChatMessageDto sendInviteSystemMessage(int roomId, int inviterId,
                                                  List<Integer> invitedUserIds){
        String inviterName = getUserName(inviterId);

        //초대된 사람들 이름
        List<String> names=invitedUserIds.stream().map(this :: getUserName).toList();

        String content=inviterName + "님이 " + String.join(", ",names) + "님을 초대했습니다.";

        ChatMessageDto dto=new ChatMessageDto(0,roomId,9999,"SYSTEM",content,null,0);

        mapper.sendMessage(dto);
        return dto;

    }

    public List<WebuserDto> getRoomMembers(int roomId){
        return mapper.selectRoomMembers(roomId);
    }
}


