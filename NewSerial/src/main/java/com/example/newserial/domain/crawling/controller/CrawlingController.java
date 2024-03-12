package com.example.newserial.domain.crawling.controller;

import com.example.newserial.domain.error.BadRequestException;
import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.member.service.AuthDataService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "Authorization")
public class CrawlingController {

    private final AuthDataService authDataService;

//    @PostMapping("/crawling")
//    public ResponseEntity<?> executePythonScript(HttpServletRequest request) {
//        try {
//            Member member = authDataService.checkAccessToken(request);
//            String[] command = {"python", "E:\\Newserial_SC\\lambda_function.py"};
//
//            // 외부 프로세스를 실행하기 위한 ProcessBuilder 생성
//            ProcessBuilder processBuilder = new ProcessBuilder(command);
//
//            // 외부 프로세스 실행
//            Process process = processBuilder.start();
//
//            // 프로세스의 종료를 기다림
//            int exitCode = process.waitFor();
//
//            // 프로세스의 종료 코드가 0인 경우 성공적으로 실행된 것으로 판단
//            if (exitCode == 0) {
//                return ResponseEntity.ok("Python script executed successfully.");
//            } else {
//                // 실행에 실패한 경우
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to execute Python script.");
//            }
//        } catch (BadRequestException e) {    //액세스 토큰, 리프레시 토큰 모두 만료된 경우
//            return authDataService.redirectToLogin();
//        } catch (IOException | InterruptedException e) {
//            // 예외 발생 시
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while executing Python script.");
//        }
//    }

    @PostMapping("/crawling")
    public ResponseEntity<?> executePythonScript(HttpServletRequest request) {
        try {
            Member member = authDataService.checkAccessToken(request);
            // 파이썬 스크립트가 위치한 경로
            String pythonScriptPath = "E:\\Newserial_SC\\lambda_function.py";

            // 파이썬 실행 명령어 및 스크립트 경로
            String[] cmd = {"python", pythonScriptPath};

            // ProcessBuilder를 사용하여 외부 프로세스 실행
            ProcessBuilder pb = new ProcessBuilder(cmd);
            Process process = pb.start();

            // 프로세스가 종료될 때까지 대기
            int exitCode = process.waitFor();
            System.out.println("Exited with error code " + exitCode);

            int code = process.exitValue();
            System.out.println("외부 프로그램 종료 코드: " + code);
            return ResponseEntity.ok("크롤링 실행 완료");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while executing Python script.");
        }
        catch (BadRequestException e) {    //액세스 토큰, 리프레시 토큰 모두 만료된 경우
            return authDataService.redirectToLogin();
        }
    }
}