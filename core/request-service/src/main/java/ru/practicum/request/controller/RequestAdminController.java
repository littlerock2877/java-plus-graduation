package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.request.service.RequestService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/requests")
public class RequestAdminController {
    private final RequestService requestService;

    @GetMapping("/count")
    public Long getConfirmedRequestsCount(@RequestParam Integer eventId) {
        log.debug("Getting count of confirmed requests for event with id {} - Started", eventId);
        Long confirmedRequestsCount = requestService.getConfirmedRequestsCount(eventId);
        log.info("Getting count of confirmed requests for event with id {} - Finished", eventId);
        return confirmedRequestsCount;
    }
}