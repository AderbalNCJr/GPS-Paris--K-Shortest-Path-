package main;

import grafo.Aresta;
import grafo.Grafo;
import grafo.Vertice;

public class CLI {

	public static void main(String[] args) {
		if(args.length != 5) {
			System.out.println("Como Usar: java main.CLI <arquivo_vertices> <arquivo_arestas> <id_vertice_partida> <id_vertice_chegada> <K>");
			System.exit(1);
		}
		
		try {
			Reader r = new Reader(args[0], args[1]);
			Grafo g = r.getGrafo();
			Vertice start = null, end = null;
			
			for (Vertice v : g.getVertices()) {
				if(v.getId().equals(args[2])) {
					start = v;
					break;
				}
			}
			
			for (Vertice v : g.getVertices()) {
				if(v.getId().equals(args[3])) {
					end = v;
					break;
				}
			}
			
			
			 if(start != null && end != null) {
				Aresta paths[][] = g.Yen(start, end, Integer.parseInt(args[4]));
				Aresta arestas[] = g.getArestas();
				arestas = g.Djikstra(start, end);
				for (int i = 0; i < arestas.length; i++) {
					System.out.println("K = " + i);
						System.out.println(arestas[i].getFim().getId() + " -> " + arestas[i].getInicio().getId() + " " + arestas[i].getRede());
					System.out.println();
				}
			} else {
				throw new NullPointerException("No nao encontrado");
			}
			 
			/*
			if(start != null && end != null) {
				if (Integer.parseInt(args[4]) == 1) {
					Aresta arestas[] = g.getArestas();
					arestas = g.Djikstra(start, end);
					for (int i = 0; i < arestas.length; i++) {
						System.out.println("K = " + i);
						System.out.println(arestas[i].getFim().getId() + " -> " + arestas[i].getInicio().getId() + " " + arestas[i].getRede());
						System.out.println();
					}
				} else if (Integer.parseInt(args[4]) > 1) {
					Aresta paths[][] = g.Yen(start, end, Integer.parseInt(args[4]));
					for (int i = 0; i < paths.length; i++) {
						System.out.println("K = " + i);
						for (int j = 0; j < paths.length; j++) {
							System.out.println(paths[i][j].getInicio().getId() + " -> " + paths[i][j].getFim().getId() + " " + paths[i][j].getRede());
						}
						System.out.println();
					}
				}
			} else {
				throw new NullPointerException("No nao encontrado");
			} */
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}

}
