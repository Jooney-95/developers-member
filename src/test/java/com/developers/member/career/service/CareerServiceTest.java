package com.developers.member.career.service;

import com.developers.member.career.dto.request.MemberCareerSaveRequest;
import com.developers.member.career.dto.request.MemberCareerUpdateRequest;
import com.developers.member.career.dto.response.CareerIdResponse;
import com.developers.member.career.dto.response.CareerInfo;
import com.developers.member.career.dto.response.MemberCareerGetResponse;
import com.developers.member.career.dto.response.MemberCareerResponse;
import com.developers.member.career.entity.Career;
import com.developers.member.career.repository.CareerRepository;
import com.developers.member.member.entity.Member;
import com.developers.member.member.entity.Role;
import com.developers.member.member.entity.Type;
import com.developers.member.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WithMockUser(roles = "USER")
public class CareerServiceTest {
    @Mock
    private CareerRepository careerRepository;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private CareerServiceImpl careerService;

    @DisplayName("사용자 경력정보 등록")
    @Test
    public void saveCareer() {
        LocalDate curruntDate = LocalDate.now();
        // given
        MemberCareerSaveRequest request = MemberCareerSaveRequest.builder()
                .company("마이다스 아이티")
                .careerStart(curruntDate)
                .careerEnd(curruntDate)
                .build();
        Career career = request.toEntity();
        when(careerRepository.save(any())).thenReturn(career);

        // when
        MemberCareerResponse response = careerService.saveCareer(request);

        // then
        assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
        assertThat(response.getMsg()).isEqualTo("정상적으로 경력정보가 등록되었습니다.");
        assertThat(response.getData()).isInstanceOf(CareerIdResponse.class);
        assertThat(response.getData().getCareerId()).isEqualTo(career.getCareerId());
    }

    @DisplayName("사용자의 이력 및 경력 정보 모두 조회")
    @Test
    public void getCareerInfo() {
        // given
        Long memberId = 1L;
        Member member = Member.builder()
                .email("career001@kakao.com")
                .password("kakao123")
                .nickname("career001")
                .type(Type.LOCAL)
                .role(Role.USER)
                .isMentor(false)
                .point(100L)
                .build();
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        List<Career> careerList = new ArrayList<>();
        Career career1 = Career.builder()
                .company("우아한형제들 정산서비스팀")
                .start(LocalDate.now())
                .end(LocalDate.now())
                .build();
        Career career2 = Career.builder()
                .company("네이버 파이넨셜")
                .start(LocalDate.now())
                .end(LocalDate.now())
                .build();
        careerList.add(career1);
        careerList.add(career2);
        when(careerRepository.findByMember(any())).thenReturn(careerList);

        // when
        MemberCareerGetResponse response = careerService.getCareerInfo(memberId);

        // then
        assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
        assertThat(response.getMsg()).isEqualTo("정상적으로 이력 및 경력정보를 조회하였습니다.");
        assertThat(response.getData()).isInstanceOf(CareerInfo.class);
        assertThat(response.getData().getCareerList().get(0).getCompany()).isEqualTo(career1.getCompany());
        assertThat(response.getData().getCareerList().get(1).getCompany()).isEqualTo(career2.getCompany());
    }

    @DisplayName("사용자의 특정 경력정보 변경")
    @Test
    public void updateCareer() {
        // given
        MemberCareerUpdateRequest request = MemberCareerUpdateRequest.builder()
                .careerId(2L)
                .company("카카오 페이")
                .careerStart(LocalDate.now())
                .careerEnd(LocalDate.now())
                .build();
        Career career = Career.builder()
                .company("카카오")
                .start(LocalDate.now())
                .end(LocalDate.now())
                .build();
        when(careerRepository.findById(any())).thenReturn(Optional.of(career));

        // when
        MemberCareerResponse response = careerService.updateCareer(request);

        // then
        assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
        assertThat(response.getMsg()).isEqualTo("정상적으로 경력정보가 변경되었습니다.");
        assertThat(response.getData()).isInstanceOf(CareerIdResponse.class);
    }

    @DisplayName("사용자의 특정 경력정보 삭제")
    @Test
    public void removeCareer() {
        // given
        Long careerId = 1L;
        Career career = Career.builder()
                .company("카카오")
                .start(LocalDate.now())
                .end(LocalDate.now())
                .build();
        when(careerRepository.findById(any())).thenReturn(Optional.of(career));

        // when
        MemberCareerResponse response = careerService.removeCareer(1L);

        // then
        assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
        assertThat(response.getMsg()).isEqualTo("정상적으로 경력정보를 삭제하였습니다.");
        assertThat(response.getData()).isInstanceOf(CareerIdResponse.class);
    }



}
