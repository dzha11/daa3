package org.example;

import java.util.ArrayList;
import java.util.List;

public class MSTResult {
    public List<Edge> mstEdges = new ArrayList<>(); // рёбра, входящие в MST
    public long totalWeight = 0;                    // общая стоимость MST
    public int originalV;                           // количество вершин в исходном графе
    public int originalE;                           // количество рёбер
    public long operations = 0;                     // число операций
    public long comparisons = 0;                    // число сравнений
    public long unions = 0;                         // для Kruskal
    public long finds = 0;                          // для Kruskal
    public long timeMs = 0;                         // время выполнения (ms)
}
