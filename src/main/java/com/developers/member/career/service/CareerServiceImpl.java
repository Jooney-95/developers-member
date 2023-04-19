package com.developers.member.career.service;

import com.developers.member.career.dto.request.MemberCareerSaveRequest;
import com.developers.member.career.dto.request.MemberCareerUpdateRequest;
import com.developers.member.career.dto.response.*;
import com.developers.member.career.entity.Career;
import com.developers.member.career.repository.CareerRepository;
import com.developers.member.member.entity.Member;
import com.developers.member.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class CareerServiceImpl implements CareerService {
    private final CareerRepository careerRepository;
    private final MemberRepository memberRepository;

    @Override
    public MemberCareerGetResponse getCareerInfo(Long memberId) {
        try {
            Optional<Member> member = memberRepository.findById(memberId);
            if (member.isPresent()) {
                List<Career> careers = careerRepository.findByMember(member.get());
                List<MemberOfCareer> careerList = new ArrayList<>();
                for(Career career : careers) {
                    MemberOfCareer m = MemberOfCareer.builder()
                            .company(career.getCompany())
                            .careerStart(career.getStart())
                            .careerEnd(career.getEnd())
                            .build();
                    careerList.add(m);
                }
                CareerInfo careerInfo = CareerInfo.builder()
                        .introduce(member.get().getIntroduce())
                        .positions(member.get().getPosition())
                        .skills(member.get().getSkills())
                        .careerList(careerList)
                        .build();
                return MemberCareerGetResponse.builder()
                        .code(HttpStatus.OK.toString())
                        .msg("정상적으로 이력 및 경력정보를 조회하였습니다.")
                        .data(careerInfo)
                        .build();
            } else {
                return MemberCareerGetResponse.builder()
                        .code(HttpStatus.NOT_FOUND.toString())
                        .msg("존재하지 않는 사용자입니다.")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            return MemberCareerGetResponse.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .msg("이력 및 경력정보 조회 중 문제가 발생하였습니다.")
                    .data(null)
                    .build();
        }
    }

    @Override
    public MemberCareerResponse saveCareer(MemberCareerSaveRequest request) {
        try {
            Optional<Member> member = memberRepository.findById(request.getMemberId());
            if(member.isPresent()) {
                Career career = Career.builder()
                        .member(member.get())
                        .company(request.getCompany())
                        .start(request.getCareerStart())
                        .end(request.getCareerEnd())
                        .build();
                careerRepository.save(career);
                CareerIdResponse careerId = CareerIdResponse.builder()
                        .careerId(career.getCareerId())
                        .build();
                return MemberCareerResponse.builder()
                        .code(HttpStatus.OK.toString())
                        .msg("정상적으로 경력정보가 등록되었습니다.")
                        .data(careerId)
                        .build();
            } else {
                return MemberCareerResponse.builder()
                        .code(HttpStatus.NOT_FOUND.toString())
                        .msg("존재하지 않는 사용자입니다.")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            return MemberCareerResponse.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .msg("경력정보를 등록하던 중 문제가 발생하였습니다.")
                    .data(null)
                    .build();
        }
    }

    @Transactional
    @Override
    public MemberCareerResponse updateCareer(MemberCareerUpdateRequest request) {
        try {
            Optional<Career> career = careerRepository.findById(request.getCareerId());
            if (career.isPresent()) {
                career.get().updateCompany(request.getCompany());
                career.get().changeStartDate(request.getCareerStart());
                career.get().changeEndDate(request.getCareerEnd());
                CareerIdResponse careerId = CareerIdResponse.builder()
                        .careerId(career.get().getCareerId())
                        .build();
                return MemberCareerResponse.builder()
                        .code(HttpStatus.OK.toString())
                        .msg("정상적으로 경력정보가 변경되었습니다.")
                        .data(careerId)
                        .build();
            } else {
                return MemberCareerResponse.builder()
                        .code(HttpStatus.NOT_FOUND.toString())
                        .msg("존재하지 않는 경력정보입니다.")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            return MemberCareerResponse.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .msg("경력정보를 변경하던 중 문제가 발생하였습니다.")
                    .data(null)
                    .build();
        }
    }

    @Override
    public MemberCareerResponse removeCareer(Long careerId) {
        try {
            Optional<Career> career = careerRepository.findById(careerId);
            if (career.isPresent()) {
                careerRepository.deleteById(career.get().getCareerId());
                CareerIdResponse deleteCareerId = CareerIdResponse.builder()
                        .careerId(careerId)
                        .build();
                return MemberCareerResponse.builder()
                        .code(HttpStatus.OK.toString())
                        .msg("정상적으로 경력정보를 삭제하였습니다.")
                        .data(deleteCareerId)
                        .build();
            } else {
                return MemberCareerResponse.builder()
                        .code(HttpStatus.NOT_FOUND.toString())
                        .msg("존재하지 않는 경력정보입니다.")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            return MemberCareerResponse.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .msg("경력정보를 삭제하던 중 문제가 발생하였습니다.")
                    .data(null)
                    .build();
        }
    }
}
