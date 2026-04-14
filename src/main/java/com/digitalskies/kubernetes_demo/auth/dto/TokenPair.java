package com.digitalskies.kubernetes_demo.auth.dto;

public record TokenPair(
        String accessToken,
        String refreshToken
) {
}
