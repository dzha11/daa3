package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class KruskalMST {

    // внутренняя структура DSU
    static class DSU {
        int[] parent;
        int[] rank;
        long unions = 0;
        long finds = 0;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;
        }

        int find(int x) {
            finds++;
            if (parent[x] == x) return x;
            parent[x] = find(parent[x]);
            return parent[x];
        }

        boolean union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a == b) return false;
            unions++;
            if (rank[a] < rank[b]) parent[a] = b;
            else if (rank[b] < rank[a]) parent[b] = a;
            else {
                parent[b] = a;
                rank[a]++;
            }
            return true;
        }
    }

    public static MSTResult run(Graph g) {
        MSTResult res = new MSTResult();
        res.originalV = g.V;
        res.originalE = g.edges.size();

        long start = System.currentTimeMillis();
        AtomicLong comparisons = new AtomicLong(0); // заменили long на атомарный счётчик
        long operations = 0;

        // сортируем рёбра по весу и считаем сравнения
        List<Edge> sorted = new ArrayList<>(g.edges);
        sorted.sort((a, b) -> {
            comparisons.incrementAndGet();
            return Integer.compare(a.w, b.w);
        });

        DSU dsu = new DSU(g.V);

        for (Edge e : sorted) {
            operations++;
            if (dsu.union(e.u, e.v)) {
                res.mstEdges.add(e);
                res.totalWeight += e.w;
            }
        }

        res.operations = operations;
        res.comparisons = comparisons.get();
        res.unions = dsu.unions;
        res.finds = dsu.finds;
        res.timeMs = System.currentTimeMillis() - start;

        return res;
    }
}
