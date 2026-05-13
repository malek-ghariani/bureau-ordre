package tn.iit.dto;

import lombok.Data;
import tn.iit.entity.PieceJointe;

@Data
public class PieceJointeDTO {
    private Long id;
    private String nomFichier;
    private String description;
    private Long taille;
    private String url;

    public PieceJointeDTO(PieceJointe pj) {
        this.id = pj.getId();
        this.nomFichier = pj.getNomFichier();
        this.description = pj.getDescription();
        this.taille = pj.getTaille();
        this.url = "/api/pieces-jointes/download/" + pj.getId();
    }
}
