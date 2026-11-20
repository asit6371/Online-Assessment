package com.onlineassessment.config;

import com.onlineassessment.entity.SystemDesignPhase;
import com.onlineassessment.entity.SystemDesignTopic;
import com.onlineassessment.entity.SystemDesignTrack;
import com.onlineassessment.enums.TrackType;
import com.onlineassessment.repository.SystemDesignPhaseRepository;
import com.onlineassessment.repository.SystemDesignTopicRepository;
import com.onlineassessment.repository.SystemDesignTrackRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(2)
public class SystemDesignDataInitializer implements CommandLineRunner {

    private final SystemDesignTrackRepository trackRepository;
    private final SystemDesignPhaseRepository phaseRepository;
    private final SystemDesignTopicRepository topicRepository;

    public SystemDesignDataInitializer(
            SystemDesignTrackRepository trackRepository,
            SystemDesignPhaseRepository phaseRepository,
            SystemDesignTopicRepository topicRepository
    ) {
        this.trackRepository = trackRepository;
        this.phaseRepository = phaseRepository;
        this.topicRepository = topicRepository;
    }

    @Override
    public void run(String... args) {
        if (trackRepository.count() == 0) {
            seedLldTrack();
            seedHldTrack();
            System.out.println("✅ System Design tracks, phases, and topics seeded successfully.");
        } else {
            SystemDesignTrack hld = trackRepository.findByTrackType(TrackType.HLD);
            if (hld != null) {
                List<SystemDesignPhase> hldPhases = phaseRepository.findByTrackIdOrderByOrderIndex(hld.getId());
                if (!hldPhases.isEmpty() && topicRepository.countByPhaseId(hldPhases.get(0).getId()) == 0) {
                    seedHldPhase1Topics(hldPhases.get(0));
                    System.out.println("✅ Seeded HLD Phase 1 topics.");
                }
            }
            System.out.println("✅ System Design tracks already exist.");
        }
    }

    private void seedLldTrack() {
        SystemDesignTrack lld = new SystemDesignTrack();
        lld.setTrackType(TrackType.LLD);
        lld.setName("Track 1: Low-Level Design (LLD)");
        lld.setDescription("Master OOP, design principles, patterns, UML and solve LLD problems.");
        lld.setTotalPhases(8);
        lld.setTotalTopics(36);
        lld.setTotalProblems(60);
        lld.setEstimatedWeeks("6-8 Weeks");
        lld = trackRepository.save(lld);

        // Phase 1: OOP Fundamentals
        createPhase(lld, 1, "Phase 1: OOP Fundamentals",
                "Classes, Objects, Abstraction, Encapsulation, Inheritance, Polymorphism", 1);

        // Phase 2: OOP in Java Deeply
        createPhase(lld, 2, "Phase 2: OOP in Java Deeply",
                "Collections, Exception Handling, Generics, Lambda, Streams, etc.", 2);

        // Phase 3: SOLID Principles
        SystemDesignPhase phase3 = createPhase(lld, 3, "Phase 3: SOLID Principles",
                "SRP, OCP, LSP, ISP, DIP with real-world examples", 3);

        // Phase 4: UML & Class Diagrams
        createPhase(lld, 4, "Phase 4: UML & Class Diagrams",
                "Use case diagrams, class diagrams, sequence diagrams, activity diagrams", 4);

        // Phase 5: Design Patterns
        createPhase(lld, 5, "Phase 5: Design Patterns",
                "Creational, Structural, Behavioral patterns in depth", 5);

        // Phase 6: Schema & API Design
        createPhase(lld, 6, "Phase 6: Schema & API Design",
                "Design database schema and RESTful APIs", 6);

        // Phase 7: Practice Problems
        createPhase(lld, 7, "Phase 7: Practice Problems",
                "Tier 1 -> Tier 2 -> Tier 3 problems", 7);

        // Phase 8: LLD Interview Framework
        createPhase(lld, 8, "Phase 8: LLD Interview Framework",
                "Approach, communication, whiteboarding tips", 8);

        // Seed Sample Topics for Phase 3 (SOLID Principles)
        createSolidOverviewTopic(phase3);
        createSrpTopic(phase3);
        createOcpTopic(phase3);
        createLspTopic(phase3);
        createIspTopic(phase3);
        createDipTopic(phase3);
    }

