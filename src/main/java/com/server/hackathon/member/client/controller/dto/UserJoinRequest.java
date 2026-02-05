package com.server.hackathon.member.client.controller.dto;

public record UserJoinRequest(
        String username,
        String password,
        String email
) {

}
