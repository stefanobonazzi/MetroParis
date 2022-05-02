package it.polito.tdp.metroparis.model;

import java.util.*;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.*;
import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {

	private Graph<Fermata, DefaultEdge> grafo;
	private MetroDAO dao = new MetroDAO();
	
	public void creaGrafo() {
		this.grafo = new SimpleDirectedGraph<Fermata, DefaultEdge>(DefaultEdge.class);
		
		List<Fermata> fermate = dao.getAllFermate();
		Map<Integer, Fermata> fermateIdMap = new HashMap<>();
		for(Fermata f: fermate)
			fermateIdMap.put(f.getIdFermata(), f);
		
		Graphs.addAllVertices(this.grafo, fermate);
		
		//Metodo 1: itero su ogni coppia di vertici.
//		for(Fermata partenza: fermate) {
//			for(Fermata arrivo: fermate) {
//			//Esiste almeno una connessione tra partenza e arrivo
//				if(dao.isFermateConnesse(partenza, arrivo)) {
//					this.grafo.addEdge(partenza, arrivo);
//				}
//			}
//		}
		
		
		//Metodo 2: itero su ogni veritce per aggiungere gli archi uscenti
		//Vatiante 2A: il DAO restituisce un elenco di ID numerici che uso per iterare sull'array totale.
//		for(Fermata partenza: fermate) {
//			List<Integer> idConnesse = dao.getIdFermateConnesse(partenza);
//			for(Integer id: idConnesse) {
//				Fermata arrivo = null;
//				for(Fermata f: this.grafo.vertexSet()) {
//					if(f.getIdFermata() == id) {
//						arrivo = f;
//						break;
//					}
//				}
//				this.grafo.addEdge(partenza, arrivo);
//			}
//		}
		
		
		//Metodo 2: itero su ogni veritce per aggiungere gli archi uscenti
		//Variante 2B: il DAO restituisce un elenco di oggetti fermata.
//		for(Fermata partenza: fermate) {
//			List<Fermata> arrivi = dao.getFermateConnesse(partenza);
//			for(Fermata arrivo: arrivi) {
//				this.grafo.addEdge(partenza, arrivo);
//			}
//		}
		
		
		//Metodo 2: itero su ogni veritce per aggiungere gli archi uscenti
		//Variante 2C: il DAO restituisce un elenco di ID numerici, che converto in oggetti tramite una Map<Integer, Fremata>
		//si chiama: "Identity Map".
//		for(Fermata partenza: fermate) {
//			List<Integer> idConnesse = dao.getIdFermateConnesse(partenza);
//			for(Integer id: idConnesse) {
//				Fermata arrivo = fermateIdMap.get(id);
//				this.grafo.addEdge(partenza, arrivo);
//			}
//		}
		
		//Metodo 3: faccio una sola query che mi restituisca la coppia di fermate da collegare.
		List<CoppiaId> fermateDaCollegare = dao.getAllConnessioni();
		for(CoppiaId coppia: fermateDaCollegare) 
			this.grafo.addEdge(fermateIdMap.get(coppia.getIdPartenza()), fermateIdMap.get(coppia.getIdArrivo()));
		
		
		System.out.println(this.grafo);
		System.out.println("Vertici = " + this.grafo.vertexSet().size());
		System.out.println("Archi = " + this.grafo.edgeSet().size());

		this.visitaGrafo(fermate.get(0));
	}
	
	public void visitaGrafo(Fermata partenza) {
		GraphIterator<Fermata, DefaultEdge> visita = new BreadthFirstIterator<>(this.grafo, partenza);
		
		while(visita.hasNext()) {
			Fermata f = visita.next();
			System.out.println(f);
		}
	}
	
}
