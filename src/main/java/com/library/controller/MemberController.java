package com.library.controller;

import com.library.dto.MemberRequestDTO;
import com.library.dto.MemberResponseDTO;
import com.library.service.MemberService;
import com.library.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "Members", description = "CRUD operations for library members")
public class MemberController {

    private final MemberService memberService;
    private final NotificationService notificationService;

    @PostMapping
    @Operation(summary = "Register a new member")
    public ResponseEntity<MemberResponseDTO> create(@Valid @RequestBody MemberRequestDTO request) {
        MemberResponseDTO created = memberService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/{id}/notify")
    @Operation(summary = "Send an asynchronous notification to a member")
    public ResponseEntity<String> notifyMember(@PathVariable Long id) {
        notificationService.sendAsyncNotification("Hello Member " + id + ", your book is ready!");
        return ResponseEntity.ok("Notification is being processed in the background...");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a member by id")
    public ResponseEntity<MemberResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getById(id));
    }

    @GetMapping
    @Operation(summary = "List members (paginated & sortable)")
    public ResponseEntity<Page<MemberResponseDTO>> getAll(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(memberService.getAll(pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing member")
    public ResponseEntity<MemberResponseDTO> update(@PathVariable Long id,
                                                    @Valid @RequestBody MemberRequestDTO request) {
        return ResponseEntity.ok(memberService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a member")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        memberService.delete(id);
        return ResponseEntity.noContent().build();
    }
}