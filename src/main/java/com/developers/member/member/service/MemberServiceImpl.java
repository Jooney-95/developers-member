package com.developers.member.member.service;

import com.developers.member.member.dto.request.MemberRegisterRequest;
import com.developers.member.member.dto.request.NicknameUpdateRequest;
import com.developers.member.member.dto.request.PasswordChangeRequest;
import com.developers.member.member.dto.request.ProfileImageUpdateRequest;
import com.developers.member.member.dto.response.*;
import com.developers.member.member.entity.Member;
import com.developers.member.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 *
 * register: 회원가입
 * getWriterInfo: 사용자의 닉네임 정보를 전달하는 Open API
 * getMentorInfo: 멘토인 사용자의 닉네임 정보를 전달하는 Open API
 */
@Log4j2
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public MemberRegisterResponse register(MemberRegisterRequest request) {
        try {
            Member member = request.toEntity();
            Long saveMemberId = memberRepository.save(member).getMemberId();
            MemberIdWithPointResponse response = MemberIdWithPointResponse.builder()
                    .memberId(saveMemberId)
                    .point(member.getPoint().getPoint())
                    .build();
            return MemberRegisterResponse.builder()
                    .code(HttpStatus.OK.toString())
                    .msg("회원가입이 정상적으로 처리되었습니다.")
                    .data(response)
                    .build();
        } catch (Exception e) {
            return MemberRegisterResponse.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .msg("회원가입 중 문제가 발생하였습니다.")
                    .data(null)
                    .build();
        }
    }

    @Override
    public MemberInfoResponse getWriterInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException(memberId + " 번호는 존재하지 않는 회원입니다."));
        return MemberInfoResponse.builder()
                .memberName(member.getNickname())
                .build();
    }

    @Override
    public MemberInfoResponse getMentorInfo(Long mentorId) {
        Member member = memberRepository.findByIsMentorIsAndMemberId(true, mentorId)
                .orElseThrow(() -> new NoSuchElementException(mentorId + " 번호는 멘토로 등록되지 않은 회원입니다."));
        return MemberInfoResponse.builder()
                .memberName(member.getNickname())
                .build();
    }

    @Override
    public ProfileGetResponse getProfile(Long memberId) {
        try {
            Optional<Member> member = memberRepository.findById(memberId);
            if(member.isPresent()) {
                Long point = member.get().getPoint().getPoint();
                ProfileResponse profile = ProfileResponse.builder()
                        .email(member.get().getEmail())
                        .nickname(member.get().getNickname())
                        .profileImageUrl(member.get().getProfileImageUrl())
                        .isMentor(member.get().isMentor())
                        .address(member.get().getAddress())
                        .introduce(member.get().getIntroduce())
                        .position(member.get().getPosition())
                        .skills(member.get().getSkills())
                        .point(point)
                        .build();
                return ProfileGetResponse.builder()
                        .code(HttpStatus.OK.toString())
                        .msg("정상적으로 나의 정보를 조회하였습니다.")
                        .data(profile)
                        .build();
            } else {
                return ProfileGetResponse.builder()
                        .code(HttpStatus.NOT_FOUND.toString())
                        .msg("존재하지 않는 사용자입니다.")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            return ProfileGetResponse.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .msg("나의 정보를 조회하던 중 문제가 발생하였습니다.")
                    .data(null)
                    .build();
        }
    }

    @Transactional
    @Override
    public NicknameUpdateResponse updateNickname(NicknameUpdateRequest request) {
        try {
            Optional<Member> member = memberRepository.findById(request.getMemberId());
            if(member.isPresent()) {
                boolean duplicate = memberRepository.existsByNickname(request.getNickname());
                System.out.println(duplicate);
                if(duplicate == true) {
                    return NicknameUpdateResponse.builder()
                            .code(HttpStatus.CONFLICT.toString())
                            .msg("이미 사용중인 닉네임입니다.")
                            .data(null)
                            .build();
                }
                member.get().updateNickname(request.getNickname());
                MemberIdWithNicknameResponse memberIdWithNickname = MemberIdWithNicknameResponse.builder()
                        .memberId(member.get().getMemberId())
                        .nickname(member.get().getNickname())
                        .build();
                return NicknameUpdateResponse.builder()
                        .code(HttpStatus.OK.toString())
                        .msg("정상적으로 닉네임을 변경하였습니다.")
                        .data(memberIdWithNickname)
                        .build();
            } else {
                return NicknameUpdateResponse.builder()
                        .code(HttpStatus.NOT_FOUND.toString())
                        .msg("존재하지 않는 사용자입니다.")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            return NicknameUpdateResponse.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .msg("닉네임 변경 중 문제가 발생하였습니다.")
                    .data(null)
                    .build();
        }
    }

    @Transactional
    @Override
    public ProfileImageUpdateResponse updateProfileImg(ProfileImageUpdateRequest request) {
        try {
            Optional<Member> member = memberRepository.findById(request.getMemberId());
            if(member.isPresent()) {
                String imagePath = request.getImagePath();
                member.get().updateProfileImageUrl(imagePath);
                ProfileImgPathResponse res = ProfileImgPathResponse.builder()
                        .path(imagePath)
                        .build();
                return ProfileImageUpdateResponse.builder()
                        .code(HttpStatus.OK.toString())
                        .msg("정상적으로 프로필 이미지를 변경하였습니다.")
                        .data(res)
                        .build();
            } else {
                return ProfileImageUpdateResponse.builder()
                        .code(HttpStatus.NOT_FOUND.toString())
                        .msg("존재하지 않는 사용자입니다.")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            return ProfileImageUpdateResponse.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .msg("프로필 이미지를 변경하던 중 문제가 발생하였습니다.")
                    .data(null)
                    .build();
        }
    }

    @Transactional
    @Override
    public PasswordChangeResponse changePassword(PasswordChangeRequest request) {
        try {
            Optional<Member> member = memberRepository.findById(request.getMemberId());
            if(member.isPresent()) {
                member.get().changePassword(request.getPassword());
                MemberIdResponse memberId = MemberIdResponse.builder()
                        .memberId(member.get().getMemberId())
                        .build();
                return PasswordChangeResponse.builder()
                        .code(HttpStatus.OK.toString())
                        .msg("정상적으로 비밀번호가 변경되었습니다.")
                        .data(memberId)
                        .build();
            } else {
                return PasswordChangeResponse.builder()
                        .code(HttpStatus.NOT_FOUND.toString())
                        .msg("존재하지 않는 사용자입니다.")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            return PasswordChangeResponse.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .msg("비밀번호를 변경하던 중 문제가 발생하였습니다.")
                    .data(null)
                    .build();
        }
    }
}
