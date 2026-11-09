package org.raj.ai;

import java.time.Duration;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringAiDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiDemoApplication.class, args);
    }


	@Bean
	CommandLineRunner askLlm(ChatClient.Builder chatClientBuilder) {
        return args -> {
            ChatClient chatClient = chatClientBuilder.build();

            ChatResponse response = chatClient.prompt()
                    .user("What is the latest news AI engineering , please give a short answer")
                    .call()
                    .chatResponse();

            // Claude returns a (empty) thinking block before the answer, and Spring AI maps
            // each block to its own Generation - so .content() would only see the empty first one.
            String answer = response.getResults().stream()
                    .map(g -> g.getOutput().getText())
                    .filter(text -> text != null && !text.isBlank())
                    .collect(Collectors.joining("\n"));

            System.out.println(answer);
        };
    }
}


