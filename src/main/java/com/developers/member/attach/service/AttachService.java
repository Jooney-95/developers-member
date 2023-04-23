package com.developers.member.attach.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.developers.member.attach.dto.response.MemberProfileImgUpdateResponse;
import com.developers.member.attach.util.CommonUtils;
import com.developers.member.member.entity.Member;
import com.developers.member.member.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class AttachService {
    private final MemberRepository memberRepository;
    private AmazonS3 amazonS3Client;
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${cloud.aws.region.static}")
    private String region;

    // 생성자 호출 후 수행할 작업 - 초기화
    // Spring은 Bean에 Proxy 패턴을 적용함.
    // Proxy 패턴은 작성한 클래스 또는 서비스를 상속받아서 새로운 클래스의 객체를 만들어서 사용하는 패턴임.
    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey,
                this.secretKey);
        amazonS3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    // 파일 업로드를 처리할 메서드
    @Transactional
    public MemberProfileImgUpdateResponse uploadProfileImage(Long memberId, MultipartFile file) {
        boolean result = validateFileExists(file);
        // 업로드 할 파일이 없으면 종료
        if(result == false){
            log.info("[AttachService] 업로드할 파일이 없습니다.");
            return null;
        }
        // 파일 경로 생성
        String fileName = CommonUtils.buildFileName(memberId, file.getOriginalFilename());
        log.info("[AttachService] 업로드할 파일명: {}", fileName);

        // 파일 형식 설정
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());

        // 파일의 내용을 읽어서 S3에 전송
        try (InputStream inputStream = file.getInputStream()) {
            log.info("[AttachService] S3에 파일 업로드를 시도합니다.");
            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            log.info("[AttachService] S3에 파일 업로드 중 문제가 발생했습니다.");
            e.printStackTrace();
            return null;
        }
        // 동일한 버킷에 모든 파일을 업로드하는 경우는 fileName 만 데이터베이스에 저장
        // 버킷 여러 개를 사용하는 경우는 버킷 이름도 데이터베이스에 저장
        // 업로드 된 파일의 실제 URL을 리턴
        String imagePath = amazonS3Client.getUrl(bucketName, fileName).toString();
        log.info("[AttachService] S3 파일 업로드 성공, {}", imagePath);
        Optional<Member> member = memberRepository.findById(memberId);
        if (member.isEmpty()) {
            log.info("[AttachService] S3이미지등록: 존재하지 않는 사용자입니다.");
            return MemberProfileImgUpdateResponse.builder()
                    .code(HttpStatus.NOT_FOUND.toString())
                    .msg("존재하지 않는 사용자입니다.")
                    .build();
        }
        member.get().updateProfileImageUrl(imagePath);
        return MemberProfileImgUpdateResponse.builder()
                .code(HttpStatus.OK.toString())
                .msg("정상적으로 프로필 이미지를 등록했습니다.")
                .build();
    }

    // 업로드할 파일의 존재 여부를 리턴하는 메서드
    private boolean validateFileExists(MultipartFile multipartFile) {
        boolean result = true;
        if (multipartFile.isEmpty()) {
            result = false;
        }
        return result;
    }
}
