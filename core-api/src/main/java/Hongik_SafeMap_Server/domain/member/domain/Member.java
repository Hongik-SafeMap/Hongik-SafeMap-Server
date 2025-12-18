package Hongik_SafeMap_Server.domain.member.domain;

import Hongik_SafeMap_Server.vo.LoginType;
import Hongik_SafeMap_Server.vo.MemberStatus;
import Hongik_SafeMap_Server.exception.MemberException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static Hongik_SafeMap_Server.exception.ErrorMessage.EMAIL_DOES_NOT_EXIST;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(length = 50)
    private String email;

    @Column(length = 200)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private MemberStatus status;

    @Column(length = 20)
    private String name;

    @Column(length = 50)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private LoginType loginType;

    @Column(length = 100)
    private String socialId;

    @Column(nullable = false)
    private boolean isCredible = false;

    @Builder
    public Member(String email, String password, MemberStatus status, String name, String phone, LoginType loginType, String socialId, boolean isCredible) {
        this.email = email;
        this.password = password;
        this.status = status;
        this.name = name;
        this.phone = phone;
        this.loginType = loginType;
        this.socialId = socialId;
        this.isCredible = isCredible;
    }

    public void validateEmail(String email) {
        if(!this.email.equals(email)) {
            throw new MemberException(EMAIL_DOES_NOT_EXIST);
        }
    }

    public void updatePassword(String encodedPassword) { this.password = encodedPassword; }

    public void toggleCredible() {
        this.isCredible = !this.isCredible;
    }
}
