package nl.hdkesting;

import nl.hdkesting.familyTree.core.auth.FamilyTreeRole;
import nl.hdkesting.familyTree.core.auth.FamilyTreeUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private FamilyTreeUserDetailsService userDetailsService;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(this.userDetailsService);
        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance()); // only for demo purposes

        return provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(this.authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // apparently "hasRole" adds "ROLE_" to the supplied name!!
        http
                .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/admin/**").hasRole(FamilyTreeRole.ADMIN.toString())
                    .antMatchers(HttpMethod.GET, "/admin/**").hasRole(FamilyTreeRole.ADMIN.toString())
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
}
