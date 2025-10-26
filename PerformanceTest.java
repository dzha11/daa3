package org.example;

import com.google.gson.*;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PerformanceTest {

    @Test
    public void testPerformanceForAllGraphs() throws Exception {
        // пути
        String inputPath = "src/main/resources/assign_3_input.json";
        String outputCsv = "output/tests.csv";

        Gson gson = new Gson();
        File inputFile = new File(inputPath);
        assertTrue(inputFile.exists(), "Файл assign_3_input.json должен существовать!");

        JsonObject root = gson.fromJson(new InputStreamReader(
                new FileInputStream(inputFile), StandardCharsets.UTF_8), JsonObject.class);

        JsonArray graphs = root.getAsJsonArray("graphs");
        assertNotNull(graphs, "В JSON должен быть массив 'graphs'");

        // создаём папку для вывода
        new File("output").mkdirs();

        StringBuilder csv = new StringBuilder();
        csv.append("id,V,E,PrimCost,PrimTime(ms),PrimOps,PrimComps,KruskalCost,KruskalTime(ms),KruskalOps,KruskalComps,KruskalUnions,KruskalFinds\n");

        // цикл по всем графам
        for (JsonElement el : graphs) {
            JsonObject gObj = el.getAsJsonObject();
            String id = gObj.get("id").getAsString();
            int V = gObj.get("vertices").getAsInt();
            Graph g = new Graph(V);

            JsonArray edges = gObj.getAsJsonArray("edges");
            for (JsonElement e : edges) {
                JsonObject eObj = e.getAsJsonObject();
                int u = eObj.get("u").getAsInt();
                int v = eObj.get("v").getAsInt();
                int w = eObj.get("w").getAsInt();
                g.addEdge(u, v, w);
            }

            // запуски алгоритмов
            MSTResult prim = PrimMST.run(g);
            MSTResult kruskal = KruskalMST.run(g);

            // проверки корректности
            assertEquals(prim.totalWeight, kruskal.totalWeight,
                    "MST стоимость должна совпадать для графа: " + id);

            if (prim.mstEdges.size() == g.V - 1)
                assertTrue(kruskal.mstEdges.size() == g.V - 1);

            assertTrue(prim.timeMs >= 0);
            assertTrue(kruskal.timeMs >= 0);

            // добавляем в CSV
            csv.append(String.format(Locale.ROOT,
                    "%s,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d\n",
                    id, V, g.edges.size(),
                    prim.totalWeight, prim.timeMs, prim.operations, prim.comparisons,
                    kruskal.totalWeight, kruskal.timeMs, kruskal.operations, kruskal.comparisons,
                    kruskal.unions, kruskal.finds));
        }

        // запись CSV
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(outputCsv), StandardCharsets.UTF_8)) {
            writer.write(csv.toString());
        }

        System.out.println(" Performance tests completed!");
        System.out.println(" Results saved to: " + outputCsv);
    }
}
