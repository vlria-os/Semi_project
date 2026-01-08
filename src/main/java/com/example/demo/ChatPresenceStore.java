package com.example.demo;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatPresenceStore {

    // roomId -> 현재 접속 중인 userId 목록
    private final Map<Integer, Set<Integer>> roomUsers =new ConcurrentHashMap<>();

    // 유저 입장
    public void enterRoom(int roomId, int userId){
        roomUsers.computeIfAbsent(roomId, K -> ConcurrentHashMap.newKeySet()).add(userId);
    }

    // 유저 퇴장
    public void leaveRoom(int roomId, int userId){
        Set<Integer> users = roomUsers.get(roomId);
        if(users != null){
            users.remove(userId);
            if (users.isEmpty()){
                roomUsers.remove(roomId);
            }
        }
    }

    // 특정 방에 유저가 있는지
    public boolean isUserInRoom(int roomId, int userId){
        return roomUsers.containsKey(roomId) && roomUsers.get(roomId).contains(userId);
    }

    // 방에 몇 명 접속 중인지
    public int getUserCount(int roomId){
        return roomUsers.getOrDefault(roomId, Set.of()).size();
    }

    // 현재 방에 접속 중인 모든 유저 반환
    public Set<Integer> getUsersInRoom(int roomId){
        return roomUsers.getOrDefault(roomId, Set.of());
    }

    public List<Integer> getUsersInRoomExcept(int roomId, int excludeUserId){
        Set<Integer> users = roomUsers.get(roomId);
        if(users == null) return Collections.emptyList();

        List<Integer> result=new ArrayList<>();
        for(Integer userId : users){
            if(!Objects.equals(userId, excludeUserId)){
                result.add(userId);
            }
        }

        return result;
    }


}
