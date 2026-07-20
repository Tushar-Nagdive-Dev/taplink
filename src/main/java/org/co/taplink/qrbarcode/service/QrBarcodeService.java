package org.co.taplink.qrbarcode.service;

import org.co.taplink.links.entities.UserLinks;
import org.co.taplink.qrbarcode.entity.QrBarcodeConfig;

public interface QrBarcodeService {

    QrBarcodeConfig saveOrUpdateConfig(UserLinks link, QrBarcodeConfig newConfig);

    byte[] generateCodeImage(String targetUrl, QrBarcodeConfig config);

    QrBarcodeConfig getConfig(Long linkId);
}
