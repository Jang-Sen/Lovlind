package com.project.lovlind.conmon.requset.argument.resolver;

import com.project.lovlind.conmon.requset.dto.CurrentUser;
import com.project.lovlind.conmon.utils.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CurrentUserResolver implements HandlerMethodArgumentResolver {

  private final JwtProvider jwtProvider;
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType().equals(CurrentUser.class);
  }

  @Override
  public CurrentUser resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

    String accessToken = webRequest.getHeader("Authorization");

    String jwt = accessToken.replace("Bearer ", "");
    Map<String, Object> claims = jwtProvider.getClaims(jwt);
    Long memberId = Long.valueOf((int) claims.get("id"));
    return new CurrentUser(memberId);
  }
}
