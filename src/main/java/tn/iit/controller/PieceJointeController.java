package tn.iit.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.web.util.UriUtils;   // pour encoder le nom du fichier
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.MalformedURLException;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import tn.iit.dto.ApiResponse;
import tn.iit.dto.PieceJointeDTO;
import tn.iit.entity.CourrierEntrant;
import tn.iit.entity.CourrierSortant;
import tn.iit.entity.Employe;
import tn.iit.entity.PieceJointe;
import tn.iit.service.CourrierEntrantService;
import tn.iit.service.CourrierSortantService;
import tn.iit.service.EmployeService;
import tn.iit.service.PieceJointeService;

@RestController
@RequestMapping("/api/pieces-jointes")
@RequiredArgsConstructor
public class PieceJointeController {

    private final PieceJointeService pieceJointeService;
    private final CourrierEntrantService courrierEntrantService;
    private final CourrierSortantService courrierSortantService;
    private final EmployeService employeService;

    private final Path fileStorageLocation =
            Paths.get(System.getProperty("user.dir"), "uploads").toAbsolutePath().normalize();

    // ================== UPLOAD ENTRANT ==================
    @PostMapping("/upload/courrier-entrant/{courrierId}")
    public ResponseEntity<ApiResponse> uploadEntrant(
            @PathVariable Long courrierId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description,
            Authentication auth) {

        try {
            Files.createDirectories(fileStorageLocation);

            CourrierEntrant courrier = courrierEntrantService.findById(courrierId)
                    .orElseThrow(() -> new RuntimeException("Courrier entrant non trouvé"));

            Employe uploadedBy = employeService.findByEmail(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Employé non trouvé"));

            String storedFileName = UUID.randomUUID() + getExtension(file.getOriginalFilename());
            Path targetLocation = fileStorageLocation.resolve(storedFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            PieceJointe pj = new PieceJointe();
            pj.setNomFichier(file.getOriginalFilename());
            pj.setCheminStockage(targetLocation.toString());
            pj.setDescription(description);
            pj.setTaille(file.getSize());
            pj.setCourrierEntrant(courrier);
            pj.setUploadedBy(uploadedBy);

            PieceJointe savedPj = pieceJointeService.save(pj);
            PieceJointeDTO dto = new PieceJointeDTO(savedPj); // <- transformer en DTO

            return ResponseEntity.ok(
                    ApiResponse.success("Upload réussi", dto)
            );

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    // ================== UPLOAD SORTANT ==================
    @PostMapping("/upload/courrier-sortant/{courrierId}")
    public ResponseEntity<ApiResponse> uploadSortant(
            @PathVariable Long courrierId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description,
            Authentication auth) {

        try {
            Files.createDirectories(fileStorageLocation);

            CourrierSortant courrier = courrierSortantService.findById(courrierId)
                    .orElseThrow(() -> new RuntimeException("Courrier sortant non trouvé"));

            Employe uploadedBy = employeService.findByEmail(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Employé non trouvé"));

            String storedFileName = UUID.randomUUID() + getExtension(file.getOriginalFilename());
            Path targetLocation = fileStorageLocation.resolve(storedFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            PieceJointe pj = new PieceJointe();
            pj.setNomFichier(file.getOriginalFilename());
            pj.setCheminStockage(targetLocation.toString());
            pj.setDescription(description);
            pj.setTaille(file.getSize());
            pj.setCourrierSortant(courrier);
            pj.setUploadedBy(uploadedBy);

            PieceJointe savedPj = pieceJointeService.save(pj);
            PieceJointeDTO dto = new PieceJointeDTO(savedPj); // <- transformer en DTO

            return ResponseEntity.ok(
                    ApiResponse.success("Upload réussi", dto)
            );

        } catch (Exception e) {
            e.printStackTrace();
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

    // ================== UTILS ==================
    private String getExtension(String name) {
        if (name == null || !name.contains(".")) return "";
        return name.substring(name.lastIndexOf("."));
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
