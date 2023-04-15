package com.developers.member.auth.security.handler;

import com.developers.member.member.entity.Member;
import com.developers.member.member.repository.MemberRepository;
import com.developers.member.util.JwtUtil;
import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        log.info("[LoginSuccessHandler] Login Success Handler");

        /**
         * onAuthenticationSuccess() 메소드에서 response.setContentType()을 통해
         * 로그인 성공 시 전송되는 응답 정보의 ContentType을 JSON으로 설정
         */
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        log.info("[LoginSuccessHandler] authentication: " + authentication);
        log.info("[LoginSuccessHandler] login member: " + authentication.getName());

        Map<String, Object> claim = Map.of("loginEmail", authentication.getName());
        // AccessToken 유효기간 30분
        String accessToken = jwtUtil.generateToken(claim, 30);
        // RefreshToken 유효기간 7일
        String refreshToken = jwtUtil.generateToken(claim, 7 * 24 * 60);

        // Member 엔티티를 조회합니다. 로그인한 사용자의 ID를 기준으로 조회합니다.
        String loginEmail = authentication.getName();
        Member member = memberRepository.findByEmail(loginEmail)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        // Member 엔티티에 RefreshToken 값을 저장합니다.
        member.setRefreshToken(refreshToken);
        memberRepository.save(member);

        // 리프레시 토큰을 HTTP Only 쿠키로 설정
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7일
        response.addCookie(refreshTokenCookie);

        Gson gson = new Gson();
        Map<String, String> keyMap = Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "memberId", String.valueOf(member.getMemberId())
        );

        String jsonStr = gson.toJson(keyMap);
        response.getWriter().println(jsonStr);
    }
}
