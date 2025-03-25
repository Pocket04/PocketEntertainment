package app.wallet.model;

import lombok.Getter;

@Getter
public enum Currency {
        POCKET_TOKEN("PT", "/images/PocketToken.webp"),
        EURO("EUR", "https://img.freepik.com/free-vector/euro-sign-money-currency-symbol_53876-119565.jpg");

        private final String code;
        private final String url;
        Currency(String code, String url) {
                this.url = url;
                this.code = code;
        }
}
