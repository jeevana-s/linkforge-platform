package com.linkforge.service;

import com.linkforge.cache.UrlCache;
import com.linkforge.entity.Url;
import com.linkforge.exception.ApiException;
import com.linkforge.kafka.ClickEventDto;
import com.linkforge.kafka.ClickEventProducer;
import com.linkforge.repository.UrlRepository;
import com.linkforge.util.UserAgentParser;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RedirectService {

    private final UrlRepository repo;
    private final UrlCache cache;
    private final ClickEventProducer producer;
    private final PasswordEncoder enc;

    public RedirectService(
            UrlRepository r,
            UrlCache c,
            ClickEventProducer p,
            PasswordEncoder e
    ) {
        this.repo = r;
        this.cache = c;
        this.producer = p;
        this.enc = e;
    }

    /**
     * Fast resolve:
     * returns original URL or null.
     */
    public String resolveFast(String code) {
        return cache.get(code);
    }

    public Url load(String code) {

        return repo.findByShortCode(code)
                .orElseThrow(() ->
                        new ApiException(
                                HttpStatus.NOT_FOUND,
                                "Link not found"
                        )
                );
    }

    public String resolveAndTrack(
            String code,
            String password,
            HttpServletRequest req
    ) {

        Url u = load(code);

        // BLOCKED
        if (u.isBlocked()) {

            throw new ApiException(
                    HttpStatus.GONE,
                    "Link blocked"
            );
        }

        // EXPIRED
        if (
                u.getExpiresAt() != null &&
                Instant.now().isAfter(u.getExpiresAt())
        ) {

            throw new ApiException(
                    HttpStatus.GONE,
                    "Link expired"
            );
        }

        // ONE-TIME ALREADY USED
        if (u.isOneTime() && u.isUsed()) {

            throw new ApiException(
                    HttpStatus.GONE,
                    "Link already used"
            );
        }

        // PASSWORD CHECK
        if (u.getPasswordHash() != null) {

            if (
                    password == null ||
                    !enc.matches(password, u.getPasswordHash())
            ) {

                throw new ApiException(
                        HttpStatus.UNAUTHORIZED,
                        "Password required"
                );
            }
        }

        // ONE-TIME LINK
        if (u.isOneTime()) {

            u.setUsed(true);

            repo.save(u);

            cache.evict(code);
        }
        else if (u.getPasswordHash() == null) {

            cache.put(code, u.getOriginalUrl());
        }

        // INCREMENT CLICK COUNT
        u.setClickCount(u.getClickCount() + 1);

        repo.save(u);

        // ANALYTICS EVENT
        String ua = req.getHeader("User-Agent");

        String ip = req.getHeader("X-Forwarded-For");

        if (ip == null) {
            ip = req.getRemoteAddr();
        }

        producer.publish(
                new ClickEventDto(
                        u.getId(),
                        ip,
                        null,
                        UserAgentParser.browser(ua),
                        UserAgentParser.device(ua),
                        req.getHeader("Referer")
                )
        );

        return u.getOriginalUrl();
    }
}