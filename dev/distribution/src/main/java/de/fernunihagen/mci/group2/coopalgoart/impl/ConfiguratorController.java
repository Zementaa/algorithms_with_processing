package de.fernunihagen.mci.group2.coopalgoart.impl;

import static de.fernunihagen.mci.group2.coopalgoart.impl.ParameterInputFactory.addParameterToGridPane;
import static de.fernunihagen.mci.group2.coopalgoart.impl.ParameterInputFactory.toRGBInt;
import static de.fernunihagen.mci.group2.coopalgoart.impl.config.ConfigurationIOHelper.createTmpFile;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import de.fernunihagen.mci.group2.coopalgoart.api.Generator;
import de.fernunihagen.mci.group2.coopalgoart.api.ServiceRegistry;
import de.fernunihagen.mci.group2.coopalgoart.api.services.ServiceDescription;
import de.fernunihagen.mci.group2.coopalgoart.api.services.ServiceFactory;
import de.fernunihagen.mci.group2.coopalgoart.api.services.desc.ParameterDescription;
import de.fernunihagen.mci.group2.coopalgoart.api.services.desc.ranges.BooleanRange;
import de.fernunihagen.mci.group2.coopalgoart.impl.config.ArtConfiguration;
import de.fernunihagen.mci.group2.coopalgoart.impl.config.ConfigurationIOHelper;
import de.fernunihagen.mci.group2.coopalgoart.impl.config.CoopMode;
import de.fernunihagen.mci.group2.coopalgoart.impl.config.GeneratorConfiguration;
import de.fernunihagen.mci.group2.coopalgoart.impl.config.GeneratorConfiguration.GeneratorConfigurationBuilder;
import de.fernunihagen.mci.group2.coopalgoart.impl.config.RendererType;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

/**
 * @author bwinzen
 *
 */
public class ConfiguratorController {
	private static final int PREF_WIDTH_CONTROLS = 500;

	private File lastFileDirectory = new File("").getAbsoluteFile();
	private String filePath;

	@FXML
	private ScrollPane scrollPane;

	@FXML
	private VBox configurationVBox;

	@FXML
	private TitledPane animationConfigurationPanel;

	@FXML
	private ChoiceBox<ServiceFactory<Generator>> generatorChoice;

	@FXML
	private ChoiceBox<CoopMode> coopModeChoice;

	@FXML
	private TextField nameInput;

	@FXML
	private Slider widthInput;

	@FXML
	private Slider heightInput;

	@FXML
	private ColorPicker backgroundColorChoice;

	@FXML
	private TextField seedTextField;

	@FXML
	private Slider fpsSlider;

	@FXML
	private Label fpsLabel;

	@FXML
	private ChoiceBox<RendererType> rendererChoice;

	@FXML
	private Spinner<Integer> maxFrameCountSpinner;

	@FXML
	private Menu exampleMenu;

	@FXML
	private Menu mostRecentMenu;

	private Map<String, GeneratorOptions> generatorOptionsMap = new TreeMap<>();

	private Collection<ServiceFactory<Generator>> possibleGenerators;

