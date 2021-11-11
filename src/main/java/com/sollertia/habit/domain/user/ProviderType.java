package com.sollertia.habit.domain.user;

public enum ProviderType {

    GOOGLE(AuthType.GOOGLE),
    KAKAO(AuthType.KAKAO),
    NAVER(AuthType.NAVER);

    private final String type;

    ProviderType(String type) {this.type = type;}

    public String getAuthType() {
        return this.type;
    }

    private static class AuthType{
        private static final String GOOGLE = "TYPE_GOOGLE";
        private static final String KAKAO = "TYPE_KAKAO";
        private static final String NAVER = "TYPE_NAVER";
    }
}
