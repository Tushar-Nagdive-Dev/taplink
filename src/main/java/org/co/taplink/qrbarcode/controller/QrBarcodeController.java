package org.co.taplink.qrbarcode.controller;

import lombok.RequiredArgsConstructor;
import org.co.taplink.links.entities.LinkRouting;
import org.co.taplink.links.entities.UserLinks;
import org.co.taplink.links.repository.LinkRoutingRepository;
import org.co.taplink.links.repository.UserLinkRepository;
import org.co.taplink.qrbarcode.entity.QrBarcodeConfig;
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
    private final UserLinkRepository userLinkRepository;

    // Injected to access the dynamically generated short codes
    private final LinkRoutingRepository linkRoutingRepository;

    @GetMapping(value = "/{linkId}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQrORBarcodeImage(@PathVariable("linkId") Long linkId) {

        // 1. Get the QR/Barcode visual configuration
        QrBarcodeConfig config = this.qrBarcodeService.getConfig(linkId);

        // 2. Fetch the routing details to get the actual short code
        LinkRouting routing = this.linkRoutingRepository.findById(linkId)
                .orElseThrow(() -> new RuntimeException(LINK_NOT_FOUND + linkId));

        // 3. Build the dynamic live URL using the actual generated short code
        String dynamicUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/{shortCode}")
                .buildAndExpand(routing.getShortCode())
                .toUriString();

        // 4. Generate the image bytes
        byte[] image = this.qrBarcodeService.generateCodeImage(dynamicUrl, config);

        return ResponseEntity.ok(image);
    }

    /**
     * Endpoint to update the visual configurations (colors, logo, size)
     */
    @PutMapping("/{linkId}/qr")
    public ResponseEntity<QrBarcodeConfig> updateQrConfig(
            @PathVariable("linkId") Long linkId,
            @RequestBody QrBarcodeConfig updatedConfig) {

        UserLinks link = this.userLinkRepository.findById(linkId)
                .orElseThrow(() -> new RuntimeException(LINK_NOT_FOUND + linkId));

        QrBarcodeConfig savedConfig = qrBarcodeService.saveOrUpdateConfig(link, updatedConfig);
        return ResponseEntity.ok(savedConfig);
    }
}