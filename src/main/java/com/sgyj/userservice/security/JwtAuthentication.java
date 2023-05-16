package com.sgyj.userservice.security;

public record JwtAuthentication(Long accountId, String email) {}
