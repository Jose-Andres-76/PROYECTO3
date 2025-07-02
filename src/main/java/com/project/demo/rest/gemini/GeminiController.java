package com.project.demo.rest.gemini;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/gemini")
public class GeminiController {

    private final GeminiConsole geminiConsole;
    private final Map<String, StringBuilder> conversaciones = new HashMap<>();

    public GeminiController(GeminiConsole geminiConsole) {
        this.geminiConsole = geminiConsole;
    }

    @PostMapping("/conversar")
    public String conversar(@RequestParam String producto, @RequestParam String mensaje) {
        StringBuilder contexto = conversaciones.computeIfAbsent(producto, k -> new StringBuilder());

        Map<String, String> descripciones = Map.of(
                "paper", "El papel es reciclable y debe ir en el contenedor azul.",
                "plastic", "El plástico es reciclable, pero algunos tipos requieren separación.",
                "glass", "El vidrio es reciclable, pero debe estar limpio y sin tapas.",
                "metal", "El metal es reciclable y se puede depositar en puntos específicos.",
                "cardboard", "El cartón es reciclable y debe ir en el contenedor azul.",
                "trash", "Este material no es reciclable y debe ir a la basura común."
        );

        if (contexto.length() == 0 && descripciones.containsKey(producto)) {
            contexto.append("Descripción del producto: ").append(descripciones.get(producto)).append("\n");
        }

        contexto.append("Usuario: ").append(mensaje).append("\n");

        String respuesta;
        try (Client client = Client.builder().apiKey(geminiConsole.getApiKey()).build()) {
            GenerateContentResponse response = client.models.generateContent(
                    "models/gemini-2.5-flash-preview-05-20",
                    contexto.toString(),
                    null
            );
            respuesta = response.text();
            contexto.append("Gemini: ").append(respuesta).append("\n");
        } catch (Exception e) {
            respuesta = "Error al comunicarse con Gemini: " + e.getMessage();
        }
        return respuesta;
    }
}