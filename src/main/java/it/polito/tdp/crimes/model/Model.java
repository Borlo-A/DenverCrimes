package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.Arco;
import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private Graph<String, DefaultWeightedEdge> grafo;
	private List<Arco> archiProvv;
	private List<Arco> archi;
	private List<Arco> archiSopraMedia;
	private List<String> soluzione;
	Set<String> visitati;
	
	public Model()
	{
		dao = new EventsDao();
		soluzione = new ArrayList<String>();
	}
	
	public void creaGrafo(String categoria, int mese)
	{
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
// Aggiungere i vertici
		Graphs.addAllVertices(this.grafo, dao.getTypes(categoria, mese));
		
// Aggiungere gli archi
		archiProvv = new ArrayList<Arco>(getArchi(categoria, mese));
		archi = new ArrayList<Arco>();
		for (Arco a : archiProvv)
		{
			DefaultWeightedEdge edge = this.grafo.getEdge(a.getOffense_type_id_1(), a.getOffense_type_id_2());
			if(edge==null)
			{
				Graphs.addEdge(this.grafo, a.getOffense_type_id_1(),a.getOffense_type_id_2(), a.getPeso());				
				archi.add(a);
			}
		}
		
		archiSopraMedia = new ArrayList<Arco>();
		double pesoMedio = this.getPesoMedio(archi);
		for(Arco a : archi)
		{
			if(a.getPeso()>pesoMedio)
				archiSopraMedia.add(a);
		}
	}
	
	public List<Arco> getArchiSopraMedia()
	{
		Collections.sort(archiSopraMedia);
		return this.archiSopraMedia;
	}

	public double getPesoMedio(List<Arco> archi)
	{
		double somma=0;
		for(Arco a : archi)
		{
			somma += a.getPeso();
		}
		return somma/archi.size();
	}
	
	public List<String> getCategories() {
		return this.dao.getCategories();
	}
	
	public List<Arco> getArchi(String categoria, int mese)
	{
		return this.dao.getArchi(categoria, mese);
	}
	
	public List<String> calcolaPercorso(Arco arco)
	{
		// cerco la componente connessa del vertice source
		ConnectivityInspector<String, DefaultWeightedEdge> ci = new ConnectivityInspector<String, DefaultWeightedEdge>(this.grafo);
		visitati = new HashSet<>();
		visitati = ci.connectedSetOf(arco.getOffense_type_id_1());
		List<String> parziale = new ArrayList<>();
		parziale.add(arco.getOffense_type_id_1());
		
		ricorsiva(parziale, 1, arco);
		// cerco la componente connessa del vertice target
		return soluzione;
	}
	
	public void ricorsiva(List<String> parziale, int L, Arco arco)
	{
// Casi terminali
		if(parziale.size()>1)
		{
			DefaultWeightedEdge edge = this.grafo.getEdge(parziale.get(parziale.size()-2), parziale.get(parziale.size()-1));
			if(edge==null)
				return;
		}
		
// Soluzioni valide
		
		if(parziale.get(parziale.size()-1).compareTo(arco.getOffense_type_id_2())==0)
		{
			int max=0;
			if(parziale.size()>max)
			{
				max=parziale.size();
				soluzione = new ArrayList<String>(parziale);
			}
			//soluzioni.add(new ArrayList<String>(parziale));
		}
		
// Generiamo i sottoproblemi
		for (String s : visitati)
		{
			if(!parziale.contains(s))
			{
				parziale.add(s);
				ricorsiva(parziale, L+1, arco);
				parziale.remove(parziale.get(parziale.size()-1));
			}
		}
	}
}
