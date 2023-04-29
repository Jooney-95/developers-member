package com.developers.member.auth.security.filter;

import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

@Log4j2
public class LoginFilter extends AbstractAuthenticationProcessingFilter {
    public LoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        log.info("[LoginFilter] Member Login Filter");
        // GET 방식의 경우 처리할 필요가 없다.
        if(request.getMethod().equalsIgnoreCase("GET")){
            log.info("[LoginFilter] GET METHOD NOT SUPPORT");
            return null;
        }
        // 로그인 요청을 했을 때 아이디와 비밀번호를 가져와서 Map으로 생성한다.
        Map<String, String> jsonData = parseRequestJSON(request);
        log.info("[LoginFilter] jsonData: {}", jsonData);
        // 아이디와 비밀번호를 다음 필터에 전송해서 사용하도록 설정한다.
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(jsonData.get("loginEmail"), jsonData.get("loginPassword"));
            log.info("[LoginFilter] authenticationToken: {}", authenticationToken);
            return getAuthenticationManager().authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            // 로그인 실패 응답 반환
            log.info("[LoginFilter] 로그인 실패: {}", HttpServletResponse.SC_UNAUTHORIZED);
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("status", HttpServletResponse.SC_UNAUTHORIZED);
            errorResponse.put("message", "로그인에 실패했습니다.");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(errorResponse.toString());
            return null;
        }

    }

    //파라미터를 읽어서 Map으로 만들어주는 메서드
    private Map<String,String> parseRequestJSON(HttpServletRequest request) {
        //JSON 데이터를 분석해서 mid, mpw 전달 값을 Map으로 처리
        try(Reader reader = new InputStreamReader(request.getInputStream())){
            Gson gson = new Gson();
            return gson.fromJson(reader, Map.class);
        }catch(Exception e){
            log.error(e.getMessage());
        }
        return null;
    }
}