    private void seedHldTrack() {
        SystemDesignTrack hld = new SystemDesignTrack();
        hld.setTrackType(TrackType.HLD);
        hld.setName("Track 2: High-Level Design (HLD)");
        hld.setDescription("Learn system design fundamentals and design scalable real-world systems.");
        hld.setTotalPhases(8);
        hld.setTotalTopics(32);
        hld.setTotalProblems(40);
        hld.setEstimatedWeeks("4-6 Weeks");
        hld = trackRepository.save(hld);

        SystemDesignPhase phase1 = createPhase(hld, 1, "Phase 1: System Design Fundamentals", "Scalability, Latency, Throughput, CAP Theorem, PACELC", 1);
        createPhase(hld, 2, "Phase 2: Networking & Load Balancing", "DNS, CDN, Reverse Proxy, Load Balancers, L4 vs L7", 2);
        createPhase(hld, 3, "Phase 3: Databases & Storage", "SQL vs NoSQL, Sharding, Replication, Consistency Models", 3);
        createPhase(hld, 4, "Phase 4: Caching Strategies", "Redis, Memcached, Cache-Aside, Write-Through, Write-Back", 4);
        createPhase(hld, 5, "Phase 5: Message Queues & Streaming", "Kafka, RabbitMQ, Event-Driven Architecture", 5);
        createPhase(hld, 6, "Phase 6: Microservices & Distributed Transactions", "Saga Pattern, 2PC, API Gateway, Service Mesh", 6);
        createPhase(hld, 7, "Phase 7: Real-World System Designs", "Design URL Shortener, Uber, Netflix, WhatsApp, Rate Limiter", 7);
        createPhase(hld, 8, "Phase 8: HLD Interview Framework", "Requirements, Back-of-envelope, High-level, Deep dive", 8);

        seedHldPhase1Topics(phase1);
    }

    private void seedHldPhase1Topics(SystemDesignPhase phase1) {
        createHldFundamentalsOverviewTopic(phase1);
        createLatencyThroughputTopic(phase1);
        createCapTheoremTopic(phase1);
        createPacelcTopic(phase1);
    }

    private SystemDesignPhase createPhase(SystemDesignTrack track, int phaseNum, String title, String subtitle, int order) {
        SystemDesignPhase phase = new SystemDesignPhase();
        phase.setTrack(track);
        phase.setPhaseNumber(phaseNum);
        phase.setTitle(title);
        phase.setSubtitle(subtitle);
        phase.setOrderIndex(order);
        return phaseRepository.save(phase);
    }

    private void createSolidOverviewTopic(SystemDesignPhase phase) {
        SystemDesignTopic topic = new SystemDesignTopic();
        topic.setPhase(phase);
        topic.setTitle("SOLID Principles");
        topic.setSubtitle("SOLID is a set of five design principles that make your code more maintainable, scalable, and testable.");
        topic.setOrderIndex(1);

        topic.setOverviewContent("""
                # SOLID Principles Overview
                
                SOLID is an acronym for five design principles for writing understandable, flexible, and maintainable software:
                
                1. **S** - Single Responsibility Principle (SRP)
                2. **O** - Open/Closed Principle (OCP)
                3. **L** - Liskov Substitution Principle (LSP)
                4. **I** - Interface Segregation Principle (ISP)
                5. **D** - Dependency Inversion Principle (DIP)
                
                ### Why is it important?
                Following SOLID principles helps in reducing code complexity, improving reusability, minimizing regressions when adding new features, and making the system easier to extend and maintain over time.
                """);

        topic.setTheoryContent("""
                # SOLID Principles Theory
                
                The SOLID concepts were introduced by Robert C. Martin (Uncle Bob) in his 2000 paper *Design Principles and Design Patterns*.
                
                ### Key Objectives:
                - **High Cohesion**: Elements that belong together should be grouped together.
                - **Low Coupling**: Dependencies between modules should be minimal and abstract.
                - **Extensibility**: Adding new functionality without modifying stable existing code.
                - **Testability**: Enabling easy unit testing with mocks and stubs.
                """);

        topic.setExamplesContent("""
                ```java
                // Example of Dependency Inversion Principle (DIP)
                
                // 1. Abstraction
                public interface NotificationService {
                    void send(String message);
                }
                
                // 2. Low-level Module
                public class EmailService implements NotificationService {
                    @Override
                    public void send(String message) {
                        System.out.println("Email sent: " + message);
                    }
                }
                
                // 3. High-level Module depending on abstraction
                public class NotificationManager {
                    private final NotificationService service;
                    
                    public NotificationManager(NotificationService service) {
                        this.service = service;
                    }
                    
                    public void notifyUser(String msg) {
                        service.send(msg);
                    }
                }
                ```
                """);

        topic.setDiagramsContent("""
                ```mermaid
                classDiagram
                    class NotificationService {
                        <<interface>>
                        +send(message: String) void
                    }
                    class EmailService {
                        +send(message: String) void
                    }
                    class SmsService {
                        +send(message: String) void
                    }
                    class NotificationManager {
                        -NotificationService service
                        +notifyUser(msg: String) void
                    }
                    NotificationService <|.. EmailService
                    NotificationService <|.. SmsService
                    NotificationManager --> NotificationService
                ```
                """);

        topic.setInterviewQaContent("""
                ### Frequently Asked Interview Questions
                
                **Q1: What is the main difference between Open/Closed Principle and Dependency Inversion Principle?**
                *Answer:* OCP focuses on making classes open for extension but closed for modification (usually via inheritance or polymorphism). DIP focuses on decoupling high-level modules from low-level modules by having both depend on abstractions (interfaces).
                
                **Q2: How does violating Liskov Substitution Principle break polymorphism?**
                *Answer:* If a subclass throws an `UnsupportedOperationException` or alters the expected invariant/precondition of a parent class method, client code expecting the parent class will fail at runtime when given the subclass instance.
                """);

        topic.setPracticeContent("""
                ### Hands-on Practice Problem
                
                **Problem: Refactor a God Invoice System**
                Given a monolithic `Invoice` class that calculates totals, saves directly to MySQL via JDBC, and sends an SMTP email:
                
                1. Split the class to follow the Single Responsibility Principle.
                2. Introduce repository and notification interfaces to follow DIP.
                3. Add support for PDF export without modifying the core invoice logic to satisfy OCP.
                """);

        topicRepository.save(topic);
    }

    private void createSrpTopic(SystemDesignPhase phase) {
        createSimpleTopic(phase, "SRP - Single Responsibility", "A class should have one, and only one, reason to change.", 2);
    }

    private void createOcpTopic(SystemDesignPhase phase) {
        createSimpleTopic(phase, "OCP - Open/Closed", "Software entities should be open for extension, but closed for modification.", 3);
    }

    private void createLspTopic(SystemDesignPhase phase) {
        createSimpleTopic(phase, "LSP - Liskov Substitution", "Subtypes must be substitutable for their base types without altering correctness.", 4);
    }

    private void createIspTopic(SystemDesignPhase phase) {
        createSimpleTopic(phase, "ISP - Interface Segregation", "Clients should not be forced to depend on methods they do not use.", 5);
    }

    private void createDipTopic(SystemDesignPhase phase) {
        createSimpleTopic(phase, "DIP - Dependency Inversion", "High-level modules should not depend on low-level modules; both should depend on abstractions.", 6);
    }

    private void createSimpleTopic(SystemDesignPhase phase, String title, String subtitle, int order) {
        SystemDesignTopic topic = new SystemDesignTopic();
        topic.setPhase(phase);
        topic.setTitle(title);
        topic.setSubtitle(subtitle);
        topic.setOrderIndex(order);
        topic.setOverviewContent("# " + title + "\n\n" + subtitle);
        topic.setTheoryContent("# " + title + " In-Depth Theory\n\nDetailed principles and best practices.");
        topic.setExamplesContent("```java\n// Implementation example for " + title + "\n```");
        topic.setDiagramsContent("# Architectural & UML Diagrams for " + title);
        topic.setInterviewQaContent("### Interview Q&A\n\nTop questions and answers for " + title);
        topic.setPracticeContent("### Coding Challenge\n\nPractice exercise for " + title);
        topicRepository.save(topic);
    }

    private void createHldFundamentalsOverviewTopic(SystemDesignPhase phase) {
        SystemDesignTopic topic = new SystemDesignTopic();
        topic.setPhase(phase);
        topic.setTitle("Scalability & System Characteristics");
        topic.setSubtitle("Understanding vertical vs horizontal scaling, bottlenecks, throughput, and system architecture fundamentals.");
        topic.setOrderIndex(1);

        topic.setOverviewContent("""
                # Scalability & System Characteristics Overview

                Scalability is the ability of a system, network, or process to handle a growing amount of work in a capable manner or its potential to be enlarged to accommodate that growth.

                ### Core Dimensions:
                1. **Horizontal Scaling (Scaling Out)**: Adding more nodes/machines to the system pool.
                2. **Vertical Scaling (Scaling Up)**: Adding more power (CPU, RAM, SSD) to an existing machine.
                3. **Stateless vs Stateful Design**: Decoupling session state to enable seamless horizontal elasticity.
                """);

        topic.setTheoryContent("""
                # Deep Dive: Scalability Principles

                ### Vertical vs Horizontal Scaling Trade-offs:
                - **Vertical Scaling**: Simple, no distributed coordination needed, but hits hard hardware limits and single point of failure (SPOF).
                - **Horizontal Scaling**: Highly resilient, linear capacity growth, but introduces network partition risks, distributed synchronization complexity, and eventual consistency challenges.

                ### Key Scalability Bottlenecks:
                - Database connection pools
                - Disk I/O saturation
                - Network bandwidth and serialization overhead
                - Global distributed locks
                """);

        topic.setExamplesContent("""
                ```java
                // Example: Stateless Service Design with Distributed Token Store
                @Service
                public class UserSessionValidator {
                    private final RedisTemplate<String, UserSession> redisTemplate;

                    public UserSessionValidator(RedisTemplate<String, UserSession> redisTemplate) {
                        this.redisTemplate = redisTemplate;
                    }

                    public boolean isValidSession(String sessionToken) {
                        return redisTemplate.hasKey("session:" + sessionToken);
                    }
                }
                ```
                """);

        topic.setDiagramsContent("""
                ```mermaid
                graph TD
                    Client[Client App] --> LB[Load Balancer]
                    LB --> Web1[Web Node 1]
                    LB --> Web2[Web Node 2]
                    LB --> Web3[Web Node 3]
                    Web1 --> Redis[(Redis Shared Cache)]
                    Web2 --> Redis
                    Web3 --> Redis
                    Web1 --> DB[(Primary Database)]
                    Web2 --> DB
                    Web3 --> DB
                ```
                """);

        topic.setInterviewQaContent("""
                ### Interview Q&A
                **Q1: How do you design a system to scale from 1,000 to 10 million daily active users?**
                *Answer:* Start monolithic with a single DB. Step 1: Move DB to separate server. Step 2: Add caching (Redis) for read-heavy queries. Step 3: Add load balancer and multiple stateless app servers. Step 4: Add Read Replicas. Step 5: Database sharding and message queues (Kafka) for async processing. Step 6: Global CDN for static assets.
                """);

        topic.setPracticeContent("""
                ### Architecture Challenge
                **Design a Scalable Counter Service:**
                Design a counter service that can record 100,000 likes/views per second without saturating a single database row.
                """);

        topicRepository.save(topic);
    }

    private void createLatencyThroughputTopic(SystemDesignPhase phase) {
        createSimpleTopic(phase, "Latency vs Throughput", "Optimizing response times, p95/p99 percentiles, and request capacity.", 2);
    }

    private void createCapTheoremTopic(SystemDesignPhase phase) {
        createSimpleTopic(phase, "CAP Theorem", "Consistency, Availability, and Partition Tolerance tradeoffs in distributed systems.", 3);
    }

    private void createPacelcTopic(SystemDesignPhase phase) {
        createSimpleTopic(phase, "PACELC Theorem", "Extending CAP theorem with latency and consistency trade-offs in normal operation.", 4);
    }
}
