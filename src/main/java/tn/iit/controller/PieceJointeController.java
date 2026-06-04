package tn.iit.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import lombok.RequiredArgsConstructor;
import tn.iit.dto.ApiResponse;
import tn.iit.dto.PieceJointeDTO;
import tn.iit.entity.CourrierEntrant;
import tn.iit.entity.CourrierSortant;
import tn.iit.entity.PieceJointe;
import tn.iit.service.CourrierEntrantService;
import tn.iit.service.CourrierSortantService;
import tn.iit.service.PieceJointeService;

@RestController
@RequestMapping("/api/pieces-jointes")
@RequiredArgsConstructor
public class PieceJointeController {

    private final PieceJointeService pieceJointeService;
    private final CourrierEntrantService courrierEntrantService;
    private final CourrierSortantService courrierSortantService;
    

   

    // ================== UPLOAD ENTRANT ==================
    @PostMapping("/upload/courrier-entrant/{courrierId}")
    public ResponseEntity<ApiResponse> uploadEntrant(
            @PathVariable Long courrierId,
            @RequestParam("files") List<MultipartFile> files,  // ← liste
            @RequestParam(value = "description", required = false) String description) {
        try {
            CourrierEntrant courrier = courrierEntrantService.getEntityById(courrierId);
            List<PieceJointeDTO> dtos = new ArrayList<>();
            for (MultipartFile file : files) {
                PieceJointe saved = pieceJointeService.uploadEntrant(file, description, courrier);
                dtos.add(new PieceJointeDTO(saved));
            }
            return ResponseEntity.ok(ApiResponse.success("Upload réussi", dtos));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    // ================== UPLOAD SORTANT ==================
    @PostMapping("/upload/courrier-sortant/{courrierId}")
    public ResponseEntity<ApiResponse> uploadSortant(
            @PathVariable Long courrierId,
            @RequestParam("files") List<MultipartFile> files,  // ← liste
            @RequestParam(value = "description", required = false) String description) {
        try {
            CourrierSortant courrier = courrierSortantService.getEntityById(courrierId);
            List<PieceJointeDTO> dtos = new ArrayList<>();
            for (MultipartFile file : files) {
                PieceJointe saved = pieceJointeService.uploadSortant(file, description, courrier);
                dtos.add(new PieceJointeDTO(saved));
            }
            return ResponseEntity.ok(ApiResponse.success("Upload réussi", dtos));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    // ================== GET ==================
    @GetMapping("/courrier-entrant/{id}")
    public ResponseEntity<ApiResponse> getEntrant(@PathVariable Long id) {
    	var dtos = pieceJointeService.findByCourrierEntrant(id)
                .stream()
                .map(PieceJointeDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("OK", dtos));
    }

    @GetMapping("/courrier-sortant/{id}")
    public ResponseEntity<ApiResponse> getSortant(@PathVariable Long id) {
        var dtos = pieceJointeService.findByCourrierSortant(id)
                     .stream()
                     .map(PieceJointeDTO::new)
                     .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("OK", dtos));
    }

    // ================== DELETE ==================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) throws IOException {
        PieceJointe pj = pieceJointeService.findById(id)
                .orElseThrow(() -> new RuntimeException("Fichier non trouvé"));

        Files.deleteIfExists(Paths.get(pj.getCheminStockage()));
        pieceJointeService.deleteById(id);

        return ResponseEntity.ok(ApiResponse.success("Supprimé"));
    }


    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws  IOException, MalformedURLException {
        PieceJointe pj = pieceJointeService.findById(id)
                .orElseThrow(() -> new RuntimeException("Fichier non trouvé"));

        Path path = Paths.get(pj.getCheminStockage());
        Resource resource = new UrlResource(path.toUri());

        // Force le navigateur à utiliser le nom original du fichier
        String encodedFileName = UriUtils.encode(pj.getNomFichier(), StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + encodedFileName + "\"")
                .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path))
                .body(resource);
    }
}
