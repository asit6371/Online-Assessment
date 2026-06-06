package com.onlineassessment.dto;

import com.onlineassessment.enums.Difficulty;
import com.onlineassessment.enums.Topic;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class QuestionRequestDto {

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotBlank(message = "Sample input required")
    private String sampleInput;

    @NotBlank(message = "Sample output required")
    private String sampleOutput;

    @NotBlank(message = "Constraints required")
    private String constraints;

    @NotNull(message = "Difficulty required")
    private Difficulty difficulty;

    @NotNull(message = "Topic required")
    private Topic topic;

    /*
        What user sees in editor.
        Admin must provide this when creating a question.
        Example for Two Sum:
            class Solution {
                public static int[] twoSum(int[] nums, int target) {
                    // write your solution here
                }
            }
    */
    @NotBlank(message = "Starter code required")
    private String starterCode;

    /*
        Hidden driver — wraps user's Solution class.
        Admin must provide this when creating a question.
        Must be a complete Main class that:
          - Reads input from stdin
          - Calls Solution method
          - Prints output to stdout
        Example for Two Sum:
            import java.util.*;
            public class Main {
                public static void main(String[] args) {
                    Scanner sc = new Scanner(System.in);
                    int n = sc.nextInt();
                    int[] nums = new int[n];
                    for (int i = 0; i < n; i++) nums[i] = sc.nextInt();
                    int target = sc.nextInt();
                    int[] result = new Solution().twoSum(nums, target);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < result.length; i++) {
                        if (i > 0) sb.append(" ");
                        sb.append(result[i]);
                    }
                    System.out.println(sb.toString().trim());
                }
            }
    */
    @NotBlank(message = "Driver code required")
    private String driverCode;

    private List<TestCaseDto> testCases;
}