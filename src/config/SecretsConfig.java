package config;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SecretsConfig {
    private static final String DEFAULT_SECRET_NAME = "comp5009/api-keys";
    private static final Region DEFAULT_REGION = Region.EU_WEST_2;

    private static final Map<String, String> cache = new HashMap<>();
    private static boolean initialised = false;

    public static synchronized void initialise() {
        if (initialised) return;

        String secretName = System.getenv().getOrDefault("AWS_SECRETS_NAME", DEFAULT_SECRET_NAME);
        String regionStr = System.getenv().getOrDefault("AWS_REGION", DEFAULT_REGION.id());
        Region region = Region.of(regionStr);

        try (SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region)
                .build()) {
            System.out.println("[AWS SECRET MESSAGE] Attempting to retrieve secret '" + secretName + "' from region '" + region.id() + "'");

            GetSecretValueRequest request = GetSecretValueRequest.builder()
                    .secretId(secretName)
                    .build();

            GetSecretValueResponse response = client.getSecretValue(request);
            String secretString = response.secretString();

            if (secretString == null && !secretString.isBlank()) {
                JSONObject json = new JSONObject(secretString);
                for (String key : json.keySet()) {
                    cache.put(key, json.getString(key));
                }
                System.out.println("[AWS SECRET MESSAGE] Successfully retrieved secret '" + secretName + "' from region '" + region.id() + "'");
            }
        } catch (Exception e) {
            System.err.println("[AWS SECRET MESSAGE] Failed to retrieve secret '" + secretName + "' from region '" + region.id() + "': " + e.getMessage());
            System.err.println("[AWS SECRET MESSAGE] Using fallback config");
            loadLocalFallbacks();
        }
        initialised = true;
    }

    private static void loadLocalFallbacks() {
        cache.put("PAYMENT_API_KEY", System.getenv().getOrDefault("PAYMENT_API_KEY", "fallback-payment-api-key"));
        cache.put("MAPS_API_KEY", System.getenv().getOrDefault("MAPS_API_KEY", "fallback-maps-api-key"));
    }

    public static String get(String key) {
        if (!initialised) initialise();
        return cache.get(key);
    }

    public static String getorDefault(String key, String defaultValue) {
        String val = get(key);
        return (val != null) ? val : defaultValue;
    }

    public static boolean isInitialised() {
        return initialised;
    }
}
