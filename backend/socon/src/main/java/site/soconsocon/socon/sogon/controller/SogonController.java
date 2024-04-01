package site.soconsocon.socon.sogon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.soconsocon.socon.sogon.domain.dto.request.AddCommentRequest;
import site.soconsocon.socon.sogon.domain.dto.request.AddSogonRequest;
import site.soconsocon.socon.sogon.domain.dto.request.GetSogonListRequest;
import site.soconsocon.socon.sogon.service.SogonService;
import site.soconsocon.utils.MessageUtils;

@RestController
@Slf4j
@RequestMapping("/api/v1/sogons")
@RequiredArgsConstructor
public class SogonController {

    private final SogonService sogonService;

    // 소곤 작성
    @PostMapping("")
    public ResponseEntity<Object> saveSogon(
            @RequestBody AddSogonRequest request,
            @RequestHeader("X-Authorization-Id") int memberId
    ){

        sogonService.addSogon(request, memberId);

        return ResponseEntity.ok().body(MessageUtils.success(null, "201 CREATED", null));
    }

    // 소곤 댓글 추가
    @PostMapping("/{sogon_id}/comment")
    public ResponseEntity<Object> addSogonComment(
        @PathVariable ("sogon_id") Integer sogonId,
        @RequestBody AddCommentRequest request,
        @RequestHeader("X-Authorization-Id") int memberId
    ){
        sogonService.addSogonComment(sogonId, request, memberId);

        return ResponseEntity.ok().body(MessageUtils.success());
    }

    // 댓글 채택
    @GetMapping("/{sogon_id}/comment/{comment_id}")
    public ResponseEntity<Object> pickSogonComment(
            @PathVariable ("sogon_id") Integer sogonId,
            @PathVariable ("comment_id") Integer commentId,
            @RequestHeader("X-Authorization-Id") int memberId
    ){
        sogonService.pickSogonComment(sogonId, commentId, memberId);

        return ResponseEntity.ok().body(MessageUtils.success());
    }

    // 소곤 상세 조회
    @GetMapping("/{sogon_id}")
    public ResponseEntity<Object> getSogon(
            @PathVariable ("sogon_id") Integer sogonId
    ){
        return ResponseEntity.ok().body(MessageUtils.success(sogonService.getSogon(sogonId)));
    }

    // 작성 소곤 목록 조회
    @GetMapping("/mine")
    public ResponseEntity<Object> getMySogons(
            @RequestHeader("X-Authorization-Id") int memberId
    ){
        return ResponseEntity.ok().body(MessageUtils.success(sogonService.getMySogons(memberId)));
    }

    // 작성 댓글 목록 조회
    @GetMapping("/mine/comments")
    public ResponseEntity<Object> getMyComments(
            @RequestHeader("X-Authorization-Id") int memberId
    ){
        return ResponseEntity.ok().body(MessageUtils.success(sogonService.getMyComments(memberId)));
    }

    // 반경 내 소곤 목록 조회
    @PostMapping("/list")
    public ResponseEntity<Object> getSogonList(
        @RequestBody GetSogonListRequest request
    ){
        return ResponseEntity.ok().body(MessageUtils.success(sogonService.getSogonList(request.getLat(), request.getLng())));
    }



}
