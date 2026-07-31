package api.models;

import lombok.Getter;

import java.util.concurrent.ThreadLocalRandom;

@Getter
public enum Countries {

    ISRAEL("Israel"),
    INDIA("India"),
    USA("United States"),
    CANADA("Canada"),
    AUSTRALIA("Australia"),
    NEW_ZEALAND("New Zealand"),
    SINGAPORE("Singapore");

    private final String value;

    Countries(String value) {
        this.value = value;
    }

    public static String getRandomCountryValue() {
        return Countries.values()[ThreadLocalRandom.current().nextInt(Countries.values().length)].getValue();
    }
}
