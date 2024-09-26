import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ShamirSecretSharing {

    // Helper function to decode the value from a specific base
    public static long decodeValue(int base, String value) {
        return Long.parseLong(value, base);
    }

    // Function to perform Lagrange interpolation and find the constant term (c)
    public static double lagrangeInterpolation(List<double[]> points) {
        int k = points.size();
        double total = 0;

        // Lagrange interpolation formula to compute the constant term
        for (int i = 0; i < k; i++) {
            double xi = points.get(i)[0];
            double yi = points.get(i)[1];
            double term = yi;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    double xj = points.get(j)[0];
                    term *= (0 - xj) / (xi - xj); // Evaluating at x = 0 for constant term
                }
            }
            total += term;
        }

        return total;
    }

    // Main function to find the secret constant (c) using the input JSON data
    public static double findSecretConstant(JSONObject inputJson) {
        // Read n and k values from JSON
        JSONObject keys = inputJson.getJSONObject("keys");
        int n = keys.getInt("n");
        int k = keys.getInt("k");

        // List to store the points (x, y)
        List<double[]> points = new ArrayList<>();

        // Loop through and decode the provided points
        for (int i = 1; i <= n; i++) {
            String key = Integer.toString(i);
            if (inputJson.has(key)) {
                JSONObject root = inputJson.getJSONObject(key);
                int base = root.getInt("base");
                String value = root.getString("value");
                double xi = i;
                double yi = decodeValue(base, value);
                points.add(new double[]{xi, yi});
            }
        }

        // We need at least k points to solve the polynomial, so take the first k points
        points = points.subList(0, k);

        // Use Lagrange interpolation to find the constant term (c)
        return lagrangeInterpolation(points);
    }

    public static void main(String[] args) {
        // Sample Test Case 1
        String jsonString1 = "{\n" +
                "    \"keys\": {\n" +
                "        \"n\": 4,\n" +
                "        \"k\": 3\n" +
                "    },\n" +
                "    \"1\": {\n" +
                "        \"base\": \"10\",\n" +
                "        \"value\": \"4\"\n" +
                "    },\n" +
                "    \"2\": {\n" +
                "        \"base\": \"2\",\n" +
                "        \"value\": \"111\"\n" +
                "    },\n" +
                "    \"3\": {\n" +
                "        \"base\": \"10\",\n" +
                "        \"value\": \"12\"\n" +
                "    },\n" +
                "    \"6\": {\n" +
                "        \"base\": \"4\",\n" +
                "        \"value\": \"213\"\n" +
                "    }\n" +
                "}";
        
        // Sample Test Case 2
        String jsonString2 = "{\n" +
                "    \"keys\": {\n" +
                "        \"n\": 9,\n" +
                "        \"k\": 6\n" +
                "    },\n" +
                "    \"1\": {\n" +
                "        \"base\": \"10\",\n" +
                "        \"value\": \"28735619723837\"\n" +
                "    },\n" +
                "    \"2\": {\n" +
                "        \"base\": \"16\",\n" +
                "        \"value\": \"1A228867F0CA\"\n" +
                "    },\n" +
                "    \"3\": {\n" +
                "        \"base\": \"12\",\n" +
                "        \"value\": \"32811A4AA0B7B\"\n" +
                "    },\n" +
                "    \"4\": {\n" +
                "        \"base\": \"11\",\n" +
                "        \"value\": \"917978721331A\"\n" +
                "    },\n" +
                "    \"5\": {\n" +
                "        \"base\": \"16\",\n" +
                "        \"value\": \"1A22886782E1\"\n" +
                "    },\n" +
                "    \"6\": {\n" +
                "        \"base\": \"10\",\n" +
                "        \"value\": \"28735619654702\"\n" +
                "    },\n" +
                "    \"7\": {\n" +
                "        \"base\": \"14\",\n" +
                "        \"value\": \"71AB5070CC4B\"\n" +
                "    },\n" +
                "    \"8\": {\n" +
                "        \"base\": \"9\",\n" +
                "        \"value\": \"122662581541670\"\n" +
                "    },\n" +
                "    \"9\": {\n" +
                "        \"base\": \"8\",\n" +
                "        \"value\": \"642121030037605\"\n" +
                "    }\n" +
                "}";

        // Parse the JSON strings
        JSONObject inputJson1 = new JSONObject(jsonString1);
        JSONObject inputJson2 = new JSONObject(jsonString2);

        // Find the secret constant for both test cases
        double secret1 = findSecretConstant(inputJson1);
        double secret2 = findSecretConstant(inputJson2);

        // Print the results
        System.out.println("The secret constant (c) for test case 1 is: " + secret1);
        System.out.println("The secret constant (c) for test case 2 is: " + secret2);
    }
}
