package org.example.gainzone.service;

import org.example.gainzone.dto.request.MemberProfileRequest;
import org.example.gainzone.dto.response.MemberProfileResponse;

public interface MemberService {
    MemberProfileResponse getMyProfile(String email);
    MemberProfileResponse updateMyProfile(String email, MemberProfileRequest request);
}
