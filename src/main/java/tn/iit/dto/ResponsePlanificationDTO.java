package tn.iit.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.iit.entity.ResultatTraitement;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePlanificationDTO {
	private ResultatTraitement resultat;
    private String message;

}
