package com.uchk.university.dto;

import com.uchk.university.entity.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto {
    private Long id;
    
    @NotBlank(message = "Le titre ne peut pas être vide")
    @Size(max = 255, message = "Le titre ne peut pas dépasser 255 caractères")
    private String title;
    
    @Size(max = 1000, message = "La description ne peut pas dépasser 1000 caractères")
    private String description;
    
    @NotNull(message = "Le type de document est obligatoire")
    private DocumentType type;
    
    @NotBlank(message = "Le niveau de visibilité est obligatoire")
    private String visibilityLevel;
    
    // Pas de champs pour le fichier car il sera transmis séparément dans la requête multipart
}