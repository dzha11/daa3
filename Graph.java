package org.example;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    public final int V; // количество вершин (districts)
    public final List<Edge> edges = new ArrayList<>(); // все дороги
    public final List<List<Edge>> adj; // список смежности (для Прима)

    public Graph(int V) {
        this.V = V;
        adj = new ArrayList<>(V);
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
    }
    public void addEdge(int u, int v, int w) {  // добавление дороги (u <-> v)
        Edge e = new Edge(u, v, w);
        edges.add(e);
        adj.get(u).add(e);
        adj.get(v).add(e);
    }
}
