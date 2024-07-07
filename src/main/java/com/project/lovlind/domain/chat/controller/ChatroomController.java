package com.project.lovlind.domain.chat.controller;

import com.project.lovlind.conmon.requset.dto.CurrentUser;
import com.project.lovlind.domain.chat.controller.dto.ChatrommSearchFilter;
import com.project.lovlind.domain.chat.controller.dto.request.PostChatDto;
import com.project.lovlind.domain.chat.controller.dto.response.ChatRoomDto;
import com.project.lovlind.domain.chat.entity.Chatroom;
import com.project.lovlind.domain.chat.service.ChatroomService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatroom")
@RequiredArgsConstructor
public class ChatroomController {
  private final ChatroomService service;
  private static final String URL = "/api/chatroom";

  /** 채팅방 조회 */
  @GetMapping
  public ResponseEntity<List<ChatRoomDto>> getChatrooms(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "limit", defaultValue = "20") Integer limit) {
    ChatrommSearchFilter filter = new ChatrommSearchFilter(page, limit );

    return ResponseEntity.ok(service.getChatrooms(filter));
  }

  /** 내가 입장한 채팅방 조회 */
  @GetMapping("/my")
  public ResponseEntity<List<ChatRoomDto>> getMyChatRoom(CurrentUser currentUser, @ModelAttribute ChatrommSearchFilter filter) {
    return ResponseEntity.ok(service.getMyChatrooms(currentUser.getUserId(), filter));
  }

  /** 대화방 입장 */
    @PostMapping("/{chatRoomId}/join")
    public ResponseEntity<Map<String, Object>> joinChatroom(@PathVariable Long chatRoomId, CurrentUser user) {
//      service.joinChatroom(id, user.getUserId());
      // chatRoomId 가 존재하는가? -> 503 에러
      // chatRoom 에 인원이 꽉차지 않았는가? -> 503 에러
      // 이미 참여된 사람인가?
      // TODO : UI / UX  신고 가 3회 이상 누적이면 차단 / 차단유저일 경우 못들어간다.
      // 응답은.
      // 최근 목록 필요 없다.

      Map<String, Object> result = new HashMap<>();
      result.put("chatRoomId", chatRoomId);
      result.put("userId", user.getUserId());

      return ResponseEntity.ok(result);
    }

  /** 채팅방 나가기 */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> leaveChatroom(@PathVariable Long id, CurrentUser user) {
    service.leaveChatroom(id, user.getUserId());

    return ResponseEntity.ok().build();
  }


  @PostMapping
  public ResponseEntity<Map<String, Object>> postChatroom(@RequestBody PostChatDto dto, CurrentUser user) {

    Chatroom chatroomEntity = service.saveChatroom(dto);

    Map<String, Object> result = new HashMap<>();
    result.put("chatRoomId", chatroomEntity.getId());
    result.put("title", chatroomEntity.getTitle());
    result.put("maxParticipant", chatroomEntity.getMaxParticipant());

    // 채팅방을 만들고
    // 위 가입시 "대화방 입장" 이랑 로직을 잘 정리해서, 입장도 시킨다.
    return ResponseEntity.ok(result);
  }
}
