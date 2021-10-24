package datamodel

import javafx.beans.property.*
import javafx.collections.ObservableList
import javafx.scene.chart.XYChart
import tornadofx.*

private const val X0_GIVEN = 0.0 // given in task description (variant 3)
private const val Y0_GIVEN = 1.0 // given in task description (variant 3)
private const val X_MAX_GIVEN = 7.0

private const val N_DEFAULT = 100 // default number of steps
private const val Y_AXIS_MIN_DEFAULT = -10.0
private const val Y_AXIS_MAX_DEFAULT = 10.0


/**
 * Represents data that will be displayed on the chart (coordinate plane, grid)
 */
abstract class ChartData(chartData: ChartData? = null) {
    val x0Property by lazy { SimpleDoubleProperty(this, "x0", X0_GIVEN) }
    protected var x0 by x0Property
        private set

    val y0Property by lazy { SimpleDoubleProperty(this, "y0", Y0_GIVEN) }
    protected var y0 by y0Property
        private set

    val NProperty by lazy { SimpleIntegerProperty(this, "N", N_DEFAULT) }
    protected var N by NProperty
        private set


    val isExactSolutionVisibleProperty by lazy { SimpleBooleanProperty(this, "isExactSolutionVisible", true) }
    protected var isExactSolutionVisible by isExactSolutionVisibleProperty
        private set

    val isEulerMethodVisibleProperty by lazy { SimpleBooleanProperty(this, "isEulerMethodVisible", true) }
    protected var isEulerMethodVisible by isEulerMethodVisibleProperty
        private set

    val isImprovedEulerMethodVisibleProperty by lazy { SimpleBooleanProperty(this, "isImprovedEulerMethodVisible", true) }
    protected var isImprovedEulerMethodVisible by isImprovedEulerMethodVisibleProperty
        private set

    val isRungeKuttaMethodVisibleProperty by lazy { SimpleBooleanProperty(this, "isRungeKuttaMethodVisible", true) }
    protected var isRungeKuttaMethodVisible by isRungeKuttaMethodVisibleProperty
        private set


    /** min border value of x-axis (it is always equal to x0 since
     *  approximation methods can be computed only from this point */
    val xAxisMinValProperty = x0Property
    protected var xAxisMinVal by xAxisMinValProperty
        private set

    /** max border value of x-axis */
    val xAxisMaxValProperty by lazy { SimpleDoubleProperty(this, "xAxisMaxVal", x0 + 10.0) }
    protected var xAxisMaxVal by xAxisMaxValProperty
        private set

    /** min border value of y-axis */
    val yAxisMinValProperty by lazy { SimpleDoubleProperty(this, "yAxisMinVal", Y_AXIS_MIN_DEFAULT) }
    private var yAxisMinVal by yAxisMinValProperty

    /** max border value of y-axis */
    val yAxisMaxValProperty by lazy { SimpleDoubleProperty(this, "yAxisMaxVal", Y_AXIS_MAX_DEFAULT) }
    private var yAxisMaxVal by yAxisMaxValProperty


    /** value of x to which global error will be calculated for each solution method */
    val xMaxForGlobalErrorProperty by lazy { SimpleDoubleProperty(this, "xMaxForGlobalError", X_MAX_GIVEN) }
    protected var xMaxForGlobalError by xMaxForGlobalErrorProperty
        private set


    /** graphs that will shown on the grid */
    val graphsProperty: Property<ObservableList<XYChart.Series<Number, Number>>> by lazy {
        SimpleObjectProperty(this, "graphs", computeGraphs(computeGridStep()))
    }



    /** Calculates and updates @see gridStep */
    open fun computeGridStep(): Double {
        return (xAxisMaxVal - xAxisMinVal) / N
    }

    /** Calculates and updates @see graphs */
    abstract fun computeGraphs(gridStep: Double): ObservableList<XYChart.Series<Number, Number>>



    init {
        if (chartData != null) {
            x0 = chartData.x0
            y0 = chartData.y0
            N = chartData.N
            isExactSolutionVisible = chartData.isExactSolutionVisible
            isEulerMethodVisible = chartData.isEulerMethodVisible
            isImprovedEulerMethodVisible = chartData.isImprovedEulerMethodVisible
            isRungeKuttaMethodVisible = chartData.isRungeKuttaMethodVisible
            xAxisMinVal = chartData.xAxisMinVal
            xAxisMaxVal = chartData.xAxisMaxVal
            xMaxForGlobalError = chartData.xMaxForGlobalError
        }

        // if x_max cannot becomes less than x_min => revert such change
        xAxisMaxValProperty.addListener { _, old, new ->
            if (new.toDouble() <= xAxisMinVal)
                xAxisMaxVal = old.toDouble()
        }

        // is x_min becomes greater than x_max => update x_max
        xAxisMinValProperty.onChange {
            if (it >= xAxisMaxVal)
                xAxisMaxVal = it + 10.0
        }
    }


    protected fun setYAxisMinMaxVal(graphs: ObservableList<XYChart.Series<Number, Number>>) {
        var yMax = Y_AXIS_MAX_DEFAULT
        var yMin = if (graphs.isEmpty()) Y_AXIS_MIN_DEFAULT else 0.0

        graphs.forEach {  series ->
            var maxYOfSeries = 0.0
            var minYOfSeries = 0.0

            series.data.forEach {
                val y = it.yValue.toDouble()
                maxYOfSeries = if (y > maxYOfSeries) y else maxYOfSeries
                minYOfSeries = if (y < minYOfSeries) y else minYOfSeries
            }

            yMax = if (maxYOfSeries > yMax) maxYOfSeries else yMax
            yMin = if (minYOfSeries < yMin) minYOfSeries else yMin
        }

        yAxisMaxVal = yMax
        yAxisMinVal = yMin
    }
}