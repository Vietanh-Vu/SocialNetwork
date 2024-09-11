package com.example.socialnetwork;

import com.example.socialnetwork.infrastructure.entity.PostReaction;
import com.example.socialnetwork.infrastructure.repository.PostReactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class PostReactionRepositoryTest {
    @Autowired
    private PostReactionRepository postReactionRepository;

    @Test
    public void testOptimisticLock() {
        // Tạo một PostReaction và lưu vào database
        PostReaction reaction = new PostReaction();
        reaction.setReactionType("like");
        postReactionRepository.save(reaction);

        // Lấy ra cùng một PostReaction ở 2 transaction khác nhau
        PostReaction reaction1 = postReactionRepository.findById(reaction.getId()).orElseThrow();
        PostReaction reaction2 = postReactionRepository.findById(reaction.getId()).orElseThrow();

        // Thay đổi dữ liệu của reaction1 và lưu
        reaction1.setReactionType("dislike");
        postReactionRepository.save(reaction1);

        // Thử thay đổi và lưu reaction2, lúc này phiên bản sẽ không khớp
        reaction2.setReactionType("love");

        Assertions.assertThrows(OptimisticLockingFailureException.class, () -> {
            postReactionRepository.save(reaction2);
        });
    }
}
