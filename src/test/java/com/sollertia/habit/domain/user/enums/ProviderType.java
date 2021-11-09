package com.sollertia.habit.domain.user.enums;

public enum ProviderType {

    GOOGLE(AuthType.GOOGLE),
    KAKAO(AuthType.KAKAO),
    NAVER(AuthType.NAVER),
    NONE(AuthType.NONE);

    private final String type;

    ProviderType(String type) {this.type = type;}

    public String getAuthType() {
        return this.type;
    }

    private static class AuthType{
        private static final String GOOGLE = "TYPE_GOOGLE";
        private static final String KAKAO = "TYPE_KAKAO";
        private static final String NAVER = "TYPE_NAVER";
        private static final String NONE = "TYPE_NONE";
    }
}