package me.xtrm.paladium.palatest.client.ui.font;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class FontData {
    @SerializedName("fontName")
    private final String fontName;

    @SerializedName("baseSize")
    private final int baseSize;

    public FontData resize(int size) {
        if (size == -1) {
            return this;
        }
        return new FontData(fontName, size);
    }
}
