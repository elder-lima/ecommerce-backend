package dev.elder.ecommerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration // Diz ao Spring: Essa classe contém definições de beans carregue ela na inicialização da aplicação.
public class RsaKeyConfig {

    @Value("classpath:private.key")
    private org.springframework.core.io.Resource privateKeyResource;

    @Value("classpath:public.key")
    private org.springframework.core.io.Resource publicKeyResource;

    @Bean
    public RSAPrivateKey privateKey() throws Exception {

        // Abre o arquivo .pem, lê todos os bytes e converte para String.
        String key = new String(privateKeyResource.getInputStream().readAllBytes());

        // Remove o cabeçalho e o rodapé PEM, remove espaços, tabs e quebras de linha
        // Resultado apenas o Base64 puro
        key = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        // Converte o texto Base64 em bytes binários
        byte[] decoded = java.util.Base64.getDecoder().decode(key);

        //PKCS#8 → padrão para chaves privadas
        var keySpec = new java.security.spec.PKCS8EncodedKeySpec(decoded);

        // Criando a chave RSA:
        var keyFactory = java.security.KeyFactory.getInstance("RSA");

        //Retorna o bean para o Spring
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    @Bean
    public RSAPublicKey publicKey() throws Exception {

        // Lê o public_key.pem.
        String key = new String(publicKeyResource.getInputStream().readAllBytes());

        // Remove cabeçalho, rodapé e espaços.
        key = key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        //Decodificação Base64
        byte[] decoded = java.util.Base64.getDecoder().decode(key);

        // X.509 → padrão para chaves públicas
        var keySpec = new java.security.spec.X509EncodedKeySpec(decoded);

        //Gerando a chave pública
        var keyFactory = java.security.KeyFactory.getInstance("RSA");

        //Retorna o bean para o Spring
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }
}
