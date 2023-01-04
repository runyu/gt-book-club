package gov.tech.gtbookclub.config;

import gov.tech.gtbookclub.Constant.AppConstant;
import gov.tech.gtbookclub.model.entity.User;
import gov.tech.gtbookclub.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ApplicationStartupRunner implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        Optional<User> userOptional = userRepository.findByEmail("admin@email.com");

        if (!userOptional.isPresent()) {
            if (!userOptional.get().getRole().equals(AppConstant.ROLE_ADMIN)) {
                User user = User.builder()
                        .name(AppConstant.DEFAULT_ADMIN)
                        .email(AppConstant.DEFAULT_ADMIN_EMAIL)
                        .password(AppConstant.DEFAULT_ADMIN_PASSWORD)
                        .role(AppConstant.ROLE_ADMIN)
                        .createdAt(new Date())
                        .build();
                userRepository.save(user);
            }
        }
    }
}