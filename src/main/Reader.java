package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import grafo.Aresta;
import grafo.Grafo;
import grafo.Rede;
import grafo.Vertice;

public class Reader {
	private Grafo g;

	public Reader() {
		g = new Grafo();
	}

	public Reader(String vertex, String edge) throws IOException {
		g = new Grafo();
		File ver = new File(vertex);
		File edg = new File(edge);

		this.readVertex(ver);
		this.readEdge(edg);
	}

	public Grafo getGrafo() {
		return g;
	}

	public void readVertex(File vertexFile) throws IOException {
		g = new Grafo();

		BufferedReader vertex = new BufferedReader(new FileReader(vertexFile));
		String linha_v = vertex.readLine();

		while ((linha_v = vertex.readLine()) != null) {
			String[] dados_v = linha_v.split(",");

			double lat = Double.parseDouble(dados_v[1]);
			double lon = Double.parseDouble(dados_v[2]);
			Rede rede;

			if (dados_v[3].equals("road"))
				rede = Rede.RODOVIA;
			else if (dados_v[3].equals("train"))
				rede = Rede.TREM;
			else if (dados_v[3].equals("metro"))
				rede = Rede.METRO;
			else
				rede = Rede.TRAM;

			g.addVertices(new Vertice(dados_v[0], lat, lon, rede));
		}

		vertex.close();
	}

	public void readEdge(File edgeFile) throws IOException, NullPointerException {
		BufferedReader edge = new BufferedReader(new FileReader(edgeFile));
		String linha_e = edge.readLine();

		while ((linha_e = edge.readLine()) != null) {

			String[] dados_e = linha_e.split(",");

			Vertice inicio = null;
			for (Vertice vert_lista : g.getVertices())
				if (vert_lista.getId().equals(dados_e[0]))
					inicio = vert_lista;

			Vertice fim = null;
			for (Vertice vert_lista : g.getVertices())
				if (vert_lista.getId().equals(dados_e[1]))
					fim = vert_lista;

			if (inicio == null || fim == null) {
				edge.close();
				throw new NullPointerException("Vertex not found");
			}
			g.addArestas(new Aresta(dados_e[2], dados_e[3].equalsIgnoreCase("twoway"), inicio, fim));

		}

		edge.close();
	}
}
