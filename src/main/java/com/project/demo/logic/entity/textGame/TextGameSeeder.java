package com.project.demo.logic.entity.textGame;


import com.project.demo.logic.entity.game.Game;
import com.project.demo.logic.entity.game.GameRepository;
import com.project.demo.logic.entity.game.GameEnum;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Order(5)
@Component
public class TextGameSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final TextGameRepository textGameRepository;
    private final GameRepository gameRepository;

    public TextGameSeeder(TextGameRepository textGameRepository, GameRepository gameRepository) {
        this.textGameRepository = textGameRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.createTextGames();
    }

    private void createTextGames() {
        if (textGameRepository.count() > 0) return;

        Optional<Game> optionalGame = gameRepository.findByName(GameEnum.ECO_FILLER);
        Game game = optionalGame.orElse(null);

        List<TextGame> games = new ArrayList<>();

        games.add(new TextGame("El contenedor de color verde se usa para reciclar ______.", "vidrio", game));
        games.add(new TextGame("Reducir, reutilizar y ______ son las tres erres del reciclaje.", "reciclar", game));
        games.add(new TextGame("La energía que proviene del sol se llama energía ______.", "solar", game));
        games.add(new TextGame("Los residuos orgánicos deben ir al contenedor de color ______.", "marrón", game));
        games.add(new TextGame("Los océanos se contaminan principalmente con ______.", "plástico", game));
        games.add(new TextGame("Reciclar papel ayuda a salvar los ______.", "árboles", game));
        games.add(new TextGame("Los gases que provocan el calentamiento global se llaman gases de efecto ______.", "invernadero", game));
        games.add(new TextGame("Una bolsa de plástico puede tardar hasta ______ años en degradarse.", "400", game));
        games.add(new TextGame("Apagar las luces al salir de una habitación ayuda a ahorrar ______.", "energía", game));
        games.add(new TextGame("Plantar árboles contribuye a reducir el ______ en la atmósfera.", "CO2", game));

        games.add(new TextGame("La basura electrónica debe reciclarse en puntos ______.", "verdes", game));
        games.add(new TextGame("El contenedor azul se usa para desechar ______.", "papel", game));
        games.add(new TextGame("Los coches eléctricos ayudan a reducir la emisión de ______.", "gases", game));
        games.add(new TextGame("La capa de ______ protege la Tierra de los rayos UV.", "ozono", game));
        games.add(new TextGame("Un producto biodegradable se descompone de forma ______.", "natural", game));
        games.add(new TextGame("Usar bicicleta en vez de coche ayuda a reducir la ______.", "contaminación", game));
        games.add(new TextGame("Los envases de plástico deben colocarse en el contenedor de color ______.", "amarillo", game));
        games.add(new TextGame("El reciclaje permite reducir el uso de ______ naturales.", "recursos", game));
        games.add(new TextGame("El cambio ______ afecta al clima mundial.", "climático", game));
        games.add(new TextGame("El vidrio puede reciclarse un número ______ de veces.", "infinito", game));

        games.add(new TextGame("Los paneles ______ transforman la luz solar en electricidad.", "solares", game));
        games.add(new TextGame("El compostaje convierte residuos orgánicos en ______.", "abono", game));
        games.add(new TextGame("La energía ______ se genera con el viento.", "eólica", game));
        games.add(new TextGame("Los animales en peligro de extinción deben ser ______.", "protegidos", game));
        games.add(new TextGame("La sobrepesca amenaza la vida ______.", "marina", game));
        games.add(new TextGame("Las 3 R ayudan a disminuir la producción de ______.", "basura", game));
        games.add(new TextGame("Reciclar ayuda a combatir el ______ global.", "calentamiento", game));
        games.add(new TextGame("El plástico de un solo ______ debe evitarse.", "uso", game));
        games.add(new TextGame("El agua es un recurso ______ que debemos cuidar.", "vital", game));
        games.add(new TextGame("Cuidar el planeta es una responsabilidad de ______.", "todos", game));

        games.add(new TextGame("Los árboles producen el ______ que respiramos.", "oxígeno", game));
        games.add(new TextGame("El reciclaje contribuye al desarrollo ______.", "sostenible", game));
        games.add(new TextGame("Las especies se extinguen por la pérdida de su ______.", "hábitat", game));
        games.add(new TextGame("Los detergentes biodegradables contaminan ______.", "menos", game));
        games.add(new TextGame("El derroche de agua potable provoca ______.", "escasez", game));
        games.add(new TextGame("El transporte público genera menos ______.", "emisiones", game));
        games.add(new TextGame("El cartón reciclado ayuda a conservar los ______.", "bosques", game));
        games.add(new TextGame("Un planeta limpio es un planeta más ______.", "saludable", game));
        games.add(new TextGame("No tirar basura en la calle evita que se tapen las ______.", "alcantarillas", game));
        games.add(new TextGame("La reforestación ayuda a prevenir la ______.", "erosión", game));

        games.add(new TextGame("Cada persona produce al día varios kilos de ______.", "residuos", game));
        games.add(new TextGame("El vidrio reciclado necesita menos ______ para fundirse.", "energía", game));
        games.add(new TextGame("Una batería mal desechada puede contaminar el ______.", "suelo", game));
        games.add(new TextGame("El consumo responsable evita el ______ innecesario.", "desperdicio", game));
        games.add(new TextGame("La contaminación lumínica afecta a la vida ______.", "nocturna", game));
        games.add(new TextGame("La contaminación acústica afecta a la salud ______.", "mental", game));
        games.add(new TextGame("Los productos ecológicos respetan el medio ______.", "ambiente", game));
        games.add(new TextGame("Las energías limpias no generan ______.", "contaminación", game));
        games.add(new TextGame("Las abejas son esenciales para la ______ de cultivos.", "polinización", game));
        games.add(new TextGame("El consumo excesivo genera más ______ que recursos.", "desechos", game));

        games.forEach(textGameRepository::save);
    }
}
