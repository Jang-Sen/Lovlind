package com.project.lovlind.conmon.utils.auth;

import static com.project.lovlind.conmon.utils.number.NumberUtils.isNumeric;

import com.project.lovlind.domain.chat.cache.dto.PrincipalDto;
import org.springframework.stereotype.Component;

@Component
public class AuthSolveUtils {

  public PrincipalDto createPrincipal(String accessToken) {
    if (!isNumeric(accessToken)) {
      return PrincipalDto.builder().memberId(1L).nickname("memberStubA").build();
    }
    return PrincipalDto.builder()
        .memberId(Long.parseLong(accessToken))
        .nickname("memberStub" + accessToken)
        .build();
  }
}
