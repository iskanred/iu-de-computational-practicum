package computations.solutions

import computations.XBasedGraph
import datamodel.EULER_METHOD_NAME
import datamodel.IMPROVED_EULER_METHOD_NAME
import datamodel.RUNGE_KUTTA_METHOD_NAME
import java.lang.IllegalArgumentException
import kotlin.math.*

abstract class ApproximationMethodGraph(x0: Double, protected val y0: Double, N: Int,
                                        xAxisMinVal: Double, xAxisMaxVal: Double) :
    XBasedGraph(N, xAxisMinVal, xAxisMaxVal, x0, y0)
{
    init {
        if (x0 !in (xAxisMinVal - 0.000001)..(xAxisMinVal + 0.000001)) {
            throw IllegalArgumentException("For approximation method graphs x0 must be equal to xAxisMinVal")
        }
    }

    /* y' = f(x, y) = sec(x) - y * tan(x) */
    protected fun getFValue(x: Double, y: Double): Double = 1 / cos(x) - y * tan(x)
}


class EulerMethodGraph(x0: Double, y0: Double, N: Int,
                       xAxisMinVal: Double, xAxisMaxVal: Double)
    : ApproximationMethodGraph(x0, y0, N, xAxisMinVal, xAxisMaxVal)
{
    override val name: String = EULER_METHOD_NAME

    override fun calculateYs(): DoubleArray {
        val ys = DoubleArray(xs.size)
        ys[0] = y0

        for (index in 1 until ys.size) {
            if (index in exclusionPointsIndices) {
                ys[index] = getYValue(xs[index])
                continue
            }
            // y_i = y_(i-1) + h * f(x_(i-1), y_(i-1))
            ys[index] = ys[index - 1] + gridStep * getFValue(xs[index - 1], ys[index - 1])
        }
        return ys
    }
}


class ImprovedEulerMethodGraph(x0: Double, y0: Double, N: Int,
                               xAxisMinVal: Double, xAxisMaxVal: Double)
    : ApproximationMethodGraph(x0, y0, N, xAxisMinVal, xAxisMaxVal)
{
    override val name = IMPROVED_EULER_METHOD_NAME

    override fun calculateYs(): DoubleArray {
        val ys = DoubleArray(xs.size)
        ys[0] = y0

        for (index in 1 until ys.size) {
            if (index in exclusionPointsIndices) {
                ys[index] = getYValue(xs[index])
                continue
            }
            val x = xs[index - 1]
            val y = ys[index - 1]
            val h = gridStep

            val k1 = h * getFValue(x, y)
            val k2 = h * getFValue(x + h, y + k1)
            ys[index] = y + 0.5 * (k1 + k2)
        }
        return ys
    }
}


class RungeKuttaMethodGraph(x0: Double, y0: Double, N: Int,
                            xAxisMinVal: Double, xAxisMaxVal: Double)
    : ApproximationMethodGraph(x0, y0, N, xAxisMinVal, xAxisMaxVal)
{
    override val name = RUNGE_KUTTA_METHOD_NAME

    override fun calculateYs(): DoubleArray {
        val ys = DoubleArray(xs.size)
        ys[0] = y0

        for (index in 1 until ys.size) {
            if (index in exclusionPointsIndices) {
                ys[index] = getYValue(xs[index])
                continue
            }
            val x = xs[index - 1]
            val y = ys[index - 1]
            val h = gridStep

            val k1 = h * getFValue(x, y)
            val k2 = h * getFValue(x + 0.5 * h, y + 0.5 * k1)
            val k3 = h * getFValue(x + 0.5 * h, y + 0.5 * k2)
            val k4 = h * getFValue(x + h, y + k3)

            ys[index] = y + (k1 + 2 * k2 + 2 * k3 + k4) / 6
        }
        return ys
    }
}