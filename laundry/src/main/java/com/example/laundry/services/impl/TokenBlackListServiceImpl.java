package com.example.laundry.services.impl;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TokenBlackListServiceImpl {
    private final Set<String> blacklistedTokens = new HashSet<>();

    public void addBlacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
