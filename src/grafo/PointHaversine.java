package grafo;

import java.awt.geom.Point2D;

public class PointHaversine extends Point2D {
	private double lat;
	private double lon;
	private static double curvature = 6371000;

	public PointHaversine() {
		this.lat = 0;
		this.lon = 0;
	}

	@Override
	public double getX() {
		return this.lat;
	}

	@Override
	public double getY() {
		return this.lon;
	}

	@Override
	public void setLocation(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}

	@Override
	public double distance(Point2D pt) {
		return this.distance(pt.getX(), pt.getY());
	}

	@Override
	public double distance(double plat, double plon) {
		double lat1_rad = Math.toRadians(this.lat);
		double lat2_rad = Math.toRadians(plat);
		double delta_lat = Math.toRadians(plat - this.lat);
		double delta_lon = Math.toRadians(plon - this.lon);
		double a = (Math.sin(delta_lat / 2) * Math.sin(delta_lat / 2)
				+ Math.cos(lat1_rad) * Math.cos(lat2_rad) * Math.sin(delta_lon / 2) * Math.sin(delta_lon / 2));

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = curvature * c;
		return d;
	}

	public static double getCurvature() {
		return curvature;
	}

	public static void setCurvature(double curvature) {
		PointHaversine.curvature = curvature;
	}

}
