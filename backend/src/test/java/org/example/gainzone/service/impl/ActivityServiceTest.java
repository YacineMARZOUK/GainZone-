package org.example.gainzone.service.impl;

import org.example.gainzone.dto.request.ActivityRequest;
import org.example.gainzone.dto.response.ActivityResponse;
import org.example.gainzone.entity.Activity;
import org.example.gainzone.entity.User;
import org.example.gainzone.mapper.ActivityMapper;
import org.example.gainzone.repository.ActivityRepository;
import org.example.gainzone.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private ActivityMapper activityMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ActivityServiceImpl activityService;

    private User coach;
    private Activity activity;
    private ActivityRequest activityRequest;
    private ActivityResponse activityResponse;

    @BeforeEach
    void setUp() {
        coach = User.builder()
                .id(1L)
                .email("coach@gainzone.com")
                .name("Coach")
                .build();

        activity = Activity.builder()
                .id(1L)
                .name("Yoga Session")
                .description("Morning yoga")
                .type("YOGA")
                .dateTime(LocalDateTime.now().plusDays(1))
                .durationMinutes(60)
                .maxParticipants(20)
                .coach(coach)
                .build();

        activityRequest = new ActivityRequest(
                "Yoga Session",
                "Morning yoga",
                "YOGA",
                LocalDateTime.now().plusDays(1),
                60,
                20
        );

        activityResponse = new ActivityResponse(
                1L,
                "Yoga Session",
                "Morning yoga",
                "YOGA",
                activity.getDateTime(),
                60,
                20,
                0,
                1L,
                "Coach"
        );
    }

    @Test
    void createActivity_Success() {
        try (MockedStatic<SecurityContextHolder> mockedSecurity = mockStatic(SecurityContextHolder.class)) {
            // Mock Security Context
            SecurityContext securityContext = mock(SecurityContext.class);
            Authentication authentication = mock(Authentication.class);
            when(authentication.getName()).thenReturn("coach@gainzone.com");
            when(securityContext.getAuthentication()).thenReturn(authentication);
            mockedSecurity.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            // Mock Service Dependencies
            when(userRepository.findByEmail("coach@gainzone.com")).thenReturn(Optional.of(coach));
            when(activityMapper.toEntity(activityRequest)).thenReturn(activity);
            when(activityRepository.saveAndFlush(any(Activity.class))).thenReturn(activity);
            when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
            when(activityMapper.toResponse(activity)).thenReturn(activityResponse);

            // Execute
            ActivityResponse result = activityService.createActivity(activityRequest);

            // Verify
            assertNotNull(result);
            assertEquals(activityResponse.id(), result.id());
            assertEquals(activityResponse.name(), result.name());
            verify(activityRepository).saveAndFlush(any(Activity.class));
            verify(userRepository).findByEmail("coach@gainzone.com");
        }
    }

    @Test
    void getAllActivities_Success() {
        // Mock
        List<Activity> activities = List.of(activity);
        List<ActivityResponse> responses = List.of(activityResponse);
        when(activityRepository.findAll()).thenReturn(activities);
        when(activityMapper.toResponseList(activities)).thenReturn(responses);

        // Execute
        List<ActivityResponse> result = activityService.getAllActivities();

        // Verify
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(activityResponse.name(), result.get(0).name());
        verify(activityRepository).findAll();
    }

    @Test
    void updateActivity_Success() {
        // Mock
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        when(activityRepository.save(activity)).thenReturn(activity);
        when(activityMapper.toResponse(activity)).thenReturn(activityResponse);

        // Execute
        ActivityResponse result = activityService.updateActivity(1L, activityRequest);

        // Verify
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(activityMapper).updateEntityFromRequest(activityRequest, activity);
        verify(activityRepository).save(activity);
    }

    @Test
    void updateActivity_NotFound() {
        // Mock
        when(activityRepository.findById(99L)).thenReturn(Optional.empty());

        // Execute & Verify
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            activityService.updateActivity(99L, activityRequest)
        );
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(activityRepository, never()).save(any());
    }

    @Test
    void deleteActivity_Success() {
        // Mock
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        when(activityMapper.toResponse(activity)).thenReturn(activityResponse);

        // Execute
        ActivityResponse result = activityService.deleteActivity(1L);

        // Verify
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(activityRepository).delete(activity);
    }

    @Test
    void deleteActivity_NotFound() {
        // Mock
        when(activityRepository.findById(99L)).thenReturn(Optional.empty());

        // Execute & Verify
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            activityService.deleteActivity(99L)
        );
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(activityRepository, never()).delete(any());
    }
}
