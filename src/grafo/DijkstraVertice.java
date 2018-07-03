package grafo;

public class DijkstraVertice extends Vertice implements Comparable<DijkstraVertice> {
	private Double peso;
	private DijkstraVertice prev;

	public DijkstraVertice(String id, double lat, double lon, Rede rede) {
		super(id, lat, lon, rede);
		this.peso = Double.POSITIVE_INFINITY;
		this.prev = null;
	}

	public DijkstraVertice(Vertice v) {
		super(v.getId(), v.getCoord().getX(), v.getCoord().getY(), v.getRede());
		this.peso = Double.POSITIVE_INFINITY;
		this.prev = null;
	}

	public DijkstraVertice(Vertice v, Double peso) {
		super(v.getId(), v.getCoord().getX(), v.getCoord().getY(), v.getRede());
		this.peso = peso;
		this.prev = null;
	}

	public boolean setMinPeso(Double peso) {
		boolean change = peso < this.peso;
		if (change)
			this.peso = peso;
		return change;
	}

	@Override
	public int compareTo(DijkstraVertice arg0) {
		return this.peso.compareTo(arg0.peso);
	}

	public DijkstraVertice getPrev() {
		return prev;
	}

	public void setPrev(DijkstraVertice prev) {
		this.prev = prev;
	}

}
