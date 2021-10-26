package computations.errors

import computations.XBasedGraph
import computations.solutions.*
import kotlin.math.abs

abstract class LocalTruncErrorGraph(x0: Double, y0: Double, N: Int, xAxisMinVal: Double, xAxisMaxVal: Double)
    : XBasedGraph(N, xAxisMinVal, xAxisMaxVal, x0, y0)
{
    abstract val approximation: ApproximationMethodGraph

    final override val name: String by lazy { approximation.name }
    private val exactSolution = ExactSolutionGraph(x0, y0, N, xAxisMinVal, xAxisMaxVal)

    final override fun calculateYs(): DoubleArray {
        val ys = DoubleArray(xs.size)
        for (index in ys.indices) {
            ys[index] = abs(exactSolution.ys[index] - approximation.ys[index])
        }
        return ys
    }
}


class EulerLocalTruncErrorGraph(x0: Double, y0: Double, N: Int, xAxisMinVal: Double, xAxisMaxVal: Double)
    : LocalTruncErrorGraph(x0, y0, N, xAxisMinVal, xAxisMaxVal)
{
    override val approximation = EulerMethodGraph(x0, y0, N, xAxisMinVal, xAxisMaxVal)
}

class ImprovedEulerLocalTruncErrorGraph(x0: Double, y0: Double, N: Int, xAxisMinVal: Double, xAxisMaxVal: Double)
    : LocalTruncErrorGraph(x0, y0, N, xAxisMinVal, xAxisMaxVal)
{
    override val approximation = ImprovedEulerMethodGraph(x0, y0, N, xAxisMinVal, xAxisMaxVal)
}

class RungeKuttaLocalTruncErrorGraph(x0: Double, y0: Double, N: Int, xAxisMinVal: Double, xAxisMaxVal: Double)
    : LocalTruncErrorGraph(x0, y0, N, xAxisMinVal, xAxisMaxVal)
{
    override val approximation = RungeKuttaMethodGraph(x0, y0, N, xAxisMinVal, xAxisMaxVal)
}