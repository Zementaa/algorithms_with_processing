package de.fernunihagen.mci.group2.coopalgoart.api.services.desc;

import java.util.Collections;

import de.fernunihagen.mci.group2.coopalgoart.api.services.desc.ranges.*;
import de.fernunihagen.mci.group2.coopalgoart.api.services.desc.ranges.FileRange.Type;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author bwinzen
 *
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class ParameterDescription {
	private String id;
	private String label;
	private boolean mandatory;
	private ParameterType type;
	private Object parameterRange;

	private String description; //="Hier könnte ihr text stehen";
	/**
	 * @param id
	 * @param label
	 * @param mandatory
	 * @param type
	 * @param parameterRange
	 */
	public ParameterDescription(String id, String label, boolean mandatory, ParameterType type, Object parameterRange) {
		this.id = id;
		this.label = label;
		this.mandatory = mandatory;
		this.type = type;
		this.parameterRange = parameterRange;
	}

	public static ParameterDescription createEnumDescription(String id, String label, String defaultValue,
			String[] enums) {
		String[][] enumsWithLabel = new String[enums.length][1];
		for (int i = 0; i < enumsWithLabel.length; i++) {
			enumsWithLabel[i] = new String[] { enums[i] };
		}
		return createEnumDescription(id, label, defaultValue, enumsWithLabel);
	}

	public static ParameterDescription createEnumDescription(String id, String label, String defaultValue,
			String[][] enumsWithLabel) {
		return new ParameterDescription(id, label, true, ParameterType.ENUM,
				new EnumRange(defaultValue, enumsWithLabel));
	}

	public static ParameterDescription createString(String id, String label, boolean mandatory, String defaultValue,
			String regex) {
		return new ParameterDescription(id, label, mandatory, ParameterType.STRING,
				new StringRange(defaultValue, regex));
	}

	public static ParameterDescription createColor(String id, String label, int defaultColor) {
		return new ParameterDescription(id, label, true, ParameterType.COLOR, new ColorRange(defaultColor));
	}

	public static ParameterDescription createInteger(String id, String label, long min, long max, long defaulValue) {
		return new ParameterDescription(id, label, true, ParameterType.INT, new IntRange(defaulValue, min, max, null));
	}

	public static ParameterDescription createFloat(String id, String label, double min, double max,
			double defaulValue) {
		return new ParameterDescription(id, label, true, ParameterType.FLOAT,
				new FloatRange(defaulValue, min, max, null));
	}

	public static ParameterDescription createFile(String id, String label, boolean mandatory, String filterDesc,
			String... fileSuffixes) {
		return new ParameterDescription(id, label, mandatory, ParameterType.FILE,
				new FileRange(Collections.singletonMap(filterDesc, fileSuffixes), Type.OPEN));
	}

	public static ParameterDescription createSaveFile(String id, String label, boolean mandatory, String filterDesc,
			String... fileSuffixes) {
		return new ParameterDescription(id, label, mandatory, ParameterType.FILE,
				new FileRange(Collections.singletonMap(filterDesc, fileSuffixes), Type.SAVE));
	}

	public static ParameterDescription createDirectory(String id, String label, boolean mandatory) {
		return new ParameterDescription(id, label, mandatory, ParameterType.DIRECTORY, null);
	}

	public static ParameterDescription createClearScreen(boolean defaultValue) {
		return new ParameterDescription("clearScreen", "clearScreen", true, ParameterType.BOOLEAN,
				new BooleanRange(defaultValue));
	}

	public static ParameterDescription createCheckBox(String id, String label, boolean defaultValue) {
		return new ParameterDescription(id, label, true, ParameterType.BOOLEAN, new BooleanRange(defaultValue));
	}

	public static ParameterDescription createRadioButton(String id, String label, String[] text, String[] image) {
		return new ParameterDescription(id, label, true, ParameterType.RADIO, new RadioRange(text, "", image));
	}

	public static ParameterDescription createRadioButton(String id, String label, String[] text, String basePath,
			String[] image) {
		return new ParameterDescription(id, label, true, ParameterType.RADIO, new RadioRange(text, basePath, image));
	}

}
