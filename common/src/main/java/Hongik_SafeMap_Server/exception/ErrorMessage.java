package Hongik_SafeMap_Server.exception;

public class ErrorMessage {
    // Member
    public static final String EMAIL_DOES_NOT_EXIST = "존재하지 않는 이메일입니다.";
    public static final String EMAIL_INVALID_FORMAT = "이메일 형식이 올바르지 않습니다.";
    public static final String PASSWORD_INVALID_FORMAT = "비밀번호는 특수문자, 영문자, 숫자를 포함한 8자리 이상 문자열입니다.";
    public static final String DUPLICATED_EMAIL = "이미 존재하는 이메일입니다.";
    public static final String PASSWORD_IS_DIFFERENT_FROM_CHECK = "비밀번호와 비밀번호 확인 입력값이 다릅니다.";
    public static final String MEMBER_NOT_EXISTS_WITH_EMAIL = "해당 이메일을 가진 회원이 없습니다.";
    public static final String INVALID_CURRENT_PASSWORD = "현재 비밀번호가 올바르지 않습니다.";
    public static final String PASSWORD_SAME_AS_OLD = "새 비밀번호가 기존 비밀번호와 동일합니다";
    public static final String INVALID_LOGIN_TYPE = "유효하지 않은 로그인 타입입니다.";
    public static final String INVALID_LOGIN = "비밀번호가 일치하지 않습니다.";

    // Auth
    public static final String INVALID_REFRESH_TOKEN = "유효하지 않은 Refresh Token입니다.";
    public static final String REFRESH_TOKEN_DOES_NOT_MATCH = "Rfresh token이 일치하지 않습니다.";
    public static final String NOT_USER = "재난 제보는 회원가입을 진행해야 합니다.";

    // DisasterReport
    public static final String INVALID_DISASTER_REPORT = "해당 id를 가진 제보를 찾을 수 없습니다.";
}
