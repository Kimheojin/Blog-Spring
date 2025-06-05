package HeoJin.demoBlog.configuration.mockUser;


import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = DemoMockSecurityContext.class)
public @interface WithMockCustomUser {

    // 커스텀 annotation
    String email() default "test@test.com";
    String password() default "testPassword";
    String memberName() default "testName";
    String[] roles() default {"ADMIN"};
}
