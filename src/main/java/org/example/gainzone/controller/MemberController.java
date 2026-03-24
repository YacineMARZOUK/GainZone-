package org.example.gainzone.controller;

import lombok.RequiredArgsConstructor;
import org.example.gainzone.dto.request.MemberProfileRequest;
import org.example.gainzone.dto.response.MemberProfileResponse;
import org.example.gainzone.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MEMBER')")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/profile")
    public ResponseEntity<MemberProfileResponse> getMyProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(memberService.getMyProfile(email));
    }

    @PutMapping("/profile")
    public ResponseEntity<MemberProfileResponse> updateMyProfile(@RequestBody MemberProfileRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(memberService.updateMyProfile(email, request));
    }
}
