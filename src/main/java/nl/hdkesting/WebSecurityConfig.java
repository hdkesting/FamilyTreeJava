package nl.hdkesting;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String ADMIN_ROLE = "Admin";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/admin/**").hasRole(ADMIN_ROLE)
                    .antMatchers(HttpMethod.GET, "/admin/**").hasRole(ADMIN_ROLE)
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                .and()
                    .logout()
                    .permitAll()
                .and()
                    .csrf().disable(); // seems harsh, isn't there a better way to allow POSTs?
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        // https://stackoverflow.com/questions/49847791/java-spring-security-user-withdefaultpasswordencoder-is-deprecated
        List<UserDetails> users = new ArrayList<>();
        users.add(User.withDefaultPasswordEncoder() // deprecated because of clear text credentials but apparently fine for demo code like this
                        .username("admin")
                        .password("Pa$$w0rd")
                        .roles(ADMIN_ROLE)
                        .build());

        return new InMemoryUserDetailsManager(users);
    }
}
