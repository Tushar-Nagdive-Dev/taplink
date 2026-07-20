package org.co.taplink.qrbarcode.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.co.taplink.links.entities.UserLinks;
import org.co.taplink.qrbarcode.entity.QrBarcodeConfig;
import org.co.taplink.qrbarcode.enums.CodeType;
import org.co.taplink.qrbarcode.repository.QrBarcodeConfigRepository;
import org.co.taplink.qrbarcode.service.QrBarcodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

import static org.co.taplink.utils.TapLinkAppMessages.QrBarcodes.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class QrBarcodeServiceImpl implements QrBarcodeService {

    private final QrBarcodeConfigRepository configRepository;

    @Override
    @Transactional
    public QrBarcodeConfig saveOrUpdateConfig(UserLinks link, QrBarcodeConfig newConfig) {
        newConfig.setUserLinks(link);
        return configRepository.save(newConfig);
    }

    @Override
    public QrBarcodeConfig getConfig(Long linkId) {
        return configRepository.findById(linkId)
                .orElseGet(() -> QrBarcodeConfig.builder()
                        .codeType(CodeType.QR_CODE) // Safe default
                        .build());
    }

    @Override
    public byte[] generateCodeImage(String targetUrl, QrBarcodeConfig config) {
        if (!config.getIsActive()) {
            throw new IllegalStateException(QA_BARCODE_DISABLED);
        }

        try {
            return switch (config.getCodeType()) {
                case QR_CODE -> generateQrCode(targetUrl, config);
                case BARCODE_128, BARCODE_39, EAN_13, UPC_A -> generateBarcode(targetUrl, config);
            };
        } catch (WriterException | IOException e) {
            log.error(FAILED_QA_BARCODE_GENERATION, targetUrl, e);
            throw new RuntimeException("Error generating image", e);
        }
    }

    private byte[] generateQrCode(String data, QrBarcodeConfig config) throws WriterException, IOException {
        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.MARGIN, config.getMargin());

        boolean shouldApplyLogo = config.isIncludeLogo() && config.getLogoUrl() != null && !config.getLogoUrl().trim().isEmpty();

        com.google.zxing.qrcode.decoder.ErrorCorrectionLevel ecLevel = shouldApplyLogo ?
                com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.H : mapErrorCorrection(config.getErrorCorrection());

        hints.put(EncodeHintType.ERROR_CORRECTION, ecLevel);

        BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, config.getSize(), config.getSize(), hints);

        int onColor = Color.decode(config.getForegroundColor()).getRGB();
        int offColor = Color.decode(config.getBackgroundColor()).getRGB();
        MatrixToImageConfig colorConfig = new MatrixToImageConfig(onColor, offColor);

        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, colorConfig);

        if (shouldApplyLogo) {
            try {
                qrImage = overlayLogo(qrImage, config);
            } catch (Exception e) {
                log.error(FAILED_LOGO_LOAD, config.getLogoUrl(), e);
            }
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(qrImage, "PNG", baos);
            return baos.toByteArray();
        }
    }

    private byte[] generateBarcode(String data, QrBarcodeConfig config) throws WriterException, IOException {
        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.MARGIN, config.getMargin());

        BarcodeFormat format = mapBarcodeFormat(config.getCodeType());
        int width = config.getSize();
        int height = Math.max(50, width / 3);

        BitMatrix bitMatrix = new MultiFormatWriter().encode(data, format, width, height, hints);

        int onColor = Color.decode(config.getForegroundColor()).getRGB();
        int offColor = Color.decode(config.getBackgroundColor()).getRGB();
        MatrixToImageConfig colorConfig = new MatrixToImageConfig(onColor, offColor);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos, colorConfig);
            return baos.toByteArray();
        }
    }

    private BufferedImage overlayLogo(BufferedImage qrImage, QrBarcodeConfig config) throws IOException {
        BufferedImage logo = ImageIO.read(new URL(config.getLogoUrl()));

        double scale = (config.getLogoScale() != null) ? config.getLogoScale() : 0.20;
        int logoWidth = (int) (qrImage.getWidth() * scale);
        int logoHeight = (int) (qrImage.getHeight() * scale);

        int x = (qrImage.getWidth() - logoWidth) / 2;
        int y = (qrImage.getHeight() - logoHeight) / 2;

        Graphics2D g = qrImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(logo, x, y, logoWidth, logoHeight, null);
        g.dispose();

        return qrImage;
    }

    private com.google.zxing.qrcode.decoder.ErrorCorrectionLevel mapErrorCorrection(org.co.taplink.qrbarcode.enums.ErrorCorrectionLevel internalLevel) {
        if (internalLevel == null) return com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.M;
        return switch (internalLevel) {
            case LOW -> com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.L;
            case MEDIUM -> com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.M;
            case QUARTILE -> com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.Q;
            case HIGH -> com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.H;
        };
    }

    private BarcodeFormat mapBarcodeFormat(CodeType codeType) {
        return switch (codeType) {
            case QR_CODE -> BarcodeFormat.QR_CODE;
            case BARCODE_128 -> BarcodeFormat.CODE_128;
            case BARCODE_39 -> BarcodeFormat.CODE_39;
            case EAN_13 -> BarcodeFormat.EAN_13;
            case UPC_A -> BarcodeFormat.UPC_A;
        };
    }
}