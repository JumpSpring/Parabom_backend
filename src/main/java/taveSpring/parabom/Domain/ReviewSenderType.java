package taveSpring.parabom.Domain;

import java.util.Arrays;

public enum ReviewSenderType {
    SELLER("판매자"),
    BUYER("구매자");

    private final String data;

    ReviewSenderType(String data) {
        this.data = data;
    }

    public static ReviewSenderType find(String senderType) {
        return Arrays.stream(values())
                .filter(type -> type.data.equals(senderType))
                .findFirst()
                .orElseThrow(()-> new IllegalArgumentException("판매자 또는 구매자만 가능합니다."));
    }
}
