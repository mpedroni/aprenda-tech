package com.mpedroni.aprendatech.infra.config;

import com.mpedroni.aprendatech.domain.feedbacks.usecases.RegisterFeedbackUseCase;
import com.mpedroni.aprendatech.infra.feedbacks.persistence.FeedbackJpaRepository;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeedbacksConfig {
    private final FeedbackJpaRepository feedbackRepository;
    private final UserJpaRepository userRepository;

    public FeedbacksConfig(FeedbackJpaRepository feedbackRepository, UserJpaRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
    }

    @Bean
    public RegisterFeedbackUseCase registerFeedbackUseCase() {
        return new RegisterFeedbackUseCase(feedbackRepository, userRepository);
    }
}