	@FXML
	public void initialize() {
		System.out.println("Initialise Controller");
		buildMenu(Starter.examples, exampleMenu);

		possibleGenerators = ServiceRegistry.getServiceFactories(Generator.class);
		ObservableList<ServiceFactory<Generator>> list = FXCollections.observableArrayList(possibleGenerators);
		generatorChoice.setItems(list);
		generatorChoice.setConverter(new StringConverter<ServiceFactory<Generator>>() {
			@Override
			public String toString(ServiceFactory<Generator> object) {
				return object.getServiceDescription().getLabel();
			}

			@Override
			public ServiceFactory<Generator> fromString(String string) {
				return null;
			}
		});
		generatorChoice.setMinWidth(200);
		Tooltip value = new Tooltip();
		value.setOnShowing(ev -> {
			ServiceFactory<Generator> generator = generatorChoice.getValue();
			if(generator == null) {
				return;
			}
			String description = generator.getServiceDescription().getDescription();
			value.setText(description);
		});
		generatorChoice.setTooltip(value);
		maxFrameCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-1, Integer.MAX_VALUE));
		// useage in client code
		maxFrameCountSpinner.focusedProperty().addListener((s, ov, nv) -> {
			if (nv)
				return;
			// intuitive method on textField, has no effect, though
			// spinner.getEditor().commitValue();
			commitEditorText(maxFrameCountSpinner);
		});
		coopModeChoice.setItems(FXCollections.observableArrayList(Arrays.asList(CoopMode.values())));
		coopModeChoice.setConverter(new StringConverter<CoopMode>() {
			@Override
			public String toString(CoopMode object) {
				return object.getLabel();
			}

			@Override
			public CoopMode fromString(String string) {
				return null;
			}
		});
		coopModeChoice.setValue(CoopMode.OVERLAYER);
		backgroundColorChoice.setValue(Color.DARKBLUE);
		seedTextField.setText("0");
		seedTextField.setMinWidth(300);
		fpsLabel.setText(String.format("%.2f", fpsSlider.getValue()));
		fpsLabel.setMinWidth(70);
		fpsLabel.setAlignment(Pos.CENTER_RIGHT);
		fpsSlider.valueProperty().addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> {
			fpsLabel.setText(String.format("%.2f", fpsSlider.getValue()));
		});
		rendererChoice.setItems(FXCollections.observableArrayList(RendererType.values()));
		rendererChoice.setValue(RendererType.FX2D);
		addMostRecent();
	}

	@SuppressWarnings("unchecked")
	private void buildMenu(Map<String, Object> map, Menu menu) {
		map.forEach((k, v) -> {
			if (v instanceof Map) {
				Menu newMenu = new Menu(k);
				buildMenu((Map<String, Object>) v, newMenu);
				menu.getItems().add(newMenu);
			} else {
				MenuItem item = new MenuItem(k);
				item.setOnAction(ev -> {
					try {
						loadConfigurationFromClassPath((String) v);
					} catch (IOException e) {
						handleException(e);
					}
				});
				menu.getItems().add(item);
			}
		});
	}

	/**
	 * copied from
	 * https://stackoverflow.com/questions/32340476/manually-typing-in-text-in-javafx-spinner-is-not-updating-the-value-unless-user
	 * to fix spinner bug in java 1.8
	 */
	private <T> void commitEditorText(Spinner<T> spinner) {
		if (!spinner.isEditable())
			return;
		String text = spinner.getEditor().getText();
		SpinnerValueFactory<T> valueFactory = spinner.getValueFactory();
		if (valueFactory != null) {
			StringConverter<T> converter = valueFactory.getConverter();
			if (converter != null) {
				T value = converter.fromString(text);
				valueFactory.setValue(value);
			}
		}
	}

	public void addGenerator(ActionEvent e) {
		ServiceFactory<Generator> value = generatorChoice.getValue();
		addGenerator(value, null);
	}

	private void addGenerator(ServiceFactory<Generator> serviceFactory, GeneratorConfiguration config) {
		if (serviceFactory == null) {
			return;
		}
		ServiceDescription<Generator> serviceDescription = serviceFactory.getServiceDescription();

		String id = String.format("%03d%s", generatorOptionsMap.size(), serviceDescription.getId());
		GeneratorOptions generatorOptions;
		if (config == null) {
			generatorOptions = new GeneratorOptions();
		} else {
			generatorOptions = new GeneratorOptions(config.isClearScreen(), config.getParameter());
		}
		generatorOptionsMap.put(id, generatorOptions);

		Node parameterInputPane = useDescriptionToBuildUi(generatorOptions,
				serviceDescription.getParameterDescriptions(), config == null ? null : config.getParameter());
		TitledPane titledPane = new TitledPane(serviceDescription.getLabel(), parameterInputPane);
		String description = serviceDescription.getDescription();
		if (serviceDescription.getDescription() != null && !description.isEmpty()) {
			titledPane.setTooltip(new Tooltip(description));
		}
		CheckBox clearScreen = new CheckBox("Verlauf ausschalten");
		Optional<ParameterDescription> optionalClearScreen = serviceDescription.getParameterDescriptions().stream()
				.filter(p -> "clearScreen".equals(p.getId())).findFirst();
		if (config == null && !optionalClearScreen.isPresent()) {
			clearScreen.setSelected(Boolean.TRUE);
		} else if (config == null) {
			BooleanRange r = (BooleanRange) optionalClearScreen.get().getParameterRange();
			clearScreen.setSelected(r.isDefaultValue());
			generatorOptions.setClearScreen(r.isDefaultValue());
		} else {
			clearScreen.setSelected(config.isClearScreen());
		}
		clearScreen.selectedProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
			generatorOptions.setClearScreen(newValue);
		});

		Button up = new Button("\u21D1");
		Button down = new Button("\u21D3");

		Button deleteButton = new Button("X");
		AnchorPane anchorPane = new AnchorPane(titledPane, up, down, clearScreen, deleteButton);
		anchorPane.setUserData(id);
		AnchorPane.setTopAnchor(titledPane, 0.);
		AnchorPane.setRightAnchor(titledPane, 0.);
		AnchorPane.setLeftAnchor(titledPane, 0.);

		AnchorPane.setTopAnchor(down, 1.);
		AnchorPane.setRightAnchor(down, 200.);

		AnchorPane.setTopAnchor(up, 1.);
		AnchorPane.setRightAnchor(up, 225.);

		AnchorPane.setTopAnchor(clearScreen, 4.);
		AnchorPane.setRightAnchor(clearScreen, 50.);

		AnchorPane.setTopAnchor(deleteButton, 1.);
		AnchorPane.setRightAnchor(deleteButton, 5.);
		up.setOnAction(ev -> {
			Node[] array = configurationVBox.getChildren().toArray(new Node[0]);
			// find index
			int i = 0;
			while (i < array.length && array[i] != anchorPane) {
				i++;
			}
			if (i == 1) {
				return;
			}

			int sourceIndex = i;
			int targetIndex = i - 1;
			Node tmp = array[i];
			array[i] = array[targetIndex];
			array[targetIndex] = tmp;
			configurationVBox.getChildren().clear();
			configurationVBox.getChildren().addAll(array);
			Map<String, GeneratorOptions> newGeneratorOptions = new TreeMap<>();
			generatorOptionsMap.forEach((k, v) -> {
				int index = Integer.parseInt(k.substring(0, 3));
				if (index == targetIndex - 1) {
					String newId = String.format("%03d%s", sourceIndex - 1, k.substring(3));
					newGeneratorOptions.put(newId, v);
				} else if (index == sourceIndex - 1) {
					String newId = String.format("%03d%s", targetIndex - 1, k.substring(3));
					newGeneratorOptions.put(newId, v);
				} else {
					newGeneratorOptions.put(k, v);
				}
			});
			generatorOptionsMap = newGeneratorOptions;
		});
		down.setOnAction(ev -> {
			Node[] array = configurationVBox.getChildren().toArray(new Node[0]);
			// find index
			int i = 0;
			while (i < array.length && array[i] != anchorPane) {
				i++;
			}
			if (i == array.length - 1) {
				return;
			}

			Node tmp = array[i];
			int sourceIndex = i;
			int targetIndex = i + 1;
			array[i] = array[targetIndex];
			array[targetIndex] = tmp;
			configurationVBox.getChildren().clear();
			configurationVBox.getChildren().addAll(array);
			Map<String, GeneratorOptions> newGeneratorOptions = new TreeMap<>();
			generatorOptionsMap.forEach((k, v) -> {
				int index = Integer.parseInt(k.substring(0, 3));
				if (index == targetIndex - 1) {
					String newId = String.format("%03d%s", sourceIndex - 1, k.substring(3));
					newGeneratorOptions.put(newId, v);
				} else if (index == sourceIndex - 1) {
					String newId = String.format("%03d%s", targetIndex - 1, k.substring(3));
					newGeneratorOptions.put(newId, v);
				} else {
					newGeneratorOptions.put(k, v);
				}
			});
			generatorOptionsMap = newGeneratorOptions;
		});
		deleteButton.setOnAction(ev -> {
			Map<String, GeneratorOptions> newGeneratorOptions = new TreeMap<>();
			Node[] array = configurationVBox.getChildren().toArray(new Node[0]);
			// find index
			int i = 0;
			while (i < array.length && array[i] != anchorPane) {
				i++;
			}
			String idToDelete = String.format("%03d%s", (i - 1), id.substring(3));
			configurationVBox.getChildren().removeAll(anchorPane);
			generatorOptionsMap.remove(idToDelete);
			generatorOptionsMap.forEach((k, v) -> {
				String newId = String.format("%03d%s", newGeneratorOptions.size(), k.substring(3));
				newGeneratorOptions.put(newId, v);
			});
			generatorOptionsMap = newGeneratorOptions;
		});
		configurationVBox.getChildren().add(anchorPane);

		configurationVBox.parentProperty().getValue().autosize();
	}

	private Node useDescriptionToBuildUi(GeneratorOptions generatorOptions,
			List<ParameterDescription> parameterDescriptions, Map<String, Object> parameterValues) {
		if (parameterDescriptions == null || parameterDescriptions.isEmpty()) {
			return new Label("No parameter defined");
		}
		ParameterDescription[] descriptions = parameterDescriptions.toArray(new ParameterDescription[0]);
		GridPane gridPane = new GridPane();
		gridPane.setPrefWidth(PREF_WIDTH_CONTROLS);
		ColumnConstraints col0 = new ColumnConstraints();
		col0.setMinWidth(150);
		ColumnConstraints col1 = new ColumnConstraints();
		col1.fillWidthProperty().set(true);
		gridPane.getColumnConstraints().addAll(col0, col1);
		for (int i = 0; i < descriptions.length; i++) {
			ParameterDescription description = descriptions[i];
			addParameterToGridPane(generatorOptions, null, gridPane, i, description, parameterValues);
		}
		return gridPane;
	}

	/**
	 * @param event
	 */
	public void recordConfiguration(ActionEvent event) {
		try {
			ArtConfiguration artConfiguration = createArtConfiguration();
//			if (artConfiguration.getRenderer() == RendererType.FX2D) {
//				handleException("JavaFx mode of progressing does not support a second ui because the ui thread is completly consumed by ");
//			} else {
				artConfiguration.setRecordMode(true);
//			}
			String tempFilePath = createTmpFile(artConfiguration);
			Starter.startSecondProcess(tempFilePath, "recorder");
//			} else {
//				Starter.startProcessingWindow(tempFilePath);
//			}
		} catch (Exception e) {
			handleException(e);
		}
	}

	/**
	 * Used on button test configuration.
	 * 
	 * @param event
	 */
	public void testConfiguration(ActionEvent event) {
		try {
			ArtConfiguration artConfiguration = createArtConfiguration();
			String tempFilePath = createTmpFile(artConfiguration);
			Starter.startSecondProcess(tempFilePath);
		} catch (Exception e) {
			handleException(e);
		}
	}

	private void handleException(Exception e) {
		e.printStackTrace();
		handleException(e.getMessage());
	}
	
	private void handleException(String e) {
		Alert errorAlert = new Alert(AlertType.ERROR);
		errorAlert.setHeaderText(e);
		errorAlert.showAndWait();
	}

	private ArtConfiguration createArtConfiguration() throws Exception {
		int width = (int) widthInput.getValue();
		int height = (int) heightInput.getValue();
		Color color = backgroundColorChoice.getValue();
		int backgroundColor = toRGBInt(color);
		int numberOfFrames = maxFrameCountSpinner.getValue() == null ? -1 : maxFrameCountSpinner.getValue().intValue();
		String name = nameInput.getText();
		List<GeneratorConfiguration> generatorConfiguration = createGeneratorConfiguration(width, height);
		if (name == null || name.trim().isEmpty()) {
			name = generatorConfiguration.stream().map(g -> {
				return possibleGenerators.stream()
						.filter(sf -> sf.getServiceDescription().getId().equals(g.getGeneratorId())).findFirst().get()
						.getServiceDescription().getLabel();
			}).collect(Collectors.joining(";"));
		}
		ArtConfiguration artConfiguration = ArtConfiguration.builder() //
				.renderer(rendererChoice.getValue()) //
				.coopMode(coopModeChoice.getValue()) //
				.seed(seedTextField.getText()) //
				.name(name) //
				.width(width) //
				.height(height) //
				.fps((float) fpsSlider.getValue()).delayBetweenIterations(-1) //
				.generatorConfigs(generatorConfiguration) //
				.backgroundColor(backgroundColor) //
				.numberOfFrames(numberOfFrames) //
				.build();

		return artConfiguration;
	}

	private List<GeneratorConfiguration> createGeneratorConfiguration(int width, int height) {
		List<GeneratorConfiguration> results = new LinkedList<>();
		int i = 0;

		for (String id : generatorOptionsMap.keySet()) {
			GeneratorOptions generatorOption = generatorOptionsMap.get(id);
			GeneratorConfigurationBuilder builder = GeneratorConfiguration.builder() //
					.generatorId(id.substring(3)) //
					.generatorVersion(0) //
					.parameter(generatorOption.getParameter()) // TODO make copy
					.clearScreen(generatorOption.isClearScreen());
			switch (coopModeChoice.getValue()) {
			case OVERLAYER:
				builder = builder.xOffset(0).yOffset(0).width(width).height(height);
				break;
			case SPLIT_SCREEN_2X1:
				builder = builder.xOffset(i % 2 == 0 ? 0 : width / 2).yOffset(0).width(width / 2).height(height);
				break;
			case SPLIT_SCREEN_2X2:
				builder = builder.xOffset(i % 2 == 0 ? 0 : width / 2).yOffset(i % 4 < 2 ? 0 : height / 2)
						.width(width / 2).height(height / 2);
				break;
			default:
				break;

			}

			results.add(builder.build());
			i++;
		}
		return results;
	}

	/**
	 * Opens a FileChooser to let the user select an art project.
	 */
	@FXML
	private void open(ActionEvent e) throws IOException {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json, *.js)", "*.json",
				"*.js");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show open file dialog
		fileChooser.setInitialDirectory(lastFileDirectory);
		File file = fileChooser.showOpenDialog(null);
		if (file == null) {
			return;
		}
		lastFileDirectory = file.getParentFile();
		String filePath = file.getPath();
		this.filePath = filePath;
		loadConfiguration(filePath);
	}

	@FXML
	public void openMostRecent() {
		Path tempDir = Paths.get(System.getProperty("java.io.tmpdir")).resolve("mci_fachpraktikum_gruppe2");
		if (Files.exists(tempDir)) {
			try {
				Optional<Path> max = Files.list(tempDir).max((p1, p2) -> {
					try {
						FileTime p1LM = Files.getLastModifiedTime(p1);
						FileTime p2LM = Files.getLastModifiedTime(p2);
						return p1LM.compareTo(p2LM);
					} catch (IOException e) {
						e.printStackTrace();
						return -1;
					}
				});
				if (max.isPresent()) {
					String path = max.get().toAbsolutePath().toString();
					loadConfiguration(path);
					return;
				}
			} catch (IOException e) {
				handleException(e);
			}
		}
		Alert errorAlert = new Alert(AlertType.INFORMATION);
		errorAlert.setHeaderText("Es wurde keine Konfiguration gefunden");
		errorAlert.showAndWait();

	}

	private void loadConfigurationFromClassPath(String filePath) throws IOException {
		InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
		ArtConfiguration artConfiguration = ConfigurationIOHelper.load(stream);
		loadConfiguration(artConfiguration);
	}

	private void loadConfiguration(String filePath) throws IOException {
//		System.out.println(filePath);
		ArtConfiguration artConfiguration = ConfigurationIOHelper.load(filePath);

		loadConfiguration(artConfiguration);
	}

	private void loadConfiguration(ArtConfiguration artConfiguration) {
		widthInput.setValue(artConfiguration.getWidth());
		heightInput.setValue(artConfiguration.getHeight());
		int backgroundColor = artConfiguration.getBackgroundColor();
		backgroundColorChoice.setValue(
				Color.rgb((backgroundColor >> 16) & 0xFF, (backgroundColor >> 8) & 0xFF, backgroundColor & 0xFF));
		maxFrameCountSpinner.getValueFactory().setValue(artConfiguration.getNumberOfFrames());
		rendererChoice.setValue(artConfiguration.getRenderer());
		nameInput.setText(artConfiguration.getName());
		seedTextField.setText(artConfiguration.getSeed() + "");
		fpsSlider.setValue(artConfiguration.getFps());
		coopModeChoice.setValue(artConfiguration.getCoopMode());
		List<GeneratorConfiguration> generatorConfigs = artConfiguration.getGeneratorConfigs();

		// Clear current
		generatorOptionsMap.clear();
		configurationVBox.getChildren().removeIf(n -> !n.equals(animationConfigurationPanel));

		for (GeneratorConfiguration generatorConfig : generatorConfigs) {
			Optional<ServiceFactory<Generator>> first = possibleGenerators.stream()
					.filter(sf -> sf.getServiceDescription().getId().equals(generatorConfig.getGeneratorId()))
					.findFirst();
			ServiceFactory<Generator> serviceFactory = first.get();
			addGenerator(serviceFactory, generatorConfig);
		}
	}

	/**
	 * Saves the file to the file that is currently open.
	 * 
	 * @throws IOException
	 */
	@FXML
	private void save() throws IOException {
		if (this.filePath != null) {
			try {
				ArtConfiguration artConfiguration = this.createArtConfiguration();
				ConfigurationIOHelper.save(filePath, artConfiguration);
			} catch (Exception e) {
				handleException(e);
			}
		} else {
			this.saveAs();
		}

	}

	/**
	 * Opens a FileChooser to let the user select a file to save to.
	 * 
	 * @throws IOException
	 */
	@FXML
	private void saveAs() throws IOException {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json, *.js)", "*.json",
				"*.js");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		fileChooser.setInitialDirectory(lastFileDirectory);
		File file = fileChooser.showSaveDialog(null);
		if (file != null) { // Make sure it has the correct extension
			lastFileDirectory = file.getParentFile();
			if (!file.getPath().endsWith(".json") && !file.getPath().endsWith(".js")) {
				file = new File(file.getPath() + ".json");
			}

			try {
				ArtConfiguration artConfiguration = this.createArtConfiguration();
				ConfigurationIOHelper.save(file.getAbsolutePath(), artConfiguration);
			} catch (Exception e) {
				handleException(e);
			}

		}

	}

	/**
	 * Opens an about dialog.
	 */
	@FXML
	private void showAbout() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Kooperative Kunst");
		alert.setHeaderText(null);
		alert.setContentText(
				"Bildgestalter, der aus ein oder mehreren unterschiedlichen Algorithmen visuelle Ausgaben erzeugt.\n"
						+ "Autoren:\n" + "Bastian Winzen, Catherine Camier, Gino Rissland, Sabine Hopf\n"
						+ "Version 1.0\n" + "Alle Rechte vorbehalten");
		alert.setGraphic(new ImageView(this.getClass().getResource("logAbout.png").toString()));

		alert.showAndWait();
	}

	/**
	 * Opens an control overview dialog.
	 */
	@FXML
	private void showKeyControls() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Steuerung");
		alert.setHeaderText(null);
		alert.setContentText("f\t-\t Zeige FPS\n" + //
				"c\t-\t Zeige den Framecount\n" + //
				"s\t-\t Stoppe/Starte Interationsvorgang\n" + //
				"n\t-\t Steppe über die Iterationen\n" + //
				"p\t-\t Aktuelles Bild speichern\n" + //
				"i\t-\t Aktiviere/Deaktiviere Interactiven Modus (Ab hier ist das System nicht mehr deterministisch)\n"
				+ //
				"\t[0-9]\t-\t Generator verstecken/wiederdarstellen\n" + // );
				"\t\t-\t Generatoren können eigene controls mitbringen\n");

		alert.setGraphic(new ImageView(this.getClass().getResource("logAbout.png").toString()));
		alert.showAndWait();
	}

	public void configureFullScreen() {
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		widthInput.setValue(size.width);
		heightInput.setValue(size.height);
	}

	public void resetUI() {
		configurationVBox.getChildren().remove(1, configurationVBox.getChildren().size());
		generatorOptionsMap.clear();
	}

	private void addMostRecent() {
		Path tempDir = Paths.get(System.getProperty("java.io.tmpdir")).resolve("mci_fachpraktikum_gruppe2");
		if (Files.exists(tempDir)) {
			try {
				List<Path> list = Files.list(tempDir).collect(Collectors.toList());

				list.sort((p1, p2) -> {
					try {
						FileTime p1LM = Files.getLastModifiedTime(p1);
						FileTime p2LM = Files.getLastModifiedTime(p2);
						return p2LM.compareTo(p1LM);
					} catch (IOException e) {
						e.printStackTrace();
						return -1;
					}
				});
				list.stream().limit(5).forEach(p -> {
					ArtConfiguration artConfiguration;
					try {
						artConfiguration = ConfigurationIOHelper.load(p.toString());
						MenuItem menuItem = new MenuItem(artConfiguration.getName());
						menuItem.setOnAction(ev -> {
							loadConfiguration(artConfiguration);
						});
						mostRecentMenu.getItems().add(menuItem);
					} catch (IOException e) {
//						handleException(e); Ignore failure in that case
					}

				});

			} catch (IOException e) {
				handleException(e);
			}
		}

	}

	/**
	 * Connected to quit button
	 * 
	 * @param e
	 */
	@FXML
	public void quit(ActionEvent e) {
		try {
			ArtConfiguration artConfiguration = createArtConfiguration();
			if (!artConfiguration.getGeneratorConfigs().isEmpty()) {
				String tempFilePath = createTmpFile(artConfiguration);
				System.out.println("Save last Config in " + tempFilePath);
			}
		} catch (Exception e1) {
			handleException(e1);
		}
		Exhibit.closeAllInstances();
		System.exit(0);
	}

}
