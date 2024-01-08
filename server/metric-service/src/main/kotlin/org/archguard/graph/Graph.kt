package org.archguard.graph

data class Graph(val nodes: List<Node>, val edges: List<Edge>)
data class Edge(val a: String, val b: String, var num: Int)
