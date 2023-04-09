package com.example.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;
import com.example.persistence.model.user.UserModel;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Component
public class JwtTokenServiceImp implements JwtTokenService {
    private final Converter<Jwt, Collection<GrantedAuthority>>
            jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;

    private final String tokenIssuer = "self";

    /**
     * {@inheritDoc}
     */
    public String generateToken(Authentication authentication) {
        return buildJwtToken(authentication.getName(), (Collection<GrantedAuthority>) authentication.getAuthorities());
    }

    /**
     * {@inheritDoc}
     */
    public String generateToken(UserModel user) {
        Collection<GrantedAuthority> authorities = new ArrayList<>(1);
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));

        return buildJwtToken(user.getLogin(), authorities);
    }

    private String buildJwtToken(String name, Collection<GrantedAuthority> authorities) {
        String scope = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(tokenIssuer)
                .issuedAt(now)
                .expiresAt(now.plus(15, ChronoUnit.MINUTES))
                .subject(name)
                .claim("scope", scope)
                .build();

        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    /**
     * {@inheritDoc}
     */
    public String extractUsername(String token) throws JwtException {
        return getDecodedToken(token).getSubject();
    }

    /**
     * {@inheritDoc}
     */
    public Collection<GrantedAuthority> extractAuthorities(String token) {
        return jwtGrantedAuthoritiesConverter.convert(getDecodedToken(token));
    }

    /**
     * Decode token and get Jwt object
     */
    private Jwt getDecodedToken(String token) {
        return decoder.decode(token);
    }

    /**
     * {@inheritDoc}
     */
    public boolean validateToken(String token) {
        try {
            Jwt decodedToken = getDecodedToken(token);
            String issuer = decodedToken.getClaim("iss");

            Instant now = Instant.now();
            Instant expiresAt = decodedToken.getClaim("exp");
            boolean isTokenNotExpired = now.isBefore(expiresAt);

            if (issuer.equals(tokenIssuer) && isTokenNotExpired) {
                return true;
            }
        } catch (Exception exception) {
            log.debug(exception);
            // do nothing
        }

        return false;
    }
}
