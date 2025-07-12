package com.PatronageSystem.Patronage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/backtest")
@CrossOrigin(origins = "*")
public class FundBackTestController {

    @GetMapping("/run")
    public Map<String, Object> runBackTest() throws IOException, InterruptedException {
        // 1. 调用python脚本
        ProcessBuilder pb = new ProcessBuilder("python", "src/main/resources/py/fundback.py");
        pb.redirectErrorStream(true);
        Process process = pb.start();

        // 2. 读取python输出（可选：读取表格数据）
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        StringBuilder output = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        int exitCode = process.waitFor();

        // 3. 读取图片
        String imgPath = "doc/fund_performance.png";
        byte[] imgBytes = Files.readAllBytes(Paths.get(imgPath));
        String imgBase64 = Base64.getEncoder().encodeToString(imgBytes);

        // 4. 读取表格数据
        String tablePath = "doc/result.csv";
        List<String> tableLines = Files.readAllLines(Paths.get(tablePath), StandardCharsets.UTF_8);

        // 5. 返回前端
        Map<String, Object> result = new HashMap<>();
        result.put("img", imgBase64);
        result.put("table", tableLines);
        result.put("output", output.toString());
        return result;
    }
}