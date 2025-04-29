package configuration;


import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = DemoMockSecurityContext.class)
public @interface WithMockCustomUser {

    String email() default "test@test.com";
    String password() default "testPassword";
    String memberName() default "testName";
    String[] roles() default {"ADMIN"};
}
