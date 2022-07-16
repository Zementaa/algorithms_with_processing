package de.fernunihagen.mci.group2.coopalgoart.impl;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import de.fernunihagen.mci.group2.coopalgoart.api.services.desc.ParameterDescription;
import de.fernunihagen.mci.group2.coopalgoart.api.services.desc.ParameterType;
import de.fernunihagen.mci.group2.coopalgoart.api.services.desc.ranges.*;
import de.fernunihagen.mci.group2.coopalgoart.api.services.desc.ranges.FileRange.Type;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.image.ImageView;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author bwinzen
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ParameterInputFactory {
	public static void addParameterToGridPane(GeneratorOptions generatorOptions, String parameterIdPrefix,
			GridPane gridPane, int i, ParameterDescription description, Map<String, Object> parameterValues) {
		if ("clearScreen".equals(description.getId())) {
			return;
		}
		addParameterToGridPane((Options) generatorOptions, parameterIdPrefix, gridPane, i, description,
				parameterValues);
	}

	public static void addParameterToGridPane(Options generatorOptions, String parameterIdPrefix, GridPane gridPane,
			int i, ParameterDescription description, Map<String, Object> parameterValues) {
		Label parameterNameLabel = new Label(description.getLabel() + ":");
		Object value = null;
		if (parameterValues != null) {
			value = parameterValues.get(description.getId());
		}
		String desc = description.getDescription();
		if (desc != null && !desc.isEmpty()) {
			parameterNameLabel.setTooltip(new Tooltip(desc));
		}
		gridPane.add(parameterNameLabel, 0, i);
		ParameterType type = description.getType();
		Node node = null;
		switch (type) {
		case BOOLEAN:
			node = createBooleanParameter(generatorOptions, parameterIdPrefix, description, (Boolean) value);
			break;
		case ENUM:
			node = createEnumParameter(generatorOptions, parameterIdPrefix, description, (String) value);
			break;
		case FLOAT:
			node = createFloatParameter(generatorOptions, parameterIdPrefix, description, (Number) value);
			break;
		case INT:
			node = createIntParameter(generatorOptions, parameterIdPrefix, description, (Number) value);
			break;
		case HIERACHY:
			node = createHierarchyParameter(generatorOptions, gridPane, description, parameterValues);
			break;
		case STRING:
			node = createStringParameter(generatorOptions, parameterIdPrefix, description, (String) value);
			break;
		case COLOR:
			node = createColorParameter(generatorOptions, parameterIdPrefix, description, (Integer) value);
			break;
		case FILE:
			node = createFileParameter(generatorOptions, parameterIdPrefix, description, (String) value);
			break;
		case DIRECTORY:
			node = createDirParameter(generatorOptions, parameterIdPrefix, description, (String) value);
			break;
		case RADIO:
			node = createRadioButton(generatorOptions, parameterIdPrefix, description, (String) value);
			break;
		case OPTION:
		default:
			break;
		}
		if (node != null) {
			gridPane.add(node, 1, i);
		} else {
			gridPane.add(new Label("Parameter Type impl still missing"), 1, i);
		}
	}

	public static Node createEnumParameter(Options generatorOptions, String parameterIdPrefix,
			ParameterDescription description, String value) {
		EnumRange enumRange = (EnumRange) description.getParameterRange();
		if (enumRange != null) {
			ChoiceBox<EnumContainer> choice = new ChoiceBox<>();
			choice.setMinWidth(500);
			String[][] enums = enumRange.getEnums();
			List<EnumContainer> collect = Arrays.stream(enums).map(EnumContainer::new).collect(Collectors.toList());
			ObservableList<EnumContainer> observableArrayList = FXCollections.observableArrayList(collect);
			choice.setItems(observableArrayList);
			String filter = value == null ? enumRange.getDefaultValue() : value;
			if (filter != null) {
				Optional<EnumContainer> optDefault = collect.stream()
						.filter(enumContainer -> enumContainer.enumString.equals(filter)).findFirst();
				EnumContainer defaultValue = optDefault.get();
				choice.setValue(defaultValue);
				setParameter(generatorOptions, parameterIdPrefix, description.getId(), defaultValue.enumString);
			}
			choice.getSelectionModel().selectedItemProperty()
					.addListener((ChangeListener<EnumContainer>) (observable, oldValue, newValue) -> {
						setParameter(generatorOptions, parameterIdPrefix, description.getId(), newValue.enumString);
					});
			return choice;
		}
		return null;
	}

	private static class EnumContainer {
		private String enumString;
		private String label;

		public EnumContainer(String[] enumLabelTuple) {
			this.enumString = enumLabelTuple[0];
			this.label = enumLabelTuple[enumLabelTuple.length > 1 ? 1 : 0];
		}

		@Override
		public String toString() {
			return label;
		}
	}

	public static Node createStringParameter(Options generatorOptions, String parameterIdPrefix,
			ParameterDescription description, String value) {
		StringRange stringRange = (StringRange) description.getParameterRange();
		if (stringRange != null) {
			TextField textField = new TextField();
			textField.setMinWidth(500);
			String defaultValue = value == null ? stringRange.getDefaultValue() : value;
			String text = defaultValue != null ? defaultValue : "";
			setParameter(generatorOptions, parameterIdPrefix, description.getId(), text);
			textField.setText(text);
			textField.textProperty() //
					.addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
						setParameter(generatorOptions, parameterIdPrefix, description.getId(), newValue);
					});
			return textField;
		}
		return null;
	}

	public static Node createColorParameter(Options generatorOptions, String parameterIdPrefix,
			ParameterDescription description, Integer value) {
		ColorPicker colorPicker = new ColorPicker();
		colorPicker.setMinWidth(500);
		ColorRange colorRange = (ColorRange) description.getParameterRange();
		if (colorRange != null) {
			int defaultColor = value == null ? colorRange.getDefaultValue() : value;
			colorPicker.setValue(Color.rgb(defaultColor >> 16 & 0xFF, defaultColor >> 8 & 0xFF, defaultColor & 0xFF));
			setParameter(generatorOptions, parameterIdPrefix, description.getId(), defaultColor);
			colorPicker.valueProperty()//
					.addListener((ChangeListener<Color>) (observable, oldValue, newValue) -> {
						setParameter(generatorOptions, parameterIdPrefix, description.getId(), toRGBInt(newValue));
					});
			return colorPicker;
		}
		return null;
	}

	public static Node createHierarchyParameter(Options generatorOptions, GridPane gridPane,
			ParameterDescription description, Map<String, Object> parameterValues) {
		GridPane subGridPane = new GridPane();
		ColumnConstraints col0 = new ColumnConstraints();
		col0.setMinWidth(150);
		ColumnConstraints col1 = new ColumnConstraints();
		col1.fillWidthProperty().set(true);
		subGridPane.getColumnConstraints().addAll(col0, col1);
		@SuppressWarnings("unchecked")
		List<ParameterDescription> subParameterDescriptions = (List<ParameterDescription>) description
				.getParameterRange();
		int j = 0;
		for (ParameterDescription subParameterDescription : subParameterDescriptions) {
			addParameterToGridPane(generatorOptions, description.getId(), gridPane, j, subParameterDescription, null);
			j++;
		}
		return subGridPane;
	}

	public static Node createFloatParameter(Options generatorOptions, String parameterIdPrefix,
			ParameterDescription description, Number value) {
		FloatRange floatRange = (FloatRange) description.getParameterRange();
		double defaultValue = value == null ? floatRange.getDefaultValue() : value.doubleValue();
		Slider sliderFloat = new Slider(floatRange.getMin(), floatRange.getMax(), defaultValue);
		sliderFloat.setMinWidth(500);
//		if (floatRange.getMin() == 0) {
//			sliderFloat.setShowTickMarks(true);
//			double mayorTick = 0.01;
//			while (floatRange.getMax() > mayorTick * 10) {
//				mayorTick *= 10;
//			}
//			sliderFloat.setMajorTickUnit(mayorTick);
//		} else {
//			sliderFloat.setMajorTickUnit(floatRange.getMax() - floatRange.getMin());
//		}
		double range = sliderFloat.getMax() - sliderFloat.getMin();
		double mayorTick = 0.01;
		while (range > mayorTick * 10 && Math.abs(sliderFloat.getMin() %  (mayorTick*10))< 0.01) {
			mayorTick *= 10;
		}

		if (range / mayorTick < 200) {
			sliderFloat.setShowTickMarks(true);
			sliderFloat.setMajorTickUnit(mayorTick);
		} else {
			sliderFloat.setMajorTickUnit(range); // NO TICKS
		}
		
		sliderFloat.setShowTickLabels(true);
		if (floatRange.getIncrement() != null) {
			sliderFloat.setMajorTickUnit(floatRange.getIncrement());
		}
		setParameter(generatorOptions, parameterIdPrefix, description.getId(), defaultValue);
		Label sliderLabelFloat = new Label();
		sliderLabelFloat.setMinWidth(50);
		sliderLabelFloat.setAlignment(Pos.CENTER_RIGHT);
		sliderLabelFloat.setMinWidth(70);
		sliderLabelFloat.setTextAlignment(TextAlignment.RIGHT);
		sliderLabelFloat.setText(String.format("%.2f", defaultValue));
		sliderFloat.valueProperty().addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> {
			setParameter(generatorOptions, parameterIdPrefix, description.getId(), newValue);
			sliderLabelFloat.setText(String.format("%.2f", newValue));
		});
		HBox hboxFloat = new HBox();
		hboxFloat.getChildren().addAll(sliderFloat, sliderLabelFloat);
		return hboxFloat;
	}

	public static Node createIntParameter(Options generatorOptions, String parameterIdPrefix,
			ParameterDescription description, Number value) {
		IntRange intRange = (IntRange) description.getParameterRange();

		long defaultValue = value == null ? intRange.getDefaultValue() : value.longValue();
		Slider slider = new Slider(intRange.getMin(), intRange.getMax(), defaultValue);
		slider.setMinWidth(500);
//			slider.setShowTickMarks(true);
		long range = intRange.getMax() - intRange.getMin();
		int mayorTick = 1;
		while (range > mayorTick * 10 && intRange.getMin() % (mayorTick*10) == 0) {
			mayorTick *= 10;
		}

		if (range / mayorTick < 50) {
			slider.setShowTickMarks(true);
			slider.setMajorTickUnit(mayorTick);
			slider.setSnapToTicks(mayorTick == 1);
		} else {
			slider.setMajorTickUnit(range); // NO TICKS
		}
		slider.setShowTickLabels(true);
		if (intRange.getIncrement() != null) {
			slider.setMajorTickUnit(intRange.getIncrement());
		}
		setParameter(generatorOptions, parameterIdPrefix, description.getId(), defaultValue);
		Label sliderLabel = new Label();
		sliderLabel.setMinWidth(70);
		sliderLabel.setAlignment(Pos.CENTER_RIGHT);
		sliderLabel.setText(String.format("%d", defaultValue));
		slider.valueProperty().addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> {
			setParameter(generatorOptions, parameterIdPrefix, description.getId(), newValue.intValue());
			sliderLabel.setText(String.format("%d", newValue.intValue()));
		});
		HBox hbox = new HBox();
		hbox.getChildren().addAll(slider, sliderLabel);
		return hbox;
	}

	public static Node createDirParameter(Options generatorOptions, String parameterIdPrefix,
			ParameterDescription description, String value) {
		DirectoryChooser fileChooser = new DirectoryChooser();
		fileChooser.setTitle("Open Resource File");

		TextField pathField = new TextField();
		pathField.setMinWidth(500);
		if (value != null) {
			pathField.setText(value);
		}
		Button button = new Button("...");
		button.setOnAction(ae -> {
			File file = fileChooser.showDialog(button.getScene().getWindow());
			if (file != null) {
				pathField.setText(file.getAbsolutePath());
			}
		});

		pathField.textProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
			setParameter(generatorOptions, parameterIdPrefix, description.getId(), newValue);
		});
		HBox hbox = new HBox();
		HBox.setHgrow(pathField, Priority.ALWAYS);
		hbox.getChildren().addAll(pathField, button);
		return hbox;
	}

	public static Node createFileParameter(Options generatorOptions, String parameterIdPrefix,
			ParameterDescription description, String value) {
		FileRange fileRange = (FileRange) description.getParameterRange();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		Map<String, String[]> filter = fileRange.getFilter();
		if (filter != null && !filter.isEmpty()) {
			filter.forEach((k, v) -> {
				fileChooser.getExtensionFilters().add(new ExtensionFilter(k, v));
			});
		}

		TextField pathField = new TextField();
		if (value != null) {
			pathField.setText(value);
		}
		Button button = new Button("...");
		button.setOnAction(ae -> {
			if (fileRange.getType() == Type.OPEN) {
				File file = fileChooser.showOpenDialog(button.getScene().getWindow());
				if (file != null) {
					pathField.setText(file.getAbsolutePath());
				}
			} else {
				File file = fileChooser.showSaveDialog(button.getScene().getWindow());
				if (file != null) {
					pathField.setText(file.getAbsolutePath());
				}
			}
		});

		pathField.textProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
			setParameter(generatorOptions, parameterIdPrefix, description.getId(), newValue);
		});
		HBox hbox = new HBox();
		HBox.setHgrow(pathField, Priority.ALWAYS);
		hbox.getChildren().addAll(pathField, button);
		return hbox;
	}

	public static Node createBooleanParameter(Options generatorOptions, String parameterIdPrefix,
			ParameterDescription description, Boolean value) {
		CheckBox checkBox = new CheckBox();
		BooleanRange booleanRange = (BooleanRange) description.getParameterRange();
		if (booleanRange != null) {
			boolean defaultValue = value == null ? booleanRange.isDefaultValue() : value;
			checkBox.setSelected(defaultValue);
			setParameter(generatorOptions, parameterIdPrefix, description.getId(), defaultValue);
		}
		checkBox.selectedProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
			setParameter(generatorOptions, parameterIdPrefix, description.getId(), newValue);
		});
		return checkBox;
	}

	public static int toRGBInt(Color color) {
		int a = (int) (color.getOpacity()*255);
		int r = (int) (color.getRed() * 255);
		int g = (int) (color.getGreen() * 255);
		int b = (int) (color.getBlue() * 255);
		return a << 24 | r << 16 | g << 8 | b;
	}

	public static void setParameter(Options generatorOptions, String parameterIdPrefix, String parameterId,
			Object newValue) {
		if (parameterIdPrefix == null) {
			generatorOptions.getParameter().put(parameterId, newValue);
		} else {
			generatorOptions.getParameter().put(parameterIdPrefix + "." + parameterId, newValue);
		}
	}

	// @author Sabine Hopf
	public static Node createRadioButton(Options generatorOptions, String parameterIdPrefix,
			ParameterDescription description, String value) {
		RadioRange radioRange = (RadioRange) description.getParameterRange();
		if (radioRange != null) {
			ToggleGroup group = new ToggleGroup();
			HBox hboxRadio = new HBox();
			String[] text = radioRange.getText();
			String[] images = radioRange.getImage();
			String defaultValue = value == null ? text[0] : value;
			for (int i = 0; i < text.length; i++) {
				RadioButton rButton = new RadioButton();
				rButton.setUserData(text[i]);
				if (images[i] != null) {
					ImageView imageView = new ImageView(
							Thread.currentThread().getContextClassLoader().getResource(images[i]).toString());
					rButton.setGraphic(imageView);
				}
				if (defaultValue.equals(text[i])) {
					rButton.setSelected(true);
				} else {
					rButton.setSelected(false);
				}
				rButton.setToggleGroup(group);
				hboxRadio.getChildren().add(rButton);
			}
			setParameter(generatorOptions, parameterIdPrefix, description.getId(), defaultValue);
			group.selectedToggleProperty().addListener((ChangeListener<Toggle>) (observable, oldValue, newValue) -> {
				setParameter(generatorOptions, parameterIdPrefix, description.getId(),
						group.getSelectedToggle().getUserData().toString());
			});
			return hboxRadio;
		}
		return null;
	}

}
