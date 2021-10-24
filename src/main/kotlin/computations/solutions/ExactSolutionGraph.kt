package computations.solutions

import computations.XBasedGraph
import kotlin.math.*

class ExactSolutionGraph(x0: Double, y0: Double, N: Int,
                         xAxisMinVal: Double, xAxisMaxVal: Double, gridStep: Double)
    : XBasedGraph(N, xAxisMinVal, xAxisMaxVal, gridStep)
{
    override val name: String = "Exact Solution"
    private val constant = (y0 - sin(x0)) / cos(x0)

    override fun calculateYs(): DoubleArray {
        val ys = DoubleArray(xs.size)
        for (index in ys.indices) {
            ys[index] = getYValue(xs[index])
        }
        return ys
    }

    /* Exact Solution: y = sin(x) + C * cos(x) */
    private fun getYValue(x: Double): Double = sin(x) + constant * cos(x)
}