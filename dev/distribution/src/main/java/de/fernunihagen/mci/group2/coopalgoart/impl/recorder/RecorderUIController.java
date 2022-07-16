package de.fernunihagen.mci.group2.coopalgoart.impl.recorder;

import static de.fernunihagen.mci.group2.coopalgoart.impl.ParameterInputFactory.addParameterToGridPane;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import de.fernunihagen.mci.group2.coopalgoart.api.ServiceRegistry;
import de.fernunihagen.mci.group2.coopalgoart.api.services.ServiceDescription;
import de.fernunihagen.mci.group2.coopalgoart.api.services.ServiceFactory;
import de.fernunihagen.mci.group2.coopalgoart.api.services.desc.ParameterDescription;
import de.fernunihagen.mci.group2.coopalgoart.impl.Artist;
import de.fernunihagen.mci.group2.coopalgoart.impl.Exhibit;
import de.fernunihagen.mci.group2.coopalgoart.impl.Options;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import processing.core.PConstants;

/**
 * @author bwinzen
 *
 */
public class RecorderUIController extends Application {
	private static Exhibit exhibit;

	private static Artist artist;

	@FXML
	private ChoiceBox<ServiceFactory<Recorder>> chooseRecorder;

	@FXML
	private Button startRecordingButton;

	@FXML
	private AnchorPane gridLocation;

	private Options options;

	private double xOffset = 0;
	private double yOffset = 0;

	public static void createUI(Exhibit exhibit, Artist artist) throws Exception {
		RecorderUIController.exhibit = exhibit;
		RecorderUIController.artist = artist;
			if (!exhibit.sketchRenderer().equals(PConstants.FX2D)) {
				Thread thread = new Thread(() -> RecorderUIController.launch(new String[] {}));
				thread.setDaemon(true);
				thread.start();
			}else {
			// Two javafx application could not be started at the same time
			if (Platform.isFxApplicationThread()) {
				new RecorderUIController().start(new Stage());
			} else {
				Platform.runLater(() -> {
					try {
						new RecorderUIController().start(new Stage());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				});

			}
		}

	}

	@FXML
	public void initialize() {
		System.out.println("Initialise RecordController");
		Collection<ServiceFactory<Recorder>> serviceFactories = ServiceRegistry.getServiceFactories(Recorder.class);
		ObservableList<ServiceFactory<Recorder>> list = FXCollections.observableArrayList(serviceFactories);
		chooseRecorder.setItems(list);
		Optional<ServiceFactory<Recorder>> lastImageRecorder = list.stream()
				.filter(sf -> sf.getServiceDescription().getId().equals("lastImage_recorder")).findFirst();
		chooseRecorder.setConverter(new StringConverter<ServiceFactory<Recorder>>() {
			@Override
			public String toString(ServiceFactory<Recorder> object) {
				return object.getServiceDescription().getLabel();
			}

			@Override
			public ServiceFactory<Recorder> fromString(String string) {
				return null;
			}
		});
		chooseRecorder.valueProperty().addListener(new ChangeListener<ServiceFactory<Recorder>>() {
			@Override
			public void changed(ObservableValue<? extends ServiceFactory<Recorder>> observable,
					ServiceFactory<Recorder> oldValue, ServiceFactory<Recorder> newValue) {
				gridLocation.getChildren().clear();
				buildUi(newValue);
			}
		});
		chooseRecorder.setValue(lastImageRecorder.get());
	}

	protected void buildUi(ServiceFactory<Recorder> selectedFactory) {
		ServiceDescription<Recorder> serviceDescription = selectedFactory.getServiceDescription();
		options = new Options();
		Node grid = useDescriptionToBuildUi(options, serviceDescription.getParameterDescriptions());
		gridLocation.getChildren().add(grid);
		gridLocation
				.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		AnchorPane.setBottomAnchor(grid, 0d);
		AnchorPane.setLeftAnchor(grid, 0d);
		AnchorPane.setTopAnchor(grid, 0d);
		AnchorPane.setRightAnchor(grid, 0d);
	}

	private Node useDescriptionToBuildUi(Options options, List<ParameterDescription> parameterDescriptions) {
		if (parameterDescriptions == null || parameterDescriptions.isEmpty()) {
			return new Label("No parameter defined");
		}
		ParameterDescription[] descriptions = parameterDescriptions.toArray(new ParameterDescription[0]);
		GridPane gridPane = new GridPane();
		ColumnConstraints col0 = new ColumnConstraints();
		col0.setMinWidth(150);
		ColumnConstraints col1 = new ColumnConstraints();
		col1.fillWidthProperty().set(true);
		gridPane.getColumnConstraints().addAll(col0, col1);
		for (int i = 0; i < descriptions.length; i++) {
			ParameterDescription description = descriptions[i];
			addParameterToGridPane(options, null, gridPane, i, description, null);
		}
		return gridPane;
	}

	/**
	 * Used on button test configuration.
	 * 
	 * @param event
	 */
	@FXML
	public void startRecording(ActionEvent event) {
		try {
			ServiceFactory<Recorder> serviceFactory = chooseRecorder.getValue();
			if (serviceFactory == null) {
				return;
			}

//			Recorder recorder = new Recorder.Mp4Recorder(Paths.get("C:\\Users\\Bastian\\Desktop\\test.mp4"), 24);
			Recorder recorder = serviceFactory.createService(options.getParameter());

			startRecordingButton.disableProperty().set(true);
			recorder.addOnFinishTask(() -> startRecordingButton.disableProperty().set(false));
			recorder.startRecording();
			artist.register(recorder);
			exhibit.runAnimation();
		} catch (Exception e) {
			handleException(e);
		}
	}

	private void handleException(Exception e) {
		e.printStackTrace();
		Alert errorAlert = new Alert(AlertType.ERROR);
		errorAlert.setHeaderText(e.getMessage());
		errorAlert.showAndWait();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(RecorderUIController.class.getClassLoader().getResource("recorder.fxml"));
		Parent root = loader.load();
		root.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		root.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				primaryStage.setX(event.getScreenX() - xOffset);
				primaryStage.setY(event.getScreenY() - yOffset);
			}
		});
		primaryStage.setTitle("Recorder of " + exhibit.getTitle());
		Scene scene = new Scene(root, 800, 200);
		scene.getStylesheets().add("style.css");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
