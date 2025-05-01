package com.uchk.university.controller;

import com.uchk.university.dto.DocumentDto;
import com.uchk.university.entity.Document;
import com.uchk.university.entity.DocumentType;
import com.uchk.university.entity.User;
import com.uchk.university.exception.ResourceNotFoundException;
import com.uchk.university.service.DocumentService;
import com.uchk.university.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Documents", description = "API pour la gestion des documents")
public class DocumentController {
    private final DocumentService documentService;
    private final UserService userService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Récupérer tous les documents accessibles par l'utilisateur actuel")
    public ResponseEntity<List<Document>> getDocumentsForCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.getUserByUsername(userDetails.getUsername());
            List<Document> documents = documentService.getDocumentsForUser(user.getId());
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            log.error("Error fetching documents for user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Récupérer tous les documents (administrateurs uniquement)")
    public ResponseEntity<List<Document>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Récupérer un document par son ID")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Document document = documentService.getDocumentById(id);
        User user = userService.getUserByUsername(userDetails.getUsername());

        if (!documentService.userHasAccessToDocument(document, user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(document);
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Créer un nouveau document")
    public ResponseEntity<Document> createDocument(
            @RequestPart("document") @Valid DocumentDto documentDto,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        try {
            User user = userService.getUserByUsername(userDetails.getUsername());
            
            Document document = new Document();
            document.setTitle(documentDto.getTitle());
            document.setDescription(documentDto.getDescription());
            document.setType(DocumentType.valueOf(documentDto.getType()));
            document.setVisibilityLevel(String.valueOf(documentDto.getVisibilityLevel()));
            
            Document createdDocument = documentService.createDocument(document, user.getId(), file);
            
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(createdDocument.getId()).toUri();
            
            return ResponseEntity.created(location).body(createdDocument);
        } catch (Exception e) {
            log.error("Error creating document: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Mettre à jour un document existant")
    public ResponseEntity<Document> updateDocument(
            @PathVariable Long id,
            @RequestPart("document") @Valid DocumentDto documentDto,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        try {
            // Vérifier si l'utilisateur a le droit de modifier ce document
            if (!documentService.isDocumentCreator(id, userDetails.getUsername()) && 
                !userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            Document document = new Document();
            document.setId(id);
            document.setTitle(documentDto.getTitle());
            document.setDescription(documentDto.getDescription());
            document.setType(DocumentType.valueOf(documentDto.getType()));
            document.setVisibilityLevel(String.valueOf(documentDto.getVisibilityLevel()));
            
            Document updatedDocument = documentService.updateDocument(id, document, file);
            return ResponseEntity.ok(updatedDocument);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating document: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Supprimer un document")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        try {
            // Vérifier si l'utilisateur a le droit de supprimer ce document
            if (!documentService.isDocumentCreator(id, userDetails.getUsername()) && 
                !userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            documentService.deleteDocument(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting document: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/download/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Télécharger un fichier de document")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        try {
            Document document = documentService.getDocumentById(id);
            User user = userService.getUserByUsername(userDetails.getUsername());
            
            if (!documentService.userHasAccessToDocument(document, user)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            // Charger le fichier comme ressource
            Resource resource = documentService.loadFileAsResource(id);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getFileName() + "\"")
                    .body(resource);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            log.error("Error downloading file: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/types")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Récupérer tous les types de documents")
    public ResponseEntity<List<DocumentType>> getAllDocumentTypes() {
        return ResponseEntity.ok(Arrays.asList(DocumentType.values()));
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Récupérer les documents par type")
    public ResponseEntity<List<Document>> getDocumentsByType(
            @PathVariable DocumentType type,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.getUserByUsername(userDetails.getUsername());
            List<Document> allDocuments = documentService.getDocumentsByType(type);
            
            // Filtrer les documents selon les droits d'accès de l'utilisateur
            List<Document> accessibleDocuments = allDocuments.stream()
                    .filter(doc -> documentService.userHasAccessToDocument(doc, user))
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(accessibleDocuments);
        } catch (Exception e) {
            log.error("Error fetching documents by type: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/creator/{userId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Récupérer les documents par créateur")
    public ResponseEntity<List<Document>> getDocumentsByCreator(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User currentUser = userService.getUserByUsername(userDetails.getUsername());
            List<Document> creatorDocuments = documentService.getDocumentsByCreator(userId);
            
            // Filtrer les documents selon les droits d'accès de l'utilisateur
            List<Document> accessibleDocuments = creatorDocuments.stream()
                    .filter(doc -> documentService.userHasAccessToDocument(doc, currentUser))
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(accessibleDocuments);
        } catch (Exception e) {
            log.error("Error fetching documents by creator: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/visibility/{level}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Récupérer les documents par niveau de visibilité")
    public ResponseEntity<List<Document>> getDocumentsByVisibilityLevel(
            @PathVariable String level,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.getUserByUsername(userDetails.getUsername());
            
            // Vérifier si l'utilisateur a accès à ce niveau de visibilité
            boolean hasAccess = level.equals("PUBLIC") || 
                                user.getRole().name().equals(level) || 
                                user.getRole().name().equals("ADMIN");
            
            if (!hasAccess) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            List<Document> documents = documentService.getDocumentsByVisibilityLevel(level);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            log.error("Error fetching documents by visibility level: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}