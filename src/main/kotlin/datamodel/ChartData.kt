package datamodel

import javafx.beans.property.*
import javafx.collections.ObservableList
import javafx.scene.chart.XYChart
import tornadofx.*

const val EXACT_SOLUTION_NAME = "Exact Solution"
const val EULER_METHOD_NAME = "Euler Method"
const val IMPROVED_EULER_METHOD_NAME = "Improved Euler Method"
const val RUNGE_KUTTA_METHOD_NAME = "Runge Kutta Method"

private const val X0_GIVEN = 0.0 // given in task description (variant 3)
private const val Y0_GIVEN = 1.0 // given in task description (variant 3)
private const val X_MAX_GIVEN = 7.0

private const val N_DEFAULT = 100 // default number of steps
private const val N_MAX_DEFAULT = 100
private const val Y_AXIS_MIN_DEFAULT = -10.0
private const val Y_AXIS_MAX_DEFAULT = 10.0

/**
 * Represents data that will be displayed on the chart (coordinate plane, grid)
 *  depending on data that comes from user's input
 * Note that x-axis and y-axis can represent values of any variables (x, N, etc.)
 *  x-axis == abscissa axis
 *  y-axis == ordinate axis
 *  while (x0, y0) is point on the graph where:
 *      abscissa axis is on x
 *      ordinate axis is on y
 */
abstract class ChartData(chartData: ChartData? = null) {
    val x0Property by lazy {
        SimpleDoubleProperty(this, "x0", X0_GIVEN)
    }
    protected var x0 by x0Property
        private set

    val y0Property by lazy {
        SimpleDoubleProperty(this, "y0", Y0_GIVEN)
    }
    protected var y0 by y0Property
        private set

    val NProperty by lazy {
        SimpleIntegerProperty(this, "N", N_DEFAULT)
    }
    protected var N by NProperty
        private set

    val XProperty by lazy {
        SimpleDoubleProperty(this, "X", X_MAX_GIVEN)
    }
    protected var X by XProperty
        private set

    val NMaxProperty by lazy {
        SimpleIntegerProperty(this, "NMax", N_MAX_DEFAULT)
    }
    private var NMax by NMaxProperty


    val isExactSolutionVisibleProperty by lazy {
        SimpleBooleanProperty(this, "isExactSolutionVisible", true)
    }
    protected var isExactSolutionVisible by isExactSolutionVisibleProperty
        private set

    val isEulerMethodVisibleProperty by lazy {
        SimpleBooleanProperty(this, "isEulerMethodVisible", true)
    }
    protected var isEulerMethodVisible by isEulerMethodVisibleProperty
        private set

    val isImprovedEulerMethodVisibleProperty by lazy {
        SimpleBooleanProperty(this, "isImprovedEulerMethodVisible", true)
    }
    protected var isImprovedEulerMethodVisible by isImprovedEulerMethodVisibleProperty
        private set

    val isRungeKuttaMethodVisibleProperty by lazy {
        SimpleBooleanProperty(this, "isRungeKuttaMethodVisible", true)
    }
    protected var isRungeKuttaMethodVisible by isRungeKuttaMethodVisibleProperty
        private set


    /** value of min boundary of x-axis */
    val xAxisMinValProperty by lazy {
        SimpleDoubleProperty(this, "xAxisMinVal", x0)
    }
    protected var xAxisMinVal by xAxisMinValProperty

    /** value of max boundary of x-axis */
    val xAxisMaxValProperty by lazy {
        SimpleDoubleProperty(this, "xAxisMaxVal", xAxisMinVal + 10.0)
    }
    protected var xAxisMaxVal by xAxisMaxValProperty

    /** value of min boundary of y-axis */
    val yAxisMinValProperty by lazy {
        SimpleDoubleProperty(this, "yAxisMinVal", Y_AXIS_MIN_DEFAULT)
    }
    private var yAxisMinVal by yAxisMinValProperty

    /** value of max boundary of y-axis */
    val yAxisMaxValProperty by lazy {
        SimpleDoubleProperty(this, "yAxisMaxVal", Y_AXIS_MAX_DEFAULT)
    }
    private var yAxisMaxVal by yAxisMaxValProperty


    /** grid step that will shown on the chart */
    val gridStepProperty: SimpleDoubleProperty by lazy {
        SimpleDoubleProperty(this, "gridStep", computeGridStep())
    }
    private var gridStep by gridStepProperty


    /** graphs that will shown on the grid */
    val graphsProperty: Property<ObservableList<XYChart.Series<Number, Number>>> by lazy {
        SimpleObjectProperty(this, "graphs", computeGraphs())
    }
    private var graphs by graphsProperty


    /** Calculates and updates @see gridStep */
    protected abstract fun computeGridStep(): Double

    /** Calculates and updates @see graphs */
    protected abstract fun computeGraphs(): ObservableList<XYChart.Series<Number, Number>>


    /** For updating graphs on display via model commit */
    fun updateGraphs() {
        gridStep = computeGridStep()
        graphs = computeGraphs()
    }


    init {
        // copy constructor if non-null instance is passed
        if (chartData != null)
            copyInit(chartData)

        // if x-axis max boundary becomes less than min boundary => revert such change
        xAxisMaxValProperty.addListener { _, old, new ->
            if (new.toDouble() <= xAxisMinVal)
                xAxisMaxVal = old.toDouble()
        }
    }


    protected fun setYAxisMinMaxVal(graphs: ObservableList<XYChart.Series<Number, Number>>) {
        var yMax = if (graphs.isEmpty()) Y_AXIS_MAX_DEFAULT else 0.0
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


    private fun copyInit(chartData: ChartData) {
        x0 = chartData.x0
        y0 = chartData.y0
        N = chartData.N
        X = chartData.X
        NMax = chartData.NMax
        isExactSolutionVisible = chartData.isExactSolutionVisible
        isEulerMethodVisible = chartData.isEulerMethodVisible
        isImprovedEulerMethodVisible = chartData.isImprovedEulerMethodVisible
        isRungeKuttaMethodVisible = chartData.isRungeKuttaMethodVisible
        xAxisMinVal = chartData.xAxisMinVal
        xAxisMaxVal = chartData.xAxisMaxVal
    }
}