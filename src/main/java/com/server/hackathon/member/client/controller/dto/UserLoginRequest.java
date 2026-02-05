package com.server.hackathon.member.client.controller.dto;

public record UserLoginRequest(
        String username,
        String password
) {

}
