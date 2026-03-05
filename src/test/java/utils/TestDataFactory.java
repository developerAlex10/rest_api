package utils;

import com.github.javafaker.Faker;
import dto.common.Dlc;
import dto.common.Requirements;
import dto.common.SimilarDlc;
import dto.request.GameRequest;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

public class TestDataFactory {
    private static final Faker faker = new Faker(new Locale("en"));

    public static String uniqueLogin() {
        return faker.name().firstName() + System.currentTimeMillis();
    }

    public static String uniquePassword() {
        return "pwd" + System.currentTimeMillis();
    }

    private static String currentTimestamp() {
        return DateTimeFormatter.ISO_INSTANT.format(Instant.now().truncatedTo(ChronoUnit.MILLIS));
    }

    public static GameRequest randomGame() {
        boolean free = Math.random() < 0.5;
        return GameRequest.builder()
                .company(faker.company().name())
                .description(faker.lorem().sentence())
                .genre(faker.book().genre())
                .title(faker.book().title() + " " + System.currentTimeMillis())
                .free(free)
                .price(free ? 0.0 : 19.99)
                .requiredAge(false)
                .rating(5)
                .publish_date(currentTimestamp())
                .tags(List.of(faker.lorem().word()))
                .requirements(minimalRequirements())
                .dlcs(List.of())
                .build();
    }

    public static GameRequest minimalGame() {
        return GameRequest.builder()
                .company("MinimalCompany")
                .description("Minimal description")
                .genre("Indie")
                .title("MinimalGame_" + System.currentTimeMillis())
                .free(true)
                .requiredAge(false)
                .price(0.0)
                .rating(0)
                .publish_date(currentTimestamp())
                .tags(List.of("minimal"))
                .requirements(minimalRequirements())
                .dlcs(List.of())
                .build();
    }

    public static Requirements minimalRequirements() {
        return Requirements.builder()
                .hardDrive(10)
                .osName("Windows 10")
                .ramGb(8)
                .videoCard("Any")
                .build();
    }

    public static List<Dlc> updatedDlcs() {
        return List.of(Dlc.builder()
                .description("Premium DLC")
                .dlcName("Premium Pack")
                .dlcFree(false)
                .price(9.99)
                .rating(5)
                .similarDlc(SimilarDlc.builder()
                        .dlcNameFromAnotherGame("Similar DLC")
                        .free(false)
                        .build())
                .build());
    }
}