package de.fernunihagen.mci.group2.coopalgoart.api.services.desc.ranges;

import lombok.Value;

@Value
public class RadioRange {
    private String[] text;
    private String[] image;

    public RadioRange(String[] text, String basePath, String[] image) {
        this.text = text;
        this.image = image;
        for (int i = 0; i < image.length; i++) {
            image[i] = basePath + image[i];

        }
    }
}
