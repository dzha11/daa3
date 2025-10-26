package org.example;

import java.util.Arrays;

public class PrimMST {

    public static MSTResult run(Graph g) {
        MSTResult res = new MSTResult();
        res.originalV = g.V;
        res.originalE = g.edges.size();

        long ops = 0;
        long comps = 0;
        long start = System.currentTimeMillis();

        int V = g.V;
        boolean[] used = new boolean[V];
        int[] minEdge = new int[V];
        int[] selEdge = new int[V];
        Arrays.fill(minEdge, Integer.MAX_VALUE);
        Arrays.fill(selEdge, -1);

        // начинаем с вершины 0
        minEdge[0] = 0;

        for (int i = 0; i < V; i++) {
            int v = -1;
            for (int j = 0; j < V; j++) {
                comps++;
                if (!used[j] && (v == -1 || minEdge[j] < minEdge[v])) v = j;
            }

            if (v == -1 || minEdge[v] == Integer.MAX_VALUE)
                break; // граф несвязный

            used[v] = true;
            ops++;

            // если не первая вершина, добавляем ребро в результат
            if (selEdge[v] != -1) {
                Edge e = new Edge(v, selEdge[v], minEdge[v]);
                res.mstEdges.add(e);
                res.totalWeight += e.w;
            }

            // обновляем минимальные рёбра
            for (Edge e : g.adj.get(v)) {
                int to = (e.u == v ? e.v : e.u);
                comps++;
                if (!used[to] && e.w < minEdge[to]) {
                    minEdge[to] = e.w;
                    selEdge[to] = v;
                    ops++;
                }
            }
        }

        res.operations = ops;
        res.comparisons = comps;
        res.timeMs = System.currentTimeMillis() - start;
        return res;
    }
}
