package computations.solutions

import computations.XBasedGraph
import java.lang.IllegalArgumentException
import kotlin.math.*

abstract class ApproximationMethodGraph(x0: Double, protected val y0: Double, N: Int,
                                        xAxisMinVal: Double, xAxisMaxVal: Double, protected val gridStep: Double) :
    XBasedGraph(N, xAxisMinVal, xAxisMaxVal, gridStep)
{
    init {
        if (x0 !in (xAxisMinVal-0.0001)..(xAxisMinVal+0.0001))
            throw IllegalArgumentException("For approximation method graphs x0 must be equal to xAxisMinVal")
    }

    /* y' = f(x, y) = sec(x) - y * tan(x) */
    protected fun getFValue(x: Double, y: Double): Double = 1 / cos(x) - y * tan(x)
}


class EulerMethodGraph(x0: Double, y0: Double, N: Int,
                       xAxisMinVal: Double, xAxisMaxVal: Double, gridStep: Double)
    : ApproximationMethodGraph(x0, y0, N, xAxisMinVal, xAxisMaxVal, gridStep)
{
    override val name: String = "Euler Method"

    override fun calculateYs(): DoubleArray {
        val ys = DoubleArray(xs.size)
        ys[0] = y0

        for (index in 1 until ys.size) {
            // y_i = y_(i-1) + h * f(x_(i-1), y_(i-1))
            ys[index] = ys[index - 1] + gridStep * getFValue(xs[index - 1], ys[index - 1])
        }
        return ys
    }
}


class ImprovedEulerMethodGraph(x0: Double, y0: Double, N: Int,
                               xAxisMinVal: Double, xAxisMaxVal: Double, gridStep: Double)
    : ApproximationMethodGraph(x0, y0, N, xAxisMinVal, xAxisMaxVal, gridStep)
{
    override val name = "Improved Euler Method"

    override fun calculateYs(): DoubleArray {
        val ys = DoubleArray(xs.size)
        ys[0] = y0

        for (index in 1 until ys.size) {
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
                            xAxisMinVal: Double, xAxisMaxVal: Double, gridStep: Double)
    : ApproximationMethodGraph(x0, y0, N, xAxisMinVal, xAxisMaxVal, gridStep)
{
    override val name = "Runge Kutta Method"

    override fun calculateYs(): DoubleArray {
        val ys = DoubleArray(xs.size)
        ys[0] = y0

        for (index in 1 until ys.size) {
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