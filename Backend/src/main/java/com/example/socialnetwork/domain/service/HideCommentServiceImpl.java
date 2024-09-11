package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.domain.model.CommentDomain;
import com.example.socialnetwork.domain.port.spi.CommentDatabasePort;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.pmml4s.model.Model;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class HideCommentServiceImpl {
    private final CommentDatabasePort commentDatabasePort;
    private Model model;
    private static final double SPAM_THRESHOLD = 0.6;
    private boolean isModelLoaded = false;

    @PostConstruct
    public void init()  {
        try {
            ClassPathResource resource = new ClassPathResource("model/comment-model.pmml");
            model = Model.fromInputStream(resource.getInputStream());
            isModelLoaded = true;
        } catch (Exception e) {
            isModelLoaded = false;
        }
    }

    private boolean isSpam(CommentDomain comment) {
        if (!isModelLoaded) {
            throw new RuntimeException("Failed to load model");
        }
        Map<String, Object> input = new HashMap<>();
        input.put("free_text", comment.getContent());

        Map<?, ?> results = model.predict(input);
        double spamProbability = (double) results.get("probability(1)");
        System.out.println(comment.getContent() + " " + spamProbability);
        return spamProbability > SPAM_THRESHOLD;
    }
}
