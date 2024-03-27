package com.mpedroni.aprendatech.domain.feedbacks.usecases;

import com.mpedroni.aprendatech.infra.feedbacks.persistence.FeedbackJpaEntity;
import com.mpedroni.aprendatech.infra.feedbacks.persistence.FeedbackJpaRepository;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;

public class RegisterFeedbackUseCase {
    private final FeedbackJpaRepository feedbackRepository;
    private final UserJpaRepository userRepository;

    public RegisterFeedbackUseCase(FeedbackJpaRepository feedbackRepository, UserJpaRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
    }

    public FeedbackJpaEntity execute(RegisterFeedbackCommand command) {
        var user = userRepository.findByUsername(command.username())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        var feedback = new FeedbackJpaEntity(
                command.courseId(),
                user.getId(),
                command.rating(),
                command.reason().orElse(null)
        );

        return feedbackRepository.save(feedback);
    }
}
