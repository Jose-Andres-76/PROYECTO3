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
    private final Map<String, StringBuilder> conversations = new HashMap<>();

    public GeminiController(GeminiConsole geminiConsole) {
        this.geminiConsole = geminiConsole;
    }

    @PostMapping("/chat")
    public String chat(@RequestParam String product, @RequestParam String message) {
        if (product == null || product.trim().isEmpty()) {
            String context = buildGeneralContext(message);
            return generateResponse(context);
        }

        StringBuilder context = conversations.computeIfAbsent(product, k -> new StringBuilder());

        if (context.length() == 0) {
            context.append(buildProductContext(product));
        }

        context.append("Usuario: ").append(message).append("\n");

        String response = generateResponse(context.toString());
        context.append("Gemini: ").append(response).append("\n");

        return response;
    }

    private String buildGeneralContext(String message) {
        return "INSTRUCCIONES ESTRICTAS: Responde ÚNICAMENTE con 2 párrafos cortos. Máximo 3 oraciones por párrafo. " +
                "Sé directo, práctico y conciso. No uses listas, numeraciones ni explicaciones largas.\n\n" +
                "Eres un asistente de medio ambiente especializado en reciclaje. " +
                "Tu respuesta debe ser útil pero breve.\n\n" +
                "Pregunta del usuario: " + message + "\n\n" +
                "Respuesta (máximo 2 párrafos cortos):";
    }

    private String buildProductContext(String product) {
        Map<String, String> descriptions = Map.of(
                "paper", "MATERIAL: Papel. Es reciclable y va en contenedor azul. Debe estar limpio y seco.",
                "plastic", "MATERIAL: Plástico. Generalmente reciclable, revisa el número en el envase. Va en contenedor amarillo.",
                "glass", "MATERIAL: Vidrio. Totalmente reciclable, debe estar limpio sin tapas. Va en contenedor verde.",
                "metal", "MATERIAL: Metal/Lata. Altamente reciclable. Vacía y enjuaga antes de depositar.",
                "cardboard", "MATERIAL: Cartón. Reciclable, debe estar seco y desdoblado. Va en contenedor azul.",
                "trash", "MATERIAL: Residuo no reciclable. Debe ir a basura común."
        );

        String materialDesc = descriptions.getOrDefault(product.toLowerCase(),
                "MATERIAL: " + product + ". Consulta las normas locales de reciclaje.");

        return "INSTRUCCIONES ESTRICTAS: Responde ÚNICAMENTE con 2 párrafos cortos. Máximo 3 oraciones por párrafo. " +
                "Sé directo, práctico y conciso. No uses listas, numeraciones ni explicaciones largas.\n\n" +
                materialDesc + "\n\n" +
                "Contexto de la conversación:\n";
    }

    private String generateResponse(String context) {
        try (Client client = Client.builder().apiKey(geminiConsole.getApiKey()).build()) {
            GenerateContentResponse response = client.models.generateContent(
                    "models/gemini-2.5-flash-preview-05-20",
                    context + "\n\nRECUERDA: Solo 2 párrafos cortos, máximo 3 oraciones cada uno.",
                    null
            );

            String result = response.text().trim();

            return limitResponseLength(result);

        } catch (Exception e) {
            return "Error al consultar el asistente IA. Por favor, intenta nuevamente.";
        }
    }

    private String limitResponseLength(String response) {
        if (response.length() > 400) {
            String[] sentences = response.split("\\. ");
            StringBuilder shortResponse = new StringBuilder();
            int sentenceCount = 0;

            for (String sentence : sentences) {
                if (sentenceCount < 6 && shortResponse.length() < 350) {
                    shortResponse.append(sentence);
                    if (!sentence.endsWith(".")) {
                        shortResponse.append(".");
                    }
                    shortResponse.append(" ");
                    sentenceCount++;
                } else {
                    break;
                }
            }

            return shortResponse.toString().trim();
        }

        return response;
    }
}