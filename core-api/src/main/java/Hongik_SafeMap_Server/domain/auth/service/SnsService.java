package Hongik_SafeMap_Server.domain.auth.service;

import Hongik_SafeMap_Server.domain.auth.dto.response.SnsAuthResponse;
import Hongik_SafeMap_Server.exception.MemberException;
import Hongik_SafeMap_Server.vo.LoginType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static Hongik_SafeMap_Server.exception.ErrorMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnsService {
    private final RestTemplate restTemplate;

    public SnsAuthResponse verifyToken(String token, LoginType loginType) {
        if (loginType == LoginType.KAKAO) {
            return getKakaoUserInfo(token);
        }

        if (loginType == LoginType.GOOGLE) {
            return getGoogleUserInfo(token);
        }

        throw new MemberException(UNSUPPORTED_SNS_LOGIN_TYPE);
    }

    @SuppressWarnings("unchecked")
    private SnsAuthResponse getKakaoUserInfo(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);

            HttpEntity<Void> request = new HttpEntity<>(headers);

            ResponseEntity<Map> responseEntity = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    request,
                    Map.class
            );

            Map<String, Object> response = responseEntity.getBody();

            if (response == null) {
                throw new MemberException(SNS_RESPONSE_EMPTY);
            }

            String socialId = String.valueOf(response.get("id"));

            if (socialId == null || socialId.isBlank() || "null".equals(socialId)) {
                throw new MemberException(SNS_SOCIAL_ID_NOT_PROVIDED);
            }

            Map<String, Object> kakaoAccount =
                    (Map<String, Object>) response.get("kakao_account");

            if (kakaoAccount == null) {
                throw new MemberException(KAKAO_ACCOUNT_NOT_PROVIDED);
            }

            String email = (String) kakaoAccount.get("email");

            if (email == null || email.isBlank()) {
                throw new MemberException(SNS_EMAIL_NOT_PROVIDED);
            }

            Map<String, Object> profile =
                    (Map<String, Object>) kakaoAccount.get("profile");

            String name = null;
            if (profile != null) {
                name = (String) profile.get("nickname");
            }

            String phone = (String) kakaoAccount.get("phone_number");

            log.info("카카오 SNS 인증 성공: email={}, socialId={}", email, socialId);

            return new SnsAuthResponse(email, socialId, name, phone);

        } catch (MemberException e) {
            throw e;
        } catch (RestClientException e) {
            log.error("카카오 사용자 정보 조회 실패", e);
            throw new MemberException(SNS_USER_INFO_REQUEST_FAILED);
        }
    }

    private SnsAuthResponse getGoogleUserInfo(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);

            HttpEntity<Void> request = new HttpEntity<>(headers);

            ResponseEntity<Map> responseEntity = restTemplate.exchange(
                    "https://www.googleapis.com/oauth2/v3/userinfo",
                    HttpMethod.GET,
                    request,
                    Map.class
            );

            Map<String, Object> response = responseEntity.getBody();

            if (response == null) {
                throw new MemberException(SNS_RESPONSE_EMPTY);
            }

            String socialId = (String) response.get("sub");
            String email = (String) response.get("email");
            String name = (String) response.get("name");

            if (socialId == null || socialId.isBlank()) {
                throw new MemberException(SNS_SOCIAL_ID_NOT_PROVIDED);
            }

            if (email == null || email.isBlank()) {
                throw new MemberException(SNS_EMAIL_NOT_PROVIDED);
            }

            log.info("구글 SNS 인증 성공: email={}, socialId={}", email, socialId);

            return new SnsAuthResponse(email, socialId, name, null);

        } catch (MemberException e) {
            throw e;
        } catch (RestClientException e) {
            log.error("구글 사용자 정보 조회 실패", e);
            throw new MemberException(SNS_USER_INFO_REQUEST_FAILED);
        }
    }
}
