package com.sollertia.habit.domain.user;

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
}
