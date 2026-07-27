package utils;

import lombok.Getter;

import java.util.concurrent.ThreadLocalRandom;

@Getter
public enum Domains {

    GMAIL("gmail.com"),
    YAHOO("yahoo.com"),
    YANDEX("yandex.kz");

    private final String domain;

    Domains(String domain) {
        this.domain = domain;
    }

    public static String getRandomDomain() {
        return Domains.values()[ThreadLocalRandom.current().nextInt(Domains.values().length)].getDomain();
    }
}
