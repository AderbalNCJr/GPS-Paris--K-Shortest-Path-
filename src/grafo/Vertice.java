package grafo;

import java.awt.geom.Point2D;

public class Vertice {
	private String id;
	private Point2D coord;
	private Rede rede;

	public Vertice(String id, double lat, double lon, Rede rede) {
		super();
		this.coord = new PointHaversine();
		this.id = id;

		this.setCoord(lat, lon);
		this.setRede(rede);
	}

	public String getId() {
		return id;
	}

	public Point2D getCoord() {
		return coord;
	}

	public Rede getRede() {
		return rede;
	}

	public void setCoord(double lat, double lon) {
		this.coord.setLocation(lat, lon);
	}

	public void setRede(Rede rede) {
		this.rede = rede;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vertice) {
			Vertice v = (Vertice) obj;
			return this.id.equals(v.id);
		}

		return false;
	}

}
