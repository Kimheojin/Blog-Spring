package HeoJin.demoBlog.seo.controller;


import HeoJin.demoBlog.seo.service.SyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
@RequiredArgsConstructor
public class SyncController {

    private final SyncService syncService;


    // 개발자 싱크 맞추는

    @PostMapping("/api/seo/mongo-sync")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> triggerSync() {
        syncService.triggerSync();



        // sync 최신 sync 시간 기록하기
        // mysql 오염시키기 싫은데

        return ResponseEntity.ok("hello");
    }


}
