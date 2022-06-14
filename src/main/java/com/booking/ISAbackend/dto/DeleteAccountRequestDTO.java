package com.booking.ISAbackend.dto;

public class DeleteAccountRequestDTO {

    public int userId;
    public String userFirstName;
    public String userLastName;
    public String role;
    public String reason;
    public int requestId;

    public DeleteAccountRequestDTO(int userId, String userFirstName, String userLastName, String role, String reason, int requestId) {
        this.userId = userId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.role = role;
        this.reason = reason;
        this.requestId = requestId;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public String getRole() {
        return role;
    }

    public String getReason() {
        return reason;
    }

    public int getRequestId() {
        return requestId;
    }
}
