package com.uchk.university.controller;

import com.uchk.university.dto.DocumentDto;
import com.uchk.university.entity.Document;
import com.uchk.university.entity.DocumentType;
import com.uchk.university.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
@Tag(name = "Document Management", description = "API endpoints for document management")
public class DocumentController {
    
    private final DocumentService documentService;
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMINISTRATION', 'TEACHER', 'FORMATION_MANAGER')")
    @Operation(summary = "Upload a new document", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Document> uploadDocument(
            @RequestPart("file") MultipartFile file,
            @RequestPart("document") DocumentDto documentDto) throws IOException {
        
        Document document = documentService.uploadDocument(file, documentDto);
        return new ResponseEntity<>(document, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get document by ID", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        Document document = documentService.getDocumentById(id);
        return ResponseEntity.ok(document);
    }
    
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get all visible documents", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Document>> getAllVisibleDocuments() {
        List<Document> documents = documentService.getAllVisibleDocuments();
        return ResponseEntity.ok(documents);
    }
    
    @GetMapping("/type/{type}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get documents by type", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Document>> getDocumentsByType(@PathVariable DocumentType type) {
        List<Document> documents = documentService.getDocumentsByType(type);
        return ResponseEntity.ok(documents);
    }
    
    @GetMapping("/download/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Download a document by ID", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) throws IOException {
        Resource resource = documentService.loadDocumentAsResource(id);
        Document document = documentService.getDocumentById(id);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getFileName() + "\"")
                .body(resource);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMINISTRATION', 'TEACHER', 'FORMATION_MANAGER') or @documentService.isDocumentCreator(#id)")
    @Operation(summary = "Update document metadata", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Document> updateDocument(@PathVariable Long id, @RequestBody DocumentDto documentDto) {
        Document document = documentService.updateDocument(id, documentDto);
        return ResponseEntity.ok(document);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMINISTRATION') or @documentService.isDocumentCreator(#id)")
    @Operation(summary = "Delete a document", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{documentId}/assign-formation/{formationId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMINISTRATION', 'FORMATION_MANAGER') or @documentService.isDocumentCreator(#documentId)")
    @Operation(summary = "Assign document to formation", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> assignDocumentToFormation(@PathVariable Long documentId, @PathVariable Long formationId) {
        documentService.assignDocumentToFormation(documentId, formationId);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{documentId}/remove-formation/{formationId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMINISTRATION', 'FORMATION_MANAGER') or @documentService.isDocumentCreator(#documentId)")
    @Operation(summary = "Remove document from formation", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> removeDocumentFromFormation(@PathVariable Long documentId, @PathVariable Long formationId) {
        documentService.removeDocumentFromFormation(documentId, formationId);
        return ResponseEntity.ok().build();
    }
}