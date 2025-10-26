package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MSTTest {

    @Test
    public void testPrimAndKruskalHaveSameCost() {
        // создаём небольшой тестовый граф
        Graph g = new Graph(5);
        g.addEdge(0, 1, 2);
        g.addEdge(0, 2, 3);
        g.addEdge(1, 2, 1);
        g.addEdge(1, 3, 4);
        g.addEdge(2, 3, 5);
        g.addEdge(3, 4, 7);
        g.addEdge(2, 4, 6);

        MSTResult prim = PrimMST.run(g);
        MSTResult kruskal = KruskalMST.run(g);

        //чекаем одинаковую стоимость MST
        assertEquals(prim.totalWeight, kruskal.totalWeight,
                "Prim и Kruskal должны иметь одинаковую общую стоимость MST");

        // чекаем количество рёбер в MST = V - 1
        assertEquals(g.V - 1, prim.mstEdges.size(), "Количество рёбер должно быть V-1");
        assertEquals(g.V - 1, kruskal.mstEdges.size(), "Количество рёбер должно быть V-1");

        // чекаем стоимость положительная
        assertTrue(prim.totalWeight > 0);
        assertTrue(kruskal.totalWeight > 0);

        //чекаем время выполнения неотрицательное
        assertTrue(prim.timeMs >= 0);
        assertTrue(kruskal.timeMs >= 0);

        // чекаем количество операций и сравнений неотрицательное
        assertTrue(prim.operations >= 0);
        assertTrue(kruskal.operations >= 0);
        assertTrue(prim.comparisons >= 0);
        assertTrue(kruskal.comparisons >= 0);
    }

    @Test
    public void testDisconnectedGraphHandledGracefully() {
        // граф из 3 вершин, но без связности
        Graph g = new Graph(3);
        g.addEdge(0, 1, 5); // вершина 2 изолирована

        MSTResult prim = PrimMST.run(g);
        MSTResult kruskal = KruskalMST.run(g);

        // MST не должен содержать V-1 рёбер
        assertNotEquals(g.V - 1, prim.mstEdges.size(),
                "Для несвязного графа Prim не должен строить полный MST");
        assertNotEquals(g.V - 1, kruskal.mstEdges.size(),
                "Для несвязного графа Kruskal не должен строить полный MST");

        // вес MST меньше, чем сумма всех рёбер
        assertTrue(prim.totalWeight <= 5);
        assertTrue(kruskal.totalWeight <= 5);
    }

    @Test
    public void testPerformanceNotNegative() {
        // средний граф
        Graph g = new Graph(8);
        g.addEdge(0, 1, 4);
        g.addEdge(0, 2, 8);
        g.addEdge(1, 2, 9);
        g.addEdge(1, 3, 10);
        g.addEdge(2, 4, 7);
        g.addEdge(3, 4, 3);
        g.addEdge(3, 5, 6);
        g.addEdge(4, 5, 5);
        g.addEdge(5, 6, 2);
        g.addEdge(6, 7, 1);

        MSTResult prim = PrimMST.run(g);
        MSTResult kruskal = KruskalMST.run(g);

        // чекаем что время не отрицательное
        assertTrue(prim.timeMs >= 0);
        assertTrue(kruskal.timeMs >= 0);

        // чекаем что количество сравнений и операций > 0
        assertTrue(prim.comparisons > 0);
        assertTrue(kruskal.comparisons > 0);
        assertTrue(prim.operations > 0);
        assertTrue(kruskal.operations > 0);
    }
}
