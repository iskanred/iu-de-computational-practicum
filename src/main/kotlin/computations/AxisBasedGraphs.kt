package computations

import kotlin.math.*

/** Graph which abscissa axis is X => undefined(exclusion) points appear */
abstract class XBasedGraph(N: Int, private val xAxisMinVal: Double, private val xAxisMaxVal: Double,
                           private val gridStep: Double)
    : MathGraph(N, xAxisMinVal, gridStep)
{
    final override fun findExclusionPointsIndices(): List<Int>
    {
        val list = mutableListOf<Int>()
        val periodMinVal = (xAxisMinVal / PI - 0.5).toInt()
        val periodMaxVal = (xAxisMaxVal / PI - 0.5).toInt()

        xs.forEachIndexed { index, x ->
            // if point is an exclusion => store index of such point
            if (isExclusionPoint(x, periodMinVal, periodMaxVal, gridStep))
                list.add(index)
        }
        return list
    }

    /**
     * Differential equation has exclusion points at (PI/2 + PI*n),
     *  where integer 'n' is a period value
     * @param x is checked if it is an excluding point
     * @return x is an excluding point or not
     */
    private fun isExclusionPoint(x: Double, periodMinVal: Int, periodMaxVal: Int, gridStep: Double): Boolean {
        for (n in periodMinVal..periodMaxVal) {
            if (x in (PI /2 + PI *n - gridStep)..(PI /2 + PI *n + gridStep))
                return true
        }
        return false
    }
}

abstract class NBasedGraph(nAxisMinVal: Int, nAxisMaxVal: Int, gridStep: Int)
    : MathGraph(nAxisMaxVal - nAxisMinVal, nAxisMinVal.toDouble(), gridStep.toDouble())
{
    /** N-axis has no exclusion points since N can be any natural number */
    final override fun findExclusionPointsIndices(): List<Int> = emptyList()
}