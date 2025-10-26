package org.example;

public class Edge {
    public final int u; // первая вершина
    public final int v; // вторая вершина
    public final int w; // вес (стоимость дороги)

    public Edge(int u, int v, int w) {
        this.u = u;
        this.v = v;
        this.w = w;
    }

    @Override
    public String toString() {
        return String.format("(%d-%d: %d)", u, v, w);
    }
}
