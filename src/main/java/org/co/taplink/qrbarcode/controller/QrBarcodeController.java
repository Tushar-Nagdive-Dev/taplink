package org.co.taplink.qrbarcode.controller;

import lombok.RequiredArgsConstructor;
import org.co.taplink.links.entities.UserLinks;
import org.co.taplink.links.repository.UserLinkRepository;
import org.co.taplink.qrbarcode.entity.QrBarcodeConfig;
import org.co.taplink.qrbarcode.repository.QrBarcodeConfigRepository;
import org.co.taplink.qrbarcode.service.QrBarcodeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.co.taplink.utils.TapLinkAppConstants.API_PATHS.QR_BARCODE_PATH;
import static org.co.taplink.utils.TapLinkAppMessages.QrBarcodes.LINK_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(QR_BARCODE_PATH)
public class QrBarcodeController {

    private final QrBarcodeService qrBarcodeService;

    private final QrBarcodeConfigRepository qrBarcodeConfigRepository;

    private final UserLinkRepository userLinkRepository;

    @GetMapping(value = "/{linkId}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQrORBarcodeImage(@PathVariable("linkId") Long linkId) {
        QrBarcodeConfig config = this.qrBarcodeService.getConfig(linkId);
        UserLinks userLinks = this.qrBarcodeConfigRepository.findById(linkId)
                .orElseThrow(() -> new RuntimeException(LINK_NOT_FOUND + linkId)).getUserLinks();
        /* * TODO: Uncomment when ShortCodeGeneratorService is implemented
         *
         * String dynamicUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
         * .path("/{shortCode}")
         * .buildAndExpand(link.getShortCode())
         * .toUriString();
         */
        String dummyUrl = "https://tap.link/placeholder-" + linkId;

        byte[] image = this.qrBarcodeService.generateCodeImage(dummyUrl, config);

        return ResponseEntity.ok(image);
    }

    /**
     * Endpoint to update the visual configurations (colors, logo, size)
     */
    @PutMapping("/{linkId}/qr")
    public ResponseEntity<QrBarcodeConfig> updateQrConfig(
            @PathVariable Long linkId,
            @RequestBody QrBarcodeConfig updatedConfig) {

        UserLinks link = this.userLinkRepository.findById(linkId)
                .orElseThrow(() -> new RuntimeException("Link not found with ID: " + linkId));

        QrBarcodeConfig savedConfig = qrBarcodeService.saveOrUpdateConfig(link, updatedConfig);
        return ResponseEntity.ok(savedConfig);
    }
}
