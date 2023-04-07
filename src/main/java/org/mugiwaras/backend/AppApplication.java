package org.mugiwaras.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.crypto.bcrypt.BCrypt;

@SpringBootApplication
@Slf4j
public class AppApplication extends SpringBootServletInitializer implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("************** ADMIN PASS ***************");
        System.out.println(BCrypt.hashpw("admin", BCrypt.gensalt()));
        System.out.println("************** USER PASS ***************");
        System.out.println(BCrypt.hashpw("user", BCrypt.gensalt()));
    }
}
