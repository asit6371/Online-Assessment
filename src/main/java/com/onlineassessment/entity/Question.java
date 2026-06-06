package com.onlineassessment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlineassessment.enums.Difficulty;
import com.onlineassessment.enums.Topic;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Title cannot be empty")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Description cannot be empty")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "sampleInput cannot be blank")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String sampleInput;

    @NotBlank(message = "sampleOutput cannot be blank")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String sampleOutput;

    @NotBlank(message = "Constraints cannot be empty")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String constraints;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Topic topic;

    /*
        What the user sees and edits in the code editor.
        Contains only the Solution class with the method signature.
        Example:
            class Solution {
                public static int[] twoSum(int[] nums, int target) {
                    // write your solution here
                }
            }
    */
    @Column(columnDefinition = "TEXT")
    private String starterCode;

    /*
        Hidden from the user.
        Contains the Main class that:
          1. Reads raw test case input from stdin
          2. Calls the Solution method
          3. Prints the result to stdout
        The judge concatenates: driverCode + userCode → compiles → runs
    */
    @Column(columnDefinition = "TEXT")
    private String driverCode;

    @JsonIgnore
    @ManyToMany(mappedBy = "questions")
    private List<Test> tests;

    @OneToMany(
            mappedBy = "question",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TestCase> testCases;
}