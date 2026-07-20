package org.co.taplink.links.services.impl;

import lombok.RequiredArgsConstructor;
import org.co.taplink.links.repository.LinkRoutingRepository;
import org.co.taplink.links.services.ShortCodeGeneratorService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

import static org.co.taplink.utils.TapLinkAppConstants.ALLOWED_CHARACTERS;
import static org.co.taplink.utils.TapLinkAppConstants.DEFAULT_CODE_LENGTH;

@Service
@RequiredArgsConstructor
public class ShortCodeGeneratorServiceImpl implements ShortCodeGeneratorService {

    private final LinkRoutingRepository linkRoutingRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String generateUniqueShortCode() {
        String code;
        do {
            code = generateRandomString(DEFAULT_CODE_LENGTH);
        }while (this.linkRoutingRepository.existsByShortCode(code));
        return code;
    }

    private String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(ALLOWED_CHARACTERS.length());
            sb.append(ALLOWED_CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }
}
