package site.soconsocon.socon.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.soconsocon.socon.store.domain.dto.request.AddMySoconRequest;
import site.soconsocon.socon.store.service.IssueService;
import site.soconsocon.utils.MessageUtils;

@RestController
@RequestMapping("/api/v1/issues")
@RequiredArgsConstructor
public class IssueApiController {

    private final IssueService issueService;

    // 소콘 발행(생성)
    public ResponseEntity<Object> saveMySocon(
            AddMySoconRequest request
    ){
        issueService.saveMySocon(request);

        return ResponseEntity.ok().body(MessageUtils.success(null));
    }

    // 소콘 발행 중지
    @PutMapping("/{issue_id}")
    public ResponseEntity<Object> stopIssue(
            @PathVariable("issue_id") Integer issueId,
            @RequestHeader("X-Authorization-Id") int memberId
    ){
        issueService.stopIssue(issueId, memberId);

        return ResponseEntity.ok().body(MessageUtils.success(null));
    }
}
