package com.developers.member.member.service;

import com.developers.member.member.dto.request.*;
import com.developers.member.member.dto.response.*;
import com.developers.member.member.entity.Member;
import com.developers.member.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberRegisterResponse register(MemberRegisterRequest request) {
        try {
            Optional<Member> memberByEmail = memberRepository.findByEmail(request.getEmail());
            Optional<Member> memberByNickname = memberRepository.findByNickname(request.getNickname());
            if (memberByEmail.isPresent()) {
                log.info("[MemberServiceImpl] 회원가입: 이미 사용중인 이메일입니다.");
                return MemberRegisterResponse.builder()
                        .code(HttpStatus.BAD_REQUEST.toString())
                        .msg("이미 가입된 이메일입니다.")
                        .data(null)
                        .build();
            }else if (memberByNickname.isPresent()) {
                log.info("[MemberServiceImpl] 회원가입: 이미 사용중인 닉네임입니다.");
                return MemberRegisterResponse.builder()
                        .code(HttpStatus.BAD_REQUEST.toString())
                        .msg("이미 사용중인 닉네임입니다.")
                        .data(null)
                        .build();
            } else {
                log.info("[MemberServiceImpl] 회원가입: 회원가입 가능한 사용자입니다. {}, {}", request.getEmail(), request.getNickname());
                Member saveMember = request.toEntity();
                saveMember.changePassword(passwordEncoder.encode(saveMember.getPassword()));
                Long saveMemberId = memberRepository.save(saveMember).getMemberId();
                MemberIdResponse response = MemberIdResponse.builder()
                        .memberId(saveMemberId)
                        .build();
                return MemberRegisterResponse.builder()
                        .code(HttpStatus.OK.toString())
                        .msg("회원가입이 정상적으로 처리되었습니다.")
                        .data(response)
                        .build();
            }
        } catch (Exception e) {
            log.info("[MemberServiceImpl] 회원가입: 회원가입 중 문제가 발생했습니다.");
            e.printStackTrace();
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
                log.info("[MemberServiceImpl] 내 정보: 조회할 사용자 번호: {}", memberId);
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
                log.info("[MemberServiceImpl] 내 정보: 존재하지 않는 사용자입니다.");
                return ProfileGetResponse.builder()
                        .code(HttpStatus.NOT_FOUND.toString())
                        .msg("존재하지 않는 사용자입니다.")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            log.info("[MemberServiceImpl] 내 정보: 내 정보 조회중 문제가 발생했습니다.");
            e.printStackTrace();
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
                if(duplicate == true) {
                    log.info("[MemberServiceImpl] 닉네임변경: 이미 사용중인 닉네임 입니다. {}", request.getNickname());
                    return NicknameUpdateResponse.builder()
                            .code(HttpStatus.CONFLICT.toString())
                            .msg("이미 사용중인 닉네임입니다.")
                            .data(null)
                            .build();
                }
                log.info("[MemberServiceImpl] 닉네임변경: 사용이 가능한 닉네임 입니다. {}", request.getNickname());
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
                log.info("[MemberServiceImpl] 닉네임변경: 존재하지 않는 사용자입니다.");
                return NicknameUpdateResponse.builder()
                        .code(HttpStatus.NOT_FOUND.toString())
                        .msg("존재하지 않는 사용자입니다.")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            log.info("[MemberServiceImpl] 닉네임변경: 닉네임 변경 중 문제가 발생하였습니다.");
            e.printStackTrace();
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
                log.info("[MemberServiceImpl] 프로필이미지변경: 사용자 번호: {}", request.getMemberId());
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
                log.info("[MemberServiceImpl] 프로필이미지변경: 존재하지 않는 사용자입니다.");
                return ProfileImageUpdateResponse.builder()
                        .code(HttpStatus.NOT_FOUND.toString())
                        .msg("존재하지 않는 사용자입니다.")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            log.info("[MemberServiceImpl] 프로필이미지변경: 프로필 이미지를 변경하던 중 문제가 발생하였습니다.");
            e.printStackTrace();
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
                if(member.get().getPassword().equals(passwordEncoder.encode(request.getPassword()))) {
                    log.info("[MemberServiceImpl] 비밀번호변경: 변경할 비밀번호가 기존 비밀번호와 일치합니다.");
                    return PasswordChangeResponse.builder()
                            .code(HttpStatus.BAD_REQUEST.toString())
                            .msg("변경할 비밀번호가 기존 비밀번호와 일치합니다.")
                            .data(null)
                            .build();
                }
                log.info("[MemberServiceImpl] 비밀번호변경: 비밀번호 변경할 사용자 번호: {}", request.getMemberId());
                member.get().changePassword(passwordEncoder.encode(request.getPassword()));
                MemberIdResponse memberId = MemberIdResponse.builder()
                        .memberId(member.get().getMemberId())
                        .build();
                return PasswordChangeResponse.builder()
                        .code(HttpStatus.OK.toString())
                        .msg("정상적으로 비밀번호가 변경되었습니다.")
                        .data(memberId)
                        .build();
            } else {
                log.info("[MemberServiceImpl] 비밀번호변경: 존재하지 않는 사용자입니다.");
                return PasswordChangeResponse.builder()
                        .code(HttpStatus.NOT_FOUND.toString())
                        .msg("존재하지 않는 사용자입니다.")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            log.info("[MemberServiceImpl] 비밀번호변경: 비밀번호를 변경하던 중 문제가 발생하였습니다.");
            e.printStackTrace();
            return PasswordChangeResponse.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .msg("비밀번호를 변경하던 중 문제가 발생하였습니다.")
                    .data(null)
                    .build();
        }
    }

    @Transactional
    @Override
    public MemberResumeSaveResponse saveMemberResume(MemberResumeSaveRequest request) {
        try {
            Optional<Member> member = memberRepository.findById(request.getMemberId());
            if (member.isPresent()) {
                member.get().updateIntroduce(request.getIntroduce());
                member.get().updatePosition(request.getPositions());
                member.get().updateSkills(request.getSkills());
                MemberIdResponse memberId = MemberIdResponse.builder().memberId(member.get().getMemberId()).build();
                log.info("[MemberServiceImpl] 이력정보등록: 이력정보를 등록할 사용자 번호: {}", request.getMemberId());
                return MemberResumeSaveResponse.builder()
                        .code(HttpStatus.OK.toString())
                        .msg("정상적으로 이력정보를 등록하였습니다.")
                        .data(memberId)
                        .build();
            } else {
                log.info("[MemberServiceImpl] 이력정보등록: 존재하지 않는 사용자입니다.");
                return MemberResumeSaveResponse.builder()
                        .code(HttpStatus.NOT_FOUND.toString())
                        .msg("존재하지 않는 사용자입니다.")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            log.info("[MemberServiceImpl] 이력정보등록: 이력정보를 등록하던 중 문제가 발생하였습니다.");
            e.printStackTrace();
            return MemberResumeSaveResponse.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .msg("이력정보를 등록하던 중 문제가 발생하였습니다.")
                    .data(null)
                    .build();
        }
    }

    @Transactional
    @Override
    public AddressUpdateResponse updateAddress(AddressUpdateRequest request) {
        try {
            Optional<Member> member = memberRepository.findById(request.getMemberId());
            if (member.isPresent()) {
                member.get().updateAddress(request.getAddress());
                MemberIdResponse memberId = MemberIdResponse.builder()
                        .memberId(request.getMemberId())
                        .build();
                log.info("[MemberServiceImpl] 거주지변경: 거주지를 변경할 사용자 번호: {}", request.getMemberId());
                return AddressUpdateResponse.builder()
                        .code(HttpStatus.OK.toString())
                        .msg("정상적으로 거주지 정보를 변경했습니다.")
                        .data(memberId)
                        .build();
            } else {
                log.info("[MemberServiceImpl] 거주지변경: 존재하지 않는 사용자입니다.");
                return AddressUpdateResponse.builder()
                        .code(HttpStatus.NOT_FOUND.toString())
                        .msg("존재하지 않는 사용자입니다.")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            log.info("[MemberServiceImpl] 거주지변경: 거주지 정보를 변경하던 중 문제가 발생했습니다.");
            e.printStackTrace();
            return AddressUpdateResponse.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .msg("거주지 정보를 변경하던 중 문제가 발생했습니다.")
                    .data(null)
                    .build();
        }
    }

    @Override
    public MemberRemoveResponse removeMember(Long memberId) {
        try {
            Optional<Member> member = memberRepository.findById(memberId);
            if (member.isPresent()) {
                memberRepository.deleteById(memberId);
                MemberIdResponse removeMemberId = MemberIdResponse.builder().memberId(memberId).build();
                log.info("[MemberServiceImpl] 회원탈퇴: 회원 탈퇴할 사용자 번호: {}", memberId);
                return MemberRemoveResponse.builder()
                        .code(HttpStatus.OK.toString())
                        .msg("정상적으로 사용자 회원 탈퇴를 처리했습니다.")
                        .data(removeMemberId)
                        .build();
            } else {
                log.info("[MemberServiceImpl] 회원탈퇴: 존재하지 않는 사용자입니다.");
                return MemberRemoveResponse.builder()
                        .code(HttpStatus.NOT_FOUND.toString())
                        .msg("존재하지 않는 사용자입니다.")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            log.info("[MemberServiceImpl] 회원탈퇴: 사용자 회원 탈퇴를 하던 중 문제가 발생했습니다.");
            e.printStackTrace();
            return MemberRemoveResponse.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .msg("사용자 회원 탈퇴를 하던 중 문제가 발생했습니다.")
                    .data(null)
                    .build();
        }
    }

    @Transactional
    @Override
    public MentorRegisterResponse registerMentor(MentorRegisterReqeust request) {
        try {
            Optional<Member> member = memberRepository.findById(request.getMemberId());
            if (member.isPresent()) {
                member.get().applyMentor();
                MemberIdResponse memberId = MemberIdResponse.builder()
                        .memberId(request.getMemberId())
                        .build();
                log.info("[MemberServiceImpl] 멘토등록: 멘토로 등록할 사용자 번호: {}", request.getMemberId());
                return MentorRegisterResponse.builder()
                        .code(HttpStatus.OK.toString())
                        .msg("정상적으로 사용자 멘토 등록을 완료했습니다.")
                        .data(memberId)
                        .build();
            } else {
                log.info("[MemberServiceImpl] 멘토등록: 존재하지 않는 사용자입니다.");
                return MentorRegisterResponse.builder()
                        .code(HttpStatus.NOT_FOUND.toString())
                        .msg("존재하지 않는 사용자입니다.")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            log.info("[MemberServiceImpl] 멘토등록: 멘토 등록을 하던 중 문제가 발생했습니다.");
            e.printStackTrace();
            return MentorRegisterResponse.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .msg("멘토 등록을 하던 중 문제가 발생했습니다.")
                    .data(null)
                    .build();
        }
    }

    @Override
    public MemberLoginResponse getLoginMember(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            return MemberLoginResponse.builder()
                    .loginEmail(member.get().getEmail())
                    .loginPassword(member.get().getPassword())
                    .build();
        }
        return null;
    }

    @Transactional
    @Override
    public UpdateMemberRefreshResponse updateRefreshToken(UpdateMemberRefreshRequest request) {
        Optional<Member> member = memberRepository.findByEmail(request.getLoginEmail());
        if(member.isPresent()) {
            member.get().setRefreshToken(request.getRefreshToken());
            return UpdateMemberRefreshResponse.builder()
                    .code(HttpStatus.OK.toString())
                    .memberId(member.get().getMemberId())
                    .build();
        } else {
            return UpdateMemberRefreshResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.toString())
                    .build();
        }
    }
}
