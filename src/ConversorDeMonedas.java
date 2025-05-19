import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.util.Scanner;

public class ConversorDeMonedas {

    private static final String API_KEY = "7914d543c9ebdf04e15882d7";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/";


    private static final DecimalFormat df = new DecimalFormat("0.00");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bienvenido al Conversor de Monedas");
        System.out.println("1. Dólar USD a CNY Yuan Chino");
        System.out.println("2. Dólar USD a EUR Euro");
        System.out.println("3. Dólar USD a CLP Peso Chileno");
        System.out.println("4. Yuan Chino CNY a Dólar USD");
        System.out.println("5. Euro EUR  a Dólar USD");
        System.out.println("6. Peso chileno CLP a Dólar USD");
        System.out.println("7. Salir de la aplicación");

        while (true) {
            System.out.print("\nSeleccione una opción válida por favor (1-7): ");
            int opcion = scanner.nextInt();

            if (opcion == 7) {
                System.out.println("Gracias por usar el conversor. ¡Hasta la próxima!");
                break;
            }

            if (opcion < 1 || opcion > 6) {
                System.out.println("Opción no válida. Por favor intente nuevamente.");
                continue;
            }

            System.out.print("Ingrese la cantidad a convertir: ");
            double cantidad = scanner.nextDouble();

            try {
                double resultado = convertirMoneda(opcion, cantidad);
                String monedaOrigen = "";
                String monedaDestino = "";

                switch (opcion) {
                    case 1:
                        monedaOrigen = "USD";
                        monedaDestino = "CNY";
                        break;
                    case 2:
                        monedaOrigen = "USD";
                        monedaDestino = "EUR";
                        break;
                    case 3:
                        monedaOrigen = "USD";
                        monedaDestino = "CLP";
                        break;
                    case 4:
                        monedaOrigen = "CNY";
                        monedaDestino = "USD";
                        break;
                    case 5:
                        monedaOrigen = "EUR";
                        monedaDestino = "USD";
                        break;
                    case 6:
                        monedaOrigen = "CLP";
                        monedaDestino = "USD";
                        break;
                }

                System.out.println("\n" + cantidad + " " + monedaOrigen + " = " +
                        df.format(resultado) + " " + monedaDestino);

            } catch (IOException | InterruptedException e) {
                System.out.println("Error al realizar la conversión: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static double convertirMoneda(int opcion, double cantidad) throws IOException, InterruptedException {
        String fromCurrency = "";
        String toCurrency = "";

        switch (opcion) {
            case 1:
                fromCurrency = "USD";
                toCurrency = "CNY";
                break;
            case 2:
                fromCurrency = "USD";
                toCurrency = "EUR";
                break;
            case 3:
                fromCurrency = "USD";
                toCurrency = "CLP";
                break;
            case 4:
                fromCurrency = "CNY";
                toCurrency = "USD";
                break;
            case 5:
                fromCurrency = "EUR";
                toCurrency = "USD";
                break;
            case 6:
                fromCurrency = "CLP";
                toCurrency = "USD";
                break;
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + fromCurrency + "/" + toCurrency + "/" + cantidad))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        Gson gson = new Gson();
        ExchangeResponse exchangeResponse = gson.fromJson(response.body(), ExchangeResponse.class);

        if (exchangeResponse.result.equals("success")) {
            return exchangeResponse.conversion_result;
        } else {
            throw new IOException("Error en la API: " + exchangeResponse.result);
        }
    }


    private static class ExchangeResponse {
        String result;
        double conversion_result;
    }
}


