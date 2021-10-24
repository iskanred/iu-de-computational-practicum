package computations.errors

import computations.NBasedGraph

/**
 * Global truncation error graph
 * Note that x-Axis is now N-Axis
 *
 * @constructor
 * @param xMax is a value of x to which global error will be calculated for each solution method
 */
abstract class GlobalTruncErrorGraph(protected val x0: Double, protected val y0: Double,
                                     nAxisMinVal: Int, nAxisMaxVal: Int, nGridStep: Int,
                                     protected val xMax: Double)
    : NBasedGraph(nAxisMinVal, nAxisMaxVal, nGridStep)
{
    /** @return graph of local truncation error of specific type defined by N grid steps */
    abstract fun getLocalTruncErrorGraph(N: Int): LocalTruncErrorGraph

    protected fun getXGridStep(N: Int) = (xMax - x0) / N

    final override fun calculateYs(): DoubleArray {
        val ys = DoubleArray(xs.size)

        for (N in ys.indices) {
            val localError = getLocalTruncErrorGraph(N)
            val maxError = localError.ys.maxOrNull() ?: Double.NaN
            ys[N] = maxError
        }
        return ys
    }
}


class EulerGlobalTruncErrorGraph(x0: Double, y0: Double, nAxisMinVal: Int,
                                 nAxisMaxVal: Int, nGridStep: Int, xMax: Double)
    : GlobalTruncErrorGraph(x0, y0, nAxisMinVal, nAxisMaxVal, nGridStep, xMax)
{
    override val name: String = "Euler Method"
    override fun getLocalTruncErrorGraph(N: Int): LocalTruncErrorGraph =
        EulerLocalTruncErrorGraph(x0, y0, N, x0, xMax, getXGridStep(N))
}

class ImprovedEulerGlobalTruncErrorGraph(x0: Double, y0: Double, nAxisMinVal: Int,
                                         nAxisMaxVal: Int, nGridStep: Int, xMax: Double)
    : GlobalTruncErrorGraph(x0, y0, nAxisMinVal, nAxisMaxVal, nGridStep, xMax)
{
    override val name: String = "Improved Euler Method"
    override fun getLocalTruncErrorGraph(N: Int): LocalTruncErrorGraph =
        ImprovedEulerLocalTruncErrorGraph(x0, y0, N, x0, xMax, getXGridStep(N))
}

class RungeKuttaGlobalTruncErrorGraph(x0: Double, y0: Double, nAxisMinVal: Int,
                                      nAxisMaxVal: Int, nGridStep: Int, xMax: Double)
    : GlobalTruncErrorGraph(x0, y0, nAxisMinVal, nAxisMaxVal, nGridStep, xMax)
{
    override val name: String = "Runge Kutta Euler Method"
    override fun getLocalTruncErrorGraph(N: Int): LocalTruncErrorGraph =
        RungeKuttaLocalTruncErrorGraph(x0, y0, N, x0, xMax, getXGridStep(N))
}