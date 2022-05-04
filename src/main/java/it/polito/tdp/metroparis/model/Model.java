package it.polito.tdp.metroparis.model;

import java.util.*;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.*;
import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {

	private List<Fermata> fermate;
	private Map<Integer, Fermata> fermateIdMap;
	private Graph<Fermata, DefaultEdge> grafo;
	private MetroDAO dao = new MetroDAO();
	
	public List<Fermata> calcolaPercorso(Fermata partenza, Fermata arrivo) {
		this.creaGrafo();
		this.visitaGrafo(partenza, arrivo);
		List<Fermata> percorso = null;
		
		return percorso;
	}
	
	public void creaGrafo() {
		this.grafo = new SimpleDirectedGraph<Fermata, DefaultEdge>(DefaultEdge.class);
		
		Graphs.addAllVertices(this.grafo, this.getFermate());
	
		List<CoppiaId> fermateDaCollegare = dao.getAllConnessioni();
		for(CoppiaId coppia: fermateDaCollegare) 
			this.grafo.addEdge(fermateIdMap.get(coppia.getIdPartenza()), fermateIdMap.get(coppia.getIdArrivo()));
		
		
//		System.out.println(this.grafo);
//		System.out.println("Vertici = " + this.grafo.vertexSet().size());
//		System.out.println("Archi = " + this.grafo.edgeSet().size());
	}

	public void visitaGrafo(Fermata partenza, Fermata arrivo) {
		GraphIterator<Fermata, DefaultEdge> visita = new BreadthFirstIterator<>(this.grafo, partenza);
		Map<Fermata, Fermata> alberoInverso = new HashMap<>();
		alberoInverso.put(partenza, null);
		
		visita.addTraversalListener(new RegistraAlberoDiVisita(alberoInverso, this.grafo));
		
		while(visita.hasNext()) {
			Fermata f = visita.next();
//			System.out.println(f);
		}
		
		List<Fermata> percorso = new ArrayList<>();
		Fermata fermata = arrivo;
		while(fermata != null) {
			fermata = alberoInverso.get(fermata);
			percorso.add(fermata);
		}
	}
	
	public List<Fermata> getFermate() {
		if(this.fermate == null) {
			this.fermate = dao.getAllFermate();
			
			this.fermateIdMap = new HashMap<>();
			for(Fermata f: this.fermate)
				this.fermateIdMap.put(f.getIdFermata(), f);
		}
		
		return fermate;
	}
	
}
