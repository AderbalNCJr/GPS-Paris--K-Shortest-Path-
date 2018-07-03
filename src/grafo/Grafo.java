package grafo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Grafo {
	private Map<String, Vertice> vertices;
	private Map<String, Aresta> arestas;

	public Grafo() {
		this.vertices = new HashMap<>();
		this.arestas = new HashMap<>();
	}

	public void addArestas(Aresta a) {
		this.arestas.put(a.getId(), a);
	}

	public void addVertices(Vertice v) {
		this.vertices.put(v.getId(), v);
	}

	public Aresta[] getArestas() {
		Aresta a[] = new Aresta[arestas.values().size()];
		return arestas.values().toArray(a);
	}

	public Vertice[] getVertices() {
		Vertice v[] = new Vertice[vertices.values().size()];
		return vertices.values().toArray(v);
	}

	public Aresta[] Djikstra(Vertice inicial, Vertice destino) {
		List<Aresta> Caminho = new ArrayList<>();
		PriorityQueue<DijkstraVertice> Unvisited = new PriorityQueue<>();
		DijkstraVertice unv[];
		DijkstraVertice current = null;
		
	
		for (Vertice v : this.getVertices()) {
			Unvisited.add(new DijkstraVertice(v));
			
		}

		unv = new DijkstraVertice[Unvisited.size()];
		unv = Unvisited.toArray(unv);

		for (DijkstraVertice v : unv) {
			if (v.equals(inicial)) {
				current = v;
				current.setMinPeso(0.0);
				Unvisited.remove(inicial);
			}
		}

		while (!Unvisited.isEmpty()) {
			for (Aresta a : this.getArestas()) {
				if (a.getInicio().equals(current) || (a.isTwo_way() && a.getFim().equals(current))) {
					unv = new DijkstraVertice[Unvisited.size()];
					unv = Unvisited.toArray(unv);

					 for (DijkstraVertice v : unv) {
						if (v.equals(a.getFim()) || (a.isTwo_way() && v.equals(a.getInicio()))) {
							if (v.setMinPeso(a.getPeso())) {
								Unvisited.remove(v);
								Unvisited.add(v);
							}
						}
					} 
				}
			}
			DijkstraVertice prev = current;
			current = Unvisited.poll();
			current.setPrev(prev);

			if (current.equals(destino)) {
				while (current.getPrev() != null) {
					for (Aresta a : this.getArestas()) {
						if ((a.getInicio().equals(current) && a.getFim().equals(current.getPrev()))
								|| (a.getFim().equals(current) && a.getInicio().equals(current.getPrev()))) {
							Caminho.add(a);
							break;
						}
					}
					current = current.getPrev();
				}

				Collections.reverse(Caminho);
				Unvisited.clear();
			}
		}

		Aresta path[] = new Aresta[Caminho.size()];
		
		ArrayList<Aresta> normal = new ArrayList<Aresta>();
		
		for(int i = Caminho.size() - 1; i >= 0; i--){  //invertendo o caminho contrario entre o vertice inicial e final e guardando em normal
			normal.add(Caminho.get(i));	
		}
		
		return Caminho.toArray(path);
	}

	public Aresta[][] Yen(Vertice inicial, Vertice destino, int K) {
		System.out.println("Iniciando Calculo, vértice de origem: " + inicial.getId() + ", vértice destino: " + destino.getId());
		Aresta[][] A = new Aresta[K][];
		A[0] = this.Djikstra(inicial, destino);
		System.out.println("Calculando Djikstra...");
		PriorityQueue<Aresta[]> B = new PriorityQueue<>(new Comparator<Aresta[]>() {

			@Override
			public int compare(Aresta[] o1, Aresta[] o2) {
				Double p1 = 0.0, p2 = 0.0;
				for (int i = 0; i < o1.length; i++) {
					p1 += o1[i].getPeso();
				}
				for (int i = 0; i < o2.length; i++) {
					p2 += o2[i].getPeso();
				}

				return p1.compareTo(p2);
			}
		});

		System.out.println("Calculando Yen...");
		for (int k = 1; k < K; k++) {
			System.out.println(k);
			for (int i = 0; i < (A[k - 1].length - 2); i++) {
				System.out.println(k + ":" + i);
				ArrayList<Aresta> restoreEdge = new ArrayList<>();
				ArrayList<Vertice> restoreVertex = new ArrayList<>();

				Vertice impulso = A[k - 1][i].getInicio();
				Aresta[] rootPath = Arrays.copyOfRange(A[k - 1], 0, i);

				for (int j = 0; j < k; j++) {
					if (rootPath.equals(Arrays.copyOfRange(A[j], 0, i))) {
						restoreEdge.add(A[j][i]);
						this.arestas.remove(A[j][i].getId());
					}
				}

				for (int j = 0; j < rootPath.length - 1; j++) {
					restoreVertex.add(rootPath[j].getInicio());
					this.vertices.remove(rootPath[j].getInicio().getId());
				}

				Aresta spurPath[] = this.Djikstra(impulso, destino);

				Aresta[] totalPath = new Aresta[rootPath.length + spurPath.length];
				for (int j = 0; j < rootPath.length; j++) {
					totalPath[j] = rootPath[j];
				}
				for (int j = 0; j < spurPath.length; j++) {
					totalPath[j + rootPath.length] = spurPath[j];
				}

				B.add(totalPath);
				for (Aresta aresta : restoreEdge) {
					this.arestas.put(aresta.getId(), aresta);
				}
				for (Vertice vertice : restoreVertex) {
					this.vertices.put(vertice.getId(), vertice);
				}
			}

			if (B.isEmpty()) {
				return A;
			} else {
				A[k] = B.poll();
			}
		}
		return A;
	}
}
