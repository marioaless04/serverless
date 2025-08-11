package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.Map;

public class Handler implements RequestHandler<Map<String, Object>, String> {

    @Override
    public String handleRequest(Map<String, Object> input, Context context) {
        int a = (int) input.get("a");
        int b = (int) input.get("b");

        // Aquí se integraría con Akka, enviando la tarea al SupervisorActor y esperando respuesta
        // Por simplicidad, mostramos el resultado directo
        return String.format("{\"resultado\": %d}", (a + b));
    }
}
