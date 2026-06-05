package com.shankong.service;

public interface VerificationCodeService {

    String generateCode(String email);

    boolean verifyCode(String email, String code);

    void removeCode(String email);

    void updatePassword(String email, String newPassword);

    boolean emailExists(String email);
}
