package org.co.taplink.qrbarcode.entity;

import jakarta.persistence.*;
import lombok.*;
import org.co.taplink.configs.BaseEntity;
import org.co.taplink.links.entities.UserLinks;
import org.co.taplink.qrbarcode.enums.CodeType;
import org.co.taplink.qrbarcode.enums.ErrorCorrectionLevel;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "qr_barcode_configs")
public class QrBarcodeConfig extends BaseEntity {

    @Id
    private Long id;

    @MapsId
    @JoinColumn(name = "user_links_id")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private UserLinks userLinks;

    @Enumerated(EnumType.STRING)
    @Column(name = "code_type", nullable = false, length = 20)
    private CodeType codeType;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "foreground_color", length = 7, nullable = false)
    @Builder.Default
    private String foregroundColor = "#000000";

    @Column(name = "background_color", length = 7, nullable = false)
    @Builder.Default
    private String backgroundColor = "#FFFFFF";

    @Column(name = "size_pixels")
    @Builder.Default
    private Integer size = 300;

    @Column(name = "margin_size")
    @Builder.Default
    private Integer margin = 1;

    @Column(name = "include_logo", nullable = false)
    @Builder.Default
    private boolean includeLogo = false;

    @Column(name = "logo_url", length = 512)
    private String logoUrl;

    @Column(name = "logo_scale")
    private Double logoScale;

    @Enumerated(EnumType.STRING)
    @Column(name = "error_correction_level", length = 10)
    @Builder.Default
    private ErrorCorrectionLevel errorCorrection = ErrorCorrectionLevel.MEDIUM;
}