package com.project.lovlind.conmon.config;

import com.project.lovlind.conmon.requset.argument.resolver.CurrentUserResolver;
import com.project.lovlind.conmon.utils.auth.AuthSolveUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
  private final AuthSolveUtils authSolveUtils;

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(new CurrentUserResolver(authSolveUtils));
  }
}
