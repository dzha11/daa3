package org.example;

import com.google.gson.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    static class GraphSpec {
        String id;
        int vertices;
        List<Map<String, Number>> edges;
    }

    public static void main(String[] args) throws Exception {
        // пути по умолчанию
        String inputPath = "src/main/resources/assign_3_input.json";
        String outputJson = "output/output.json";
        String outputCsv = "output/summary.csv";

        // можно передавать аргументы через консоль, если нужно
        if (args.length >= 1) inputPath = args[0];
        if (args.length >= 2) outputJson = args[1];
        if (args.length >= 3) outputCsv = args[2];

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File inputFile = new File(inputPath);
        if (!inputFile.exists()) {
            System.out.println("Input JSON not found: " + inputPath);
            return;
        }

        // читаем json
        JsonObject root = gson.fromJson(new InputStreamReader(
                new FileInputStream(inputFile), StandardCharsets.UTF_8), JsonObject.class);

        JsonArray graphs = root.getAsJsonArray("graphs");
        if (graphs == null) {
            System.out.println("No 'graphs' array found in input JSON");
            return;
        }

        JsonArray results = new JsonArray();
        StringBuilder csv = new StringBuilder();
        csv.append("id,V,E,prim_cost,prim_time_ms,prim_ops,prim_comps,kruskal_cost,kruskal_time_ms,kruskal_ops,kruskal_comps,kruskal_unions,kruskal_finds\n");

        // создаём папку для вывода
        new File("output").mkdirs();

        // проходим по каждому графу
        for (JsonElement ge : graphs) {
            JsonObject go = ge.getAsJsonObject();
            String id = go.get("id").getAsString();
            int V = go.get("vertices").getAsInt();
            Graph graph = new Graph(V);

            JsonArray edges = go.getAsJsonArray("edges");
            for (JsonElement e : edges) {
                JsonObject edgeObj = e.getAsJsonObject();
                int u = edgeObj.get("u").getAsInt();
                int v = edgeObj.get("v").getAsInt();
                int w = edgeObj.get("w").getAsInt();
                graph.addEdge(u, v, w);
            }

            // запуски алгоритмов
            MSTResult prim = PrimMST.run(graph);
            MSTResult kruskal = KruskalMST.run(graph);

            // json output
            JsonObject out = new JsonObject();
            out.addProperty("id", id);
            out.add("prim", gson.toJsonTree(prim));
            out.add("kruskal", gson.toJsonTree(kruskal));
            results.add(out);

            // csv output
            csv.append(String.format(Locale.ROOT,
                    "%s,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d\n",
                    id, V, graph.edges.size(),
                    prim.totalWeight, prim.timeMs, prim.operations, prim.comparisons,
                    kruskal.totalWeight, kruskal.timeMs, kruskal.operations, kruskal.comparisons,
                    kruskal.unions, kruskal.finds
            ));
        }

        // записываем результаты
        JsonObject finalOut = new JsonObject();
        finalOut.add("results", results);

        try (Writer w = new OutputStreamWriter(new FileOutputStream(outputJson), StandardCharsets.UTF_8)) {
            gson.toJson(finalOut, w);
        }
        try (Writer w = new OutputStreamWriter(new FileOutputStream(outputCsv), StandardCharsets.UTF_8)) {
            w.write(csv.toString());
        }

        System.out.println("Done! Results saved to:");
        System.out.println("   " + outputJson);
        System.out.println("   " + outputCsv);
    }
}
