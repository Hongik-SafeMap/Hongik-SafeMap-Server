package Hongik_SafeMap_Server.exception;

public class ErrorMessage {
    // Member
    public static final String EMAIL_DOES_NOT_EXIST = "존재하지 않는 이메일입니다.";
    public static final String EMAIL_INVALID_FORMAT = "이메일 형식이 올바르지 않습니다.";
    public static final String PASSWORD_INVALID_FORMAT = "비밀번호는 특수문자, 영문자, 숫자를 포함한 8자리 이상 문자열입니다.";
    public static final String DUPLICATED_EMAIL = "이미 존재하는 이메일입니다.";
    public static final String PASSWORD_IS_DIFFERENT_FROM_CHECK = "비밀번호와 비밀번호 확인 입력값이 다릅니다.";
    public static final String MEMBER_NOT_EXISTS_WITH_EMAIL = "해당 이메일을 가진 회원이 없습니다.";
    public static final String MEMBER_NOT_EXISTS = "해당 회원이 존재하지 않습니다.";
    public static final String INVALID_CURRENT_PASSWORD = "현재 비밀번호가 올바르지 않습니다.";
    public static final String PASSWORD_SAME_AS_OLD = "새 비밀번호가 기존 비밀번호와 동일합니다";
    public static final String INVALID_LOGIN_TYPE = "유효하지 않은 로그인 타입입니다.";
    public static final String MEMBER_IS_NOT_ADMIN = "해당 회원은 관리자가 아닙니다.";
    public static final String EMERGENCY_CONTACT_NOT_FOUND = "비상연락망이 존재하지 않습니다.";

    // Auth
    public static final String INVALID_REFRESH_TOKEN = "유효하지 않은 Refresh Token입니다.";
    public static final String LOGIN_RATE_LIMIT_EXCEEDED = "로그인 시도 횟수를 초과했습니다. 잠시 후 다시 시도해주세요.";
    public static final String REFRESH_TOKEN_DOES_NOT_MATCH = "Refresh token이 일치하지 않습니다.";

    // SNS Auth
    public static final String UNSUPPORTED_SNS_LOGIN_TYPE = "지원하지 않는 소셜 로그인 타입입니다.";
    public static final String SNS_TOKEN_INVALID = "유효하지 않은 SNS 액세스 토큰입니다.";
    public static final String SNS_USER_INFO_REQUEST_FAILED = "SNS 사용자 정보 조회에 실패했습니다.";
    public static final String SNS_RESPONSE_EMPTY = "SNS 응답이 비어있습니다.";
    public static final String SNS_EMAIL_NOT_PROVIDED = "SNS 계정에서 이메일 정보를 제공하지 않았습니다.";
    public static final String SNS_SOCIAL_ID_NOT_PROVIDED = "SNS 사용자 식별자를 가져오지 못했습니다.";
    public static final String KAKAO_ACCOUNT_NOT_PROVIDED = "카카오 계정 정보를 가져오지 못했습니다.";
    public static final String SOCIAL_ACCOUNT_ALREADY_REGISTERED_WITH_OTHER_TYPE = "이미 다른 로그인 방식으로 가입된 이메일입니다.";

    // DisasterReport
    public static final String INVALID_DISASTER_REPORT = "해당 id를 가진 제보를 찾을 수 없습니다.";
    public static final String INVALID_DISASTER_REPORT_EVALUATION = "해당 제보의 제보 평가 정보를 찾을 수 없습니다.";
    public static final String BLINDED_DISASTER_REPORT = "블라인드 처리된 제보입니다.";
    public static final String ALREADY_ACCUSED_DISASTER_REPORT = "이미 신고한 제보입니다.";

    // DisasterReportGroup
    public static final String DISASTER_REPORT_GROUP_NOT_FOUND = "해당 id를 가진 그룹을 찾을 수 없습니다.";

    // LostReport
    public static final String LOST_REPORT_NOT_FOUND = "해당 id를 가진 실종신고를 찾을 수 없습니다.";

    // ResourceReport
    public static final String RESOURCE_REPORT_NOT_FOUND = "해당 id를 가진 자원 게시글을 찾을 수 없습니다.";

    // DisasterType
    public static final String DISASTER_TYPE_NOT_FOUND = "해당 재난 유형을 찾을 수 없습니다.";
    public static final String DISASTER_TYPE_ALREADY_EXISTS = "이미 존재하는 재난 유형입니다.";

    // SafetyTip
    public static final String SAFETY_TIP_NOT_FOUND = "해당 재난 유형을 찾을 수 없습니다.";

    // Terms (이용약관)
    public static final String TERMS_NOT_FOUND = "해당 이용약관을 찾을 수 없습니다.";
    public static final String TERMS_ALREADY_EXISTS = "이미 존재하는 버전의 이용약관입니다.";

    // PrivacyPolicy (개인정보처리방침)
    public static final String PRIVACY_POLICY_NOT_FOUND = "해당 개인정보처리방침을 찾을 수 없습니다.";
    public static final String PRIVACY_POLICY_ALREADY_EXISTS = "이미 존재하는 버전의 개인정보처리방침입니다.";

    // 게시물 공통
    public static final String REPORT_DELETE_UNAUTHORIZED = "본인이 작성한 게시물만 삭제할 수 있습니다";
    public static final String REPORT_UPDATE_UNAUTHORIZED = "본인이 작성한 게시물만 수정할 수 있습니다";
}
