package com.sollertia.habit.domain.user;

import com.sollertia.habit.exception.InvalidSocialNameException;

public enum UserType {

    Google(AuthType.Google),
    Kakao(AuthType.Kakao),
    Naver(AuthType.Naver);

    private final String type;

    UserType(String type) {this.type = type;}

    public String getAuthType(){return this.type;}

    private static class AuthType{
        private static String Google = "TYPE_GOOGLE";
        private static String Kakao = "TYPE_KAKAO";
        private static String Naver = "TYPE_NAVER";
    }

    public static UserType from(ProviderType providerType) {
        switch (providerType){
            case KAKAO: return UserType.Kakao;
            case NAVER: return UserType.Naver;
            case GOOGLE: return UserType.Google;
            default: throw new InvalidSocialNameException("잘못된 소셜 로그인 타입입니다.");
        }
    }
}
