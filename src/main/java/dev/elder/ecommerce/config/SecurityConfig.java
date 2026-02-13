package dev.elder.ecommerce.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration // define Beans de configuração. Equivale a um “arquivo de configuração Java”.
@EnableWebSecurity // Ativa o Spring Security para a aplicação web, Sem isso, o SecurityFilterChain não funciona
@EnableMethodSecurity // Permite segurança em nível de metodo, exemplo: para deletar algo tem que ser ROLE_ADMIN
public class SecurityConfig {

    private RSAPublicKey publicKey; // Public Key → valida o token (quem consome). Quem valida usa apenas a chave pública

    private RSAPrivateKey privateKey; // Private Key → assina o token (quem emite). Quem gera o token usa a chave privada

    public SecurityConfig(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    // Esse metodo define TODA a política de segurança da API.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll() // acesso livre (login gera o token)
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll() // acesso livre (criar usuario)
                        .requestMatchers(
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable()) // API é stateless, CSRF é desnecessário

                // Essa API é um OAuth2 Resource Server que valida JWT. O Spring automaticamente extrai o token do header, Valida: assinatura, expiração e integridade e Cria um JwtAuthenticationToken no SecurityContext.
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                // Stateless, nenhuma sessão HTTP é criada, cada request precisa do JWT.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Finaliza e registra o filtro de segurança.
        return http.build();
    }

    // Gerar o token
    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    // Validar o token, verifica a assinatura do JWT, usa somente a chave pública
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    // criptografar senha ao salvar no banco
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
