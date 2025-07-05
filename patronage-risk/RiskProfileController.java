package com.example.riskprofile.controller;

import com.example.riskprofile.dto.RiskProfileRequest;
import com.example.riskprofile.dto.RiskProfileResponse;
import com.example.riskprofile.service.RiskProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 风险画像控制器
 * 处理风险承受能力评测问卷的提交和计算
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RiskProfileController {

    @Autowired
    private RiskProfileService riskProfileService;

    /**
     * 提交风险画像问卷
     * @param request 风险画像问卷数据
     * @return 提交结果
     */
    @PostMapping("/risk-profile")
    public ResponseEntity<RiskProfileResponse> submitRiskProfile(@RequestBody RiskProfileRequest request) {
        try {
            RiskProfileResponse response = riskProfileService.calculateRiskProfile(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(RiskProfileResponse.builder()
                            .success(false)
                            .message("提交失败: " + e.getMessage())
                            .build());
        }
    }

    /**
     * 获取风险等级说明
     * @return 风险等级说明
     */
    @GetMapping("/risk-levels")
    public ResponseEntity<Map<String, Object>> getRiskLevels() {
        Map<String, Object> riskLevels = Map.of(
                "low", Map.of(
                        "name", "保守型",
                        "description", "适合低风险投资，偏好稳定收益",
                        "score_range", "0-150"
                ),
                "mid", Map.of(
                        "name", "稳健型",
                        "description", "适合中等风险投资，平衡收益与风险",
                        "score_range", "151-250"
                ),
                "high", Map.of(
                        "name", "进取型",
                        "description", "适合高风险投资，追求高收益",
                        "score_range", "251-395"
                )
        );
        return ResponseEntity.ok(riskLevels);
    }
}