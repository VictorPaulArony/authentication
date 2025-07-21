package com.example.authentication.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "institution")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "schoolnamne", nullable = false, length = 255)
    private String schoolnamne;

    @Column(name = "registraition_number", nullable = false, length = 255)
    private String registraitionNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "school_type", nullable = false)
    private SchoolType schoolType;

    @Enumerated(EnumType.STRING)
    @Column(name = "education_system", nullable = false)
    private EducationSystem educationSystem;

    @Column(nullable = false, length = 255)
    private String location;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, unique = true, length = 20)
    private String phone;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "postal_address", length = 255)
    private String postalAddress;

    @Column(length = 255)
    private String website;

    @Column(length = 255)
    private String logo;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "principal_name", length = 255)
    private String principalName;

    @Column(name = "principal_email", unique = true, length = 255)
    private String principalEmail;

    @Column(name = "principal_phone", unique = true, length = 20)
    private String principalPhone;

    @Column(name = "established_year")
    private Integer establishedYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "accreditation_status")
    @Builder.Default
    private AccreditationStatus accreditationStatus = AccreditationStatus.PENDING;

    @Column(name = "accreditation_body", length = 255)
    private String accreditationBody;

    @Column(name = "accreditation_date")
    private LocalDateTime accreditationDate;

    // Link to Teacher entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_institution_teacher"), 
                referencedColumnName = "id", nullable = true,
                columnDefinition = "FOREIGN KEY (created_by) REFERENCES teacher(id) ON DELETE SET NULL")
    private Teacher createdBy;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    // Enums
    public enum SchoolType {
        PRIMARY, SECONDARY, TERTIARY, COLLAGE, UNIVERSITY
    }

    public enum EducationSystem {
        CBC, _8_4_4, IGCSE, IB, OTHER
    }

    public enum AccreditationStatus {
        ACCREDITED, PENDING, NOT_ACCREDITED
    }
}
