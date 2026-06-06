package com.onlineassessment.execution;

import com.onlineassessment.execution.dto.ExecutionResultDto;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Service
public class CodeExecutionService {

    /*
        Executes pure Java code with optional stdin input.
        Used by the raw /execute endpoint only.
    */
    public ExecutionResultDto executeJavaCode(
            String code,
            String input
    ) {
        return compile_and_run(code, input);
    }

    /*
        LeetCode-style execution:
        Combines the hidden driverCode (Main class) with the
        user's Solution class into one compilation unit.

        File layout compiled together:
            [driverCode]     ← Main class, reads stdin, calls Solution
            [userCode]       ← Solution class with user's method

        The judge calls this method — not executeJavaCode directly.
    */
    public ExecutionResultDto executeWithDriver(
            String driverCode,
            String userCode,
            String input
    ) {
        // Combine: driver first, then user's Solution class
        String combined = driverCode + "\n\n" + userCode;
        return compile_and_run(combined, input);
    }

    private ExecutionResultDto compile_and_run(
            String code,
            String input
    ) {
        ExecutionResultDto result = new ExecutionResultDto();

        try {
            // Create isolated temp directory for this execution
            Path tempDir = Files.createTempDirectory("judge-exec-");
            Path javaFile = tempDir.resolve("Main.java");
            Files.writeString(javaFile, code);

            // Step 1: Compile
            ProcessBuilder compileBuilder = new ProcessBuilder(
                    "javac", javaFile.toString()
            );
            compileBuilder.directory(tempDir.toFile());
            Process compileProcess = compileBuilder.start();
            int compileExit = compileProcess.waitFor();

            if (compileExit != 0) {
                String error = new String(
                        compileProcess.getErrorStream().readAllBytes()
                );
                result.setSuccess(false);
                result.setError(sanitizeCompileError(error));
                return result;
            }

            // Step 2: Run with 5-second timeout
            ProcessBuilder runBuilder = new ProcessBuilder(
                    "java", "-cp", tempDir.toString(), "Main"
            );
            runBuilder.directory(tempDir.toFile());
            Process runProcess = runBuilder.start();

            // Feed test case input to stdin
            if (input != null && !input.isBlank()) {
                runProcess.getOutputStream().write(input.getBytes());
                runProcess.getOutputStream().flush();
                runProcess.getOutputStream().close();
            }

            boolean finished = runProcess.waitFor(5, TimeUnit.SECONDS);

            if (!finished) {
                runProcess.destroyForcibly();
                result.setSuccess(false);
                result.setError("Time Limit Exceeded");
                return result;
            }

            if (runProcess.exitValue() != 0) {
                String runtimeError = new String(
                        runProcess.getErrorStream().readAllBytes()
                );
                result.setSuccess(false);
                result.setError(sanitizeRuntimeError(runtimeError));
                return result;
            }

            String output = new String(
                    runProcess.getInputStream().readAllBytes()
            );

            result.setSuccess(true);
            result.setOutput(output.trim());
            return result;

        } catch (IOException | InterruptedException e) {
            result.setSuccess(false);
            result.setError(e.getMessage());
            return result;
        }
    }

    /*
        Remove temp file paths from compile errors
        so users don't see internal server paths.
        e.g. "/tmp/judge-exec-123/Main.java:5: error: ..."
             becomes "Line 5: error: ..."
    */
    private String sanitizeCompileError(String raw) {
        if (raw == null) return "Compilation Error";
        return raw
                .replaceAll("/[^\\s]+/Main\\.java:(\\d+):", "Line $1:")
                .trim();
    }

    private String sanitizeRuntimeError(String raw) {
        if (raw == null) return "Runtime Error";
        // Remove internal class path noise
        return raw
                .replaceAll("at sun\\..*\\n?", "")
                .replaceAll("at java\\..*\\n?", "")
                .trim();
    }
}