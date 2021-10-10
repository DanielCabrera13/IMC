package dad.IMC;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class IMC extends Application {

	private TextField texto1;
	private TextField texto2;
	private Label IMC;
	private Label resultado;
	
	private DoubleProperty operando1 = new SimpleDoubleProperty();
	private DoubleProperty operando2 = new SimpleDoubleProperty();
	private DoubleProperty calculo = new SimpleDoubleProperty();
	private DoubleProperty metros = new SimpleDoubleProperty();

	@Override
	public void start(Stage primaryStage) throws Exception {

		// creamos etiqueta
		Label peso = new Label();
		peso.setText("Peso: ");

		// creamos etiqueta
		Label kg = new Label();
		kg.setText("kg");

		// creamos etiqueta
		Label altura = new Label();
		altura.setText("Altura: ");

		// creamos etiqueta
		Label cm = new Label();
		cm.setText("cm");

		IMC = new Label("");
		Label imcRes = new Label();
		imcRes.setText("IMC: ");
		
		resultado = new Label();

		// Creamos cuadro de texto 1
		texto1 = new TextField();
		texto1.setAlignment(Pos.CENTER_LEFT);
		texto1.setMaxWidth(50); // texto

		// Creamos cuadro de texto 2
		texto2 = new TextField();
		texto2.setAlignment(Pos.CENTER_LEFT);
		texto2.setMaxWidth(50); // texto

		// Creamos HBox
		HBox h1 = new HBox(5, peso, texto1, kg);
		h1.setAlignment(Pos.CENTER);

		// Creamos HBox, panel horizontal
		HBox h2 = new HBox(5, altura, texto2, cm);
		h2.setAlignment(Pos.CENTER);

		// Creamos HBox, panel horizontal
		HBox h3 = new HBox(imcRes,IMC);
		h3.setAlignment(Pos.CENTER);

		// Creamos HBox, panel horizontal
		HBox h4 = new HBox(resultado);
		h4.setAlignment(Pos.CENTER);

		// Panel con disposición vertical
		VBox root = new VBox();
		root.setSpacing(5);
		root.setAlignment(Pos.CENTER);
		root.getChildren().addAll(h1, h2, h3, h4);

		// Creamos la escena
		Scene scene = new Scene(root, 320, 200);

		// configuramos la ventana
		primaryStage.setTitle("IMC");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
		//BINDEOS: CALCULO DE IMC
		Bindings.bindBidirectional(texto1.textProperty(), operando1, new NumberStringConverter());
		Bindings.bindBidirectional(texto2.textProperty(), operando2, new NumberStringConverter());
		metros.bind(operando2.divide(100));
		calculo.bind(operando1.divide(metros.multiply(metros)));
		
		//CONDICION PARA EVITAR NaN E infinity
		BooleanProperty div = new SimpleBooleanProperty();
		div.bind(calculo.greaterThan(18.5f).and(calculo.lessThan(30f)));
		
		IMC.textProperty().bind(Bindings.concat(Bindings.when(div).then(calculo.asString("%.2f")).otherwise("")));
		
		
		//BINDEOS: RESULTADO DE IMC
		BooleanProperty bajo = new SimpleBooleanProperty();
		BooleanProperty normal = new SimpleBooleanProperty();
		BooleanProperty sobrepeso = new SimpleBooleanProperty();
		BooleanProperty obeso = new SimpleBooleanProperty();
		
		bajo.bind(calculo.lessThan(18.5f));
		normal.bind(calculo.greaterThanOrEqualTo(18.5f).and(calculo.lessThan(25f)));
		sobrepeso.bind(calculo.greaterThanOrEqualTo(25f).and(calculo.lessThan(30f)));
		obeso.bind(calculo.greaterThanOrEqualTo(30f));
		
		StringExpression finalRes = Bindings.concat(Bindings.when(bajo).then("BAJO").otherwise("")
				.concat(Bindings.when(normal).then("NORMAL").otherwise(""))
						.concat(Bindings.when(sobrepeso).then("SOBREPESO").otherwise(""))
								.concat(Bindings.when(obeso).then("OBESO").otherwise("")));
		
		//CONDICION PARA EVITAR QUE SALGA NADA ANTES DE TENER EL RESULTADO FINAL
		BooleanProperty div2 = new SimpleBooleanProperty();
		div2.bind(calculo.greaterThan(18.5f).and(calculo.lessThan(30f)));
		resultado.textProperty().bind(Bindings.concat(Bindings.when(div2).then(finalRes).otherwise("")));

	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
