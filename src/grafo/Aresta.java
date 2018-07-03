package grafo;

public class Aresta {
	private String id;
	private boolean two_way;

	private Vertice inicio;
	private Vertice fim;

	public Aresta(String id, boolean two_way, Vertice inicio, Vertice fim) {
		super();
		this.id = id;

		this.setTwo_way(two_way);
		this.setInicio(inicio);
		this.setFim(fim);
	}

	public String getId() {
		return id;
	}

	public boolean isTwo_way() {
		return two_way;
	}

	public void setTwo_way(boolean two_way) {
		this.two_way = two_way;
	}

	public Vertice getInicio() {
		return inicio;
	}

	public void setInicio(Vertice inicio) {
		this.inicio = inicio;
	}

	public Vertice getFim() {
		return fim;
	}

	public void setFim(Vertice fim) {
		this.fim = fim;
	}

	public double getPeso() {
		return inicio.getCoord().distance(fim.getCoord());
	}

	public Rede getRede() {
		if (this.inicio.getRede() == this.fim.getRede())
			return this.inicio.getRede();
		else
			return Rede.CROSSLAYER;
	}
}
