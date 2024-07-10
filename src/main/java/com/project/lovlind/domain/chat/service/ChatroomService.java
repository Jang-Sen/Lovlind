package com.project.lovlind.domain.chat.service;

import static com.project.lovlind.domain.chat.exception.ChatExceptionCode.FULL_PARTICIPANTS;
import static com.project.lovlind.domain.chat.exception.ChatExceptionCode.IS_ALREADY_MEMBER;
import static com.project.lovlind.domain.chat.exception.ChatExceptionCode.NOT_FOUND_PARTICIPANT;

import com.project.lovlind.conmon.exception.BusinessLogicException;
import com.project.lovlind.conmon.requset.dto.CurrentUser;
import com.project.lovlind.domain.chat.cache.CacheParticipantRepository;
import com.project.lovlind.domain.chat.controller.dto.ChatrommSearchFilter;
import com.project.lovlind.domain.chat.controller.dto.request.PostChatDto;
import com.project.lovlind.domain.chat.controller.dto.response.ChatRoomDto;
import com.project.lovlind.domain.chat.entity.Chatroom;
import com.project.lovlind.domain.chat.entity.ChatroomHobby;
import com.project.lovlind.domain.chat.repository.ChatroomRepository;
import com.project.lovlind.domain.participaint.entity.Participant;
import com.project.lovlind.domain.participaint.repository.ParticipantRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatroomRepository repository;
    private final CacheParticipantRepository cacheParticipantRepository;
    private final ParticipantRepository participantRepository;

    //    private final MemberRepository memberRepository;
  /*

      public void joinChatroom(Long roomId, Long userId) {
          Chatroom chatroom = repository.findById(roomId)
                  .orElseThrow(() -> new BusinessLogicException(NOT_FOUND_CHATROOM));

          Member member = memberRepository.findById(memberId)
                  .orElseThrow(() -> new BusinessLogicException(NOT_FOUND_PARTICIPANT));

          Participant participant = new Participant(member, chatroom);
          participantRepository.save(participant);
      }
  */

    /**
     * chatRoomId 존재 여부
     */
    public boolean existsById(Long chatRoomId) {
        return repository.existsById(chatRoomId);
    }

    /**
     * chatRoom 인원 초과 여부
     */
    public boolean isFullChatRoom(Long chatRoomId) {
        Chatroom chatroom = repository.findById(chatRoomId)
                .orElseThrow(() -> new BusinessLogicException(FULL_PARTICIPANTS));

        return chatroom.getParticipantsList().size() >= chatroom.getMaxParticipant();
    }

    /**
     * chatRoom 에 이미 참여한 user 인지 확인 여부
     */
    public boolean isMemberAlreadyJoined(Long chatRoomId, CurrentUser userId) {
        Chatroom chatroom = repository.findById(chatRoomId)
                .orElseThrow(() -> new BusinessLogicException(IS_ALREADY_MEMBER));

        return chatroom.getParticipantsList().stream().anyMatch(user -> user.getId().equals(userId));
    }

    public void leaveChatroom(Long roomId, Long userId) {
        Participant participant =
                participantRepository
                        .findByMemberIdAndRoomId(userId, roomId)
                        .orElseThrow(() -> new BusinessLogicException(NOT_FOUND_PARTICIPANT));

        participantRepository.delete(participant);
    }

    public List<ChatRoomDto> getChatrooms(ChatrommSearchFilter filter) {

        //    Page<Chatroom> chatrooms = filter.isAllSearch() ?
        // repository.findAll(PageRequest.of(filter.getPage(), 100))
        Page<Chatroom> chatrooms =
                repository.findAll(PageRequest.of(filter.getPage() - 1, filter.getPageSize()));

        return chatrooms.stream().map(ChatRoomDto::new).collect(Collectors.toList());
    }

    public Chatroom saveChatroom(PostChatDto dto) {
        List<ChatroomHobby> requestHobbyList = new ArrayList<>();

        Chatroom requestEntity =
                new Chatroom(dto.title(), dto.maxParticipant(), dto.minParticipant(),
                        requestHobbyList);
        Chatroom savedEntity = repository.save(requestEntity);

        // Cache 추가
        cacheParticipantRepository.saveRoom(savedEntity.getId());
        return savedEntity;
    }

    public List<ChatRoomDto> getMyChatrooms(Long userId, ChatrommSearchFilter filter) {

        // 참여한 방 전채 목록 조회
        List<Participant> joinedChatIds = participantRepository.findByMemberId(userId);
        if (joinedChatIds.isEmpty()) {
            return new ArrayList<>();
        }

        return joinedChatIds.stream()
                .map(participant -> participant.getChatroom())
                .map(ChatRoomDto::new)
                .distinct()
                .collect(Collectors.toList());
    }
}
