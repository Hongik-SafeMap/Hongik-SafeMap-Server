package Hongik_SafeMap_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SnsAuthResponse {
    private String email;
    private String socialId;
    private String name;
    private String phone;
}
