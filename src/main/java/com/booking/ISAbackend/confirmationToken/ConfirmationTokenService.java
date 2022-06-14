package com.booking.ISAbackend.confirmationToken;

import com.booking.ISAbackend.model.MyUser;

public interface ConfirmationTokenService {
    void verify(String token) throws Exception;

    void createVerificationToken(MyUser user, String token);
}
