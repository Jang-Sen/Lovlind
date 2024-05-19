package com.project.lovlind.domain.chat.service;

import com.project.lovlind.domain.chat.controller.dto.request.PostChatDto;
import com.project.lovlind.domain.chat.entity.Chatroom;
import com.project.lovlind.domain.chat.entity.ChatroomHobby;
import com.project.lovlind.domain.chat.repository.ChatroomRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatroomService {
  private final ChatroomRepository repository;

  public Long saveChatroom(PostChatDto dto) {
    List<ChatroomHobby> requestHobbyList = new ArrayList<>();

    Chatroom requestEntity =
        new Chatroom(dto.title(), dto.maxParticipant(), dto.minParticipant(), requestHobbyList);
    Chatroom savedEntity = repository.save(requestEntity);
    return savedEntity.getId();
  }
  // TODO: chatroom Hobby 적용
}
