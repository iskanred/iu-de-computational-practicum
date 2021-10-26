package computations.solutions

import computations.XBasedGraph
import datamodel.EXACT_SOLUTION_NAME

class ExactSolutionGraph(x0: Double, y0: Double, N: Int,
                         xAxisMinVal: Double, xAxisMaxVal: Double)
    : XBasedGraph(N, xAxisMinVal, xAxisMaxVal, x0, y0) {
    override val name: String = EXACT_SOLUTION_NAME

    override fun calculateYs(): DoubleArray {
        val ys = DoubleArray(xs.size)
        for (index in ys.indices) {
            ys[index] = getYValue(xs[index])
        }
        return ys
    }
}