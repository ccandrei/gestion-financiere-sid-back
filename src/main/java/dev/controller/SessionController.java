package dev.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.controller.vm.SessionLigneVM;
import dev.controller.vm.SessionVM;
import dev.domain.Session;
import dev.repository.SessionRepo;

@RestController
@CrossOrigin
public class SessionController {

	// Ici requestes d'acces au tables
	private SessionRepo sessionRepo;	

	private static final Logger LOG = LoggerFactory.getLogger(dev.controller.SessionController.class);

	public SessionController(SessionRepo sessionRepo) {
		this.sessionRepo = sessionRepo;
	}

	/** 
	 * Retourne la liste de tous les Sessions 
	 * 
	 * @param 
	 * @return
	 */	 
	@RequestMapping(method = RequestMethod.GET, path = "Sessions")
	public List<SessionVM> getSessions() {
		LOG.info( "*** Recuperer tous les Sessions ***");
		List<Session> listeSessions = this.sessionRepo.findAll();
		LOG.info( listeSessions.get(0).toString());
		return listeSessions.stream().map(col -> new SessionVM(col)).collect(Collectors.toList());
	}

	/** Rechercher la liste des sessions filtrées par : 
	*    - libellé : établissement / formation / certification 
	*                salle        / entreprise
	*    - période : date de début, date de fin
	*    
	* Chaque filtre est optionnel.
	* Pour les libellés on peut saisir un sous libelle
	* 
	* @return List<SessionLigne> : liste des lignes session à afficher
	*/
	@RequestMapping(method = RequestMethod.GET, path = "SessionsFiltres") 
	public List<SessionLigneVM> getSessionFiltreParMatriculeNom( 
			@RequestParam("etablissement") 		String etablissement, 
            @RequestParam("formation") 			String formation,
            @RequestParam("certification") 		String certif,
            @RequestParam("salle") 				String salle,
            @RequestParam("entreprise") 		String entreprise,
            @RequestParam("dateDebutSession") 	String dateDebut,
			@RequestParam("dateFinSession") 	String dateFin) {
		
		LOG.info( "*** Filtrer les Sessions par : " );
		LOG.info( "   - établissement / formation / certif ==>" + etablissement + '/' + formation + '/' + certif); 
		LOG.info( "   - salle     / entreprise             ==>" + salle + '/'+ entreprise );
		LOG.info( "   - dateDebut / dateFin                ==>" + dateDebut + '/' + dateFin  );
		
		/** Récupérer les infos de la BD */
		List<Session> listeSessionsRepo 	= this.sessionRepo.findByNomStartingWith( formation );
		for( Session session : listeSessionsRepo) {
				LOG.info( "*** Nom de session / formation : " 
							+ session.getNom() + " / " + session.getFormation().getNom());
		} 
		
		/** Renvoyer au front les résultats */
		return listeSessionsRepo.stream().map(session -> new SessionLigneVM( session)).collect( Collectors.toList());
	}


}
