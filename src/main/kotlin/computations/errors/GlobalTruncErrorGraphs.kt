package computations.errors

import computations.NBasedGraph
import datamodel.EULER_METHOD_NAME
import datamodel.IMPROVED_EULER_METHOD_NAME
import datamodel.RUNGE_KUTTA_METHOD_NAME

/**
 * Global truncation error graph
 * Note that x-Axis is now N-Axis
 *
 * @constructor
 * @param xMax is a value of x to which global error will be calculated for each solution method
 */
abstract class GlobalTruncErrorGraph(protected val x0: Double, protected val y0: Double,
                                     nAxisMinVal: Int, nAxisMaxVal: Int,
                                     protected val xMax: Double)
    : NBasedGraph(nAxisMinVal, nAxisMaxVal)
{
    /** @return graph of local truncation error of specific type defined by N grid steps */
    abstract fun getLocalTruncErrorGraph(N: Int): LocalTruncErrorGraph

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
                                 nAxisMaxVal: Int, xMax: Double)
    : GlobalTruncErrorGraph(x0, y0, nAxisMinVal, nAxisMaxVal, xMax)
{
    override val name: String = EULER_METHOD_NAME
    override fun getLocalTruncErrorGraph(N: Int): LocalTruncErrorGraph =
        EulerLocalTruncErrorGraph(x0, y0, N, x0, xMax)
}

class ImprovedEulerGlobalTruncErrorGraph(x0: Double, y0: Double, nAxisMinVal: Int,
                                         nAxisMaxVal: Int, xMax: Double)
    : GlobalTruncErrorGraph(x0, y0, nAxisMinVal, nAxisMaxVal, xMax)
{
    override val name: String = IMPROVED_EULER_METHOD_NAME
    override fun getLocalTruncErrorGraph(N: Int): LocalTruncErrorGraph =
        ImprovedEulerLocalTruncErrorGraph(x0, y0, N, x0, xMax)
}

class RungeKuttaGlobalTruncErrorGraph(x0: Double, y0: Double, nAxisMinVal: Int,
                                      nAxisMaxVal: Int, xMax: Double)
    : GlobalTruncErrorGraph(x0, y0, nAxisMinVal, nAxisMaxVal, xMax)
{
    override val name: String = RUNGE_KUTTA_METHOD_NAME
    override fun getLocalTruncErrorGraph(N: Int): LocalTruncErrorGraph =
        RungeKuttaLocalTruncErrorGraph(x0, y0, N, x0, xMax)
}