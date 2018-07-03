package main;

import java.io.File;
import java.io.IOException;

import grafo.Aresta;
import grafo.Grafo;
import grafo.Vertice;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class GUI extends Application {

	private static Reader r;
	private static Vertice start, end;
	private static GraphicsContext gc;
	private static File edgeFile, vertexFile;
	private static Integer K;

	public static void main(String[] args) {
		r = new Reader();
		launch(args);
	}

	private void drawGrafo() {
		Grafo g = r.getGrafo();

		gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
		gc.setLineWidth(0.5);

		Double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY;
		Double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;

		Vertice vertices[] = g.getVertices();
		Aresta arestas[] = g.getArestas();
		if (start != null && end != null) {
			if (K == null) {
				arestas = g.Djikstra(start, end);
			} else {
				arestas = g.Yen(start, end, K)[K - 1];
			}
		}

		for (int i = 0; i < vertices.length; i++) {
			Double lat = vertices[i].getCoord().getX();
			Double lon = vertices[i].getCoord().getY();

			if (lat < minX) {
				minX = lat;
			} else if (lat > maxX) {
				maxX = lat;
			}

			if (lon < minY) {
				minY = lon;
			} else if (lon > maxY) {
				maxY = lon;
			}
		}

		for (int i = 0; i < arestas.length; i++) {
			Double lat_i = arestas[i].getInicio().getCoord().getX();
			Double lon_i = arestas[i].getInicio().getCoord().getY();

			Double lat_f = arestas[i].getFim().getCoord().getX();
			Double lon_f = arestas[i].getFim().getCoord().getY();

			Double X0 = ((lat_i - minX) / (maxX - minX)) * gc.getCanvas().getWidth();
			Double Y0 = ((lon_i - minY) / (maxY - minY)) * gc.getCanvas().getHeight();
			Double Xf = ((lat_f - minX) / (maxX - minX)) * gc.getCanvas().getWidth();
			Double Yf = ((lon_f - minY) / (maxY - minY)) * gc.getCanvas().getHeight();

			switch (arestas[i].getRede()) {
			case RODOVIA:
				gc.setStroke(Color.RED);
				break;
			case METRO:
				gc.setStroke(Color.BLUE);
				break;
			case TRAM:
				gc.setStroke(Color.GREEN);
				break;
			case TREM:
				gc.setStroke(Color.YELLOW);
				break;
			default:
				gc.setStroke(Color.BLACK);
				break;
			}

			gc.strokeLine(X0, Y0, Xf, Yf);
		}

		gc.setStroke(null);
		gc.setLineWidth(0);

		for (int i = 0; i < vertices.length; i++) {
			Double lat = vertices[i].getCoord().getX();
			Double lon = vertices[i].getCoord().getY();

			Double X = ((lat - minX) / (maxX - minX)) * gc.getCanvas().getWidth();
			Double Y = ((lon - minY) / (maxY - minY)) * gc.getCanvas().getHeight();

			switch (vertices[i].getRede()) {
			case RODOVIA:
				gc.setFill(Color.RED);
				break;
			case METRO:
				gc.setFill(Color.BLUE);
				break;
			case TRAM:
				gc.setFill(Color.GREEN);
				break;
			case TREM:
				gc.setFill(Color.YELLOW);
				break;
			default:
				gc.setFill(Color.BLACK);
				break;
			}

			gc.fillOval(X, Y, 1, 1);
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane mainLayout = new BorderPane();
		GridPane controlsLayout = new GridPane();
		controlsLayout.getStyleClass().add("grid");

		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Todos Arquivos", "*.*"),
				new FileChooser.ExtensionFilter("Arquivo CSV", "*.csv"));

		ComboBox<String> startCombo = new ComboBox<>();
		Label startLabel = new Label("Ponto de partida: ");
		controlsLayout.addRow(3, startLabel, startCombo);

		startCombo.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				Grafo g = r.getGrafo();
				start = null;
				if (startCombo.getValue() != null) {
					for (Vertice v : g.getVertices()) {
						if (v.getId().equals(startCombo.getValue().toString())) {
							start = v;
							break;
						}
					}
				}

				drawGrafo();
			}
		});

		ComboBox<String> endCombo = new ComboBox<>();
		Label endLabel = new Label("Ponto de chegada: ");
		controlsLayout.addRow(4, endLabel, endCombo);

		startCombo.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				Grafo g = r.getGrafo();
				end = null;
				if (endCombo.getValue() != null) {
					for (Vertice v : g.getVertices()) {
						if (v.getId().equals(endCombo.getValue().toString())) {
							end = v;
							break;
						}
					}
				}

				drawGrafo();
			}
		});

		Button vertexButton = new Button("Escolher arquivo");
		Label vertexLabel = new Label("Nenhum arquivo escolhido...", vertexButton);
		Label vertexDescription = new Label("Arquivo de vertices");
		controlsLayout.addRow(0, vertexDescription, vertexLabel);

		vertexButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				fileChooser.setTitle("Arquivo de vertices");

				vertexFile = fileChooser.showOpenDialog(primaryStage);
				if (vertexFile != null) {
					vertexLabel.setText(vertexFile.getName());
					try {
						r.readVertex(vertexFile);
						if (edgeFile != null) {
							r.readEdge(edgeFile);
						}

						Grafo g = r.getGrafo();
						Vertice vertices[] = g.getVertices();
						startCombo.getItems().clear();
						startCombo.getItems().add(" ");
						for (int i = 0; i < vertices.length; i++) {
							startCombo.getItems().add(vertices[i].getId());
						}

						endCombo.getItems().clear();
						endCombo.getItems().add(" ");
						for (int i = 0; i < vertices.length; i++) {
							endCombo.getItems().add(vertices[i].getId());
						}

						drawGrafo();
					} catch (IOException e) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setContentText(e.getMessage());
						alert.showAndWait();
						e.printStackTrace();
					}
				}
			}
		});

		Button edgeButton = new Button("Escolher arquivo");
		Label edgeLabel = new Label("Nenhum arquivo escolhido...", edgeButton);
		Label edgeDescription = new Label("Arquivo de arestas");
		controlsLayout.addRow(1, edgeDescription, edgeLabel);

		edgeButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				fileChooser.setTitle("Arquivo de arestas");

				edgeFile = fileChooser.showOpenDialog(primaryStage);
				if (edgeFile != null) {
					edgeLabel.setText(edgeFile.getName());
					try {
						r.readEdge(edgeFile);
						drawGrafo();
					} catch (IOException | NullPointerException e) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setContentText(e.getMessage());
						alert.showAndWait();
						e.printStackTrace();
					}
				}
			}
		});

		TextField kField = new TextField();
		Label kLabel = new Label("Nº de Alternativas: ");
		controlsLayout.addRow(5, kLabel, kField);

		kField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				try {
					K = Integer.parseInt(kField.getText());
				} catch (Exception e) {
					K = 0;
				}
				drawGrafo();
			}

		});

		Canvas canvas = new Canvas(750, 500);
		gc = canvas.getGraphicsContext2D();

		mainLayout.setCenter(canvas);
		mainLayout.setRight(controlsLayout);

		Scene scene = new Scene(mainLayout);
		scene.getStylesheets().add(getClass().getResource("GUI.css").toString());

		primaryStage.setTitle("K menores Caminhos");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
