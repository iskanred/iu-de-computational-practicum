package computations

import javafx.scene.chart.XYChart
import javafx.scene.layout.StackPane
import tornadofx.*

abstract class MathGraph(N: Int, xAxisMinVal: Double, gridStep: Double)
{
    /** Name of graph that will be displayed */
    abstract val name: String
    /** Indices of points in which y-value is undefined */
    private val exclusionPointsIndices by lazy { findExclusionPointsIndices() }

    protected val xs: DoubleArray = DoubleArray(N + 1)
    val ys: DoubleArray by lazy { calculateYs() }

    val series: XYChart.Series<Number, Number> by lazy {
        val points = xs.zip(ys).map { XYChart.Data<Number, Number>(it.first, it.second) }.asObservable()
        val series = XYChart.Series(name, points)
        markExclusionPoints(series)
        series
    }

    /** Fill xs */
    init {
        xs[0] = xAxisMinVal
        for (index in 1 until xs.size) {
            // x_i = x_(i-1) + h
            xs[index] = xs[index - 1] + gridStep
        }
    }


    /** Calculate function values of points that will be shown */
    protected abstract fun calculateYs(): DoubleArray
    /** Find indices exclusion points (in which y-value is undefined) */
    protected abstract fun findExclusionPointsIndices(): List<Int>


    private fun markExclusionPoints(series: XYChart.Series<Number, Number>) {
        series.nodeProperty().onChange { seriesNode ->
            if (seriesNode == null)
                return@onChange
            series.data.forEachIndexed { index, point ->
                // if index of point is not exclusion point index
                if (index !in exclusionPointsIndices) {
                    point.nodeProperty().onChange { pointNode ->
                        val stackPane = pointNode as? StackPane
                        stackPane?.isVisible = false // remove
                    }
                }
            }
        }
    }
}