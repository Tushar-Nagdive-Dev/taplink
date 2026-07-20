package org.co.taplink.qrbarcode.repository;

import lombok.NonNull;
import org.co.taplink.qrbarcode.entity.QrBarcodeConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QrBarcodeConfigRepository extends JpaRepository<@NonNull QrBarcodeConfig,@NonNull Long> {
}
