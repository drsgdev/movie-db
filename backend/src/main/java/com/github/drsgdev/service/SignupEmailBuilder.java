package com.github.drsgdev.service;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignupEmailBuilder {

    private final TemplateEngine templateEngine;

    public String build(String body) {
        Context context = new Context();
        context.setVariable("body", body);

        return templateEngine.process("verificationEmail", context);
    }
}
