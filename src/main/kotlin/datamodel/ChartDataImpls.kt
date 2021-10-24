package datamodel

import computations.errors.*
import computations.solutions.EulerMethodGraph
import computations.solutions.ExactSolutionGraph
import computations.solutions.ImprovedEulerMethodGraph
import computations.solutions.RungeKuttaMethodGraph
import javafx.collections.ObservableList
import javafx.scene.chart.XYChart
import tornadofx.*



/**
 * Represents data that will be displayed
 *  on the grid on "Solutions" page
 */
class SolutionChartData(chartData: ChartData? = null) : ChartData(chartData) {
    override fun computeGraphs(gridStep: Double): ObservableList<XYChart.Series<Number, Number>> {
        val graphs = mutableListOf<XYChart.Series<Number, Number>>().asObservable()

        if (isExactSolutionVisible)
            graphs += ExactSolutionGraph(x0, y0, N, xAxisMinVal, xAxisMaxVal, gridStep).series
        if (isEulerMethodVisible)
            graphs += EulerMethodGraph(x0, y0, N, xAxisMinVal, xAxisMaxVal, gridStep).series
        if (isImprovedEulerMethodVisible)
            graphs += ImprovedEulerMethodGraph(x0, y0, N, xAxisMinVal, xAxisMaxVal, gridStep).series
        if (isRungeKuttaMethodVisible)
            graphs += RungeKuttaMethodGraph(x0, y0, N, xAxisMinVal, xAxisMaxVal, gridStep).series

        // setting y-axis bounds
        setYAxisMinMaxVal(graphs)
        return graphs
    }
}


/**
 * Represents data that will be displayed
 *  on the grid on "Local Truncation Error" page
 */
class LocalTruncErrorChartData(chartData: ChartData? = null) : ChartData(chartData) {
    override fun computeGraphs(gridStep: Double): ObservableList<XYChart.Series<Number, Number>> {
        val graphs = mutableListOf<XYChart.Series<Number, Number>>().asObservable()

        if (isEulerMethodVisible)
            graphs += EulerLocalTruncErrorGraph(x0, y0, N, xAxisMinVal, xAxisMaxVal, gridStep).series
        if (isImprovedEulerMethodVisible)
            graphs += ImprovedEulerLocalTruncErrorGraph(x0, y0, N, xAxisMinVal, xAxisMaxVal, gridStep).series
        if (isRungeKuttaMethodVisible)
            graphs += RungeKuttaLocalTruncErrorGraph(x0, y0, N, xAxisMinVal, xAxisMaxVal, gridStep).series

        // setting y-axis bounds
        setYAxisMinMaxVal(graphs)
        return graphs
    }
}


/**
 * Represents data that will be displayed
 *  on the grid on "Global Truncation Error" page
 */
class GlobalTruncErrorChartData(chartData: ChartData? = null) : ChartData(chartData) {
    /**
     * Compute grid step:
     *  For global truncation error x-axis of grid
     *  represents values of N, since N is natural
     *  grid step is set to min value of N = 1.0
     */
    override fun computeGridStep(): Double {
        return 1.0
    }

    override fun computeGraphs(gridStep: Double): ObservableList<XYChart.Series<Number, Number>> {
        val graphs = mutableListOf<XYChart.Series<Number, Number>>().asObservable()

        if (isEulerMethodVisible) {
            graphs += EulerGlobalTruncErrorGraph(
                x0, y0, xAxisMinVal.toInt(), xAxisMaxVal.toInt(), gridStep.toInt(), xMaxForGlobalError
            ).series
        }
        if (isImprovedEulerMethodVisible) {
            graphs += ImprovedEulerGlobalTruncErrorGraph(
                x0, y0, xAxisMinVal.toInt(), xAxisMaxVal.toInt(), gridStep.toInt(), xMaxForGlobalError
            ).series
        }
        if (isRungeKuttaMethodVisible) {
            graphs += RungeKuttaGlobalTruncErrorGraph(
                x0, y0, xAxisMinVal.toInt(), xAxisMaxVal.toInt(), gridStep.toInt(), xMaxForGlobalError
            ).series
        }

        // setting y-axis bounds
        setYAxisMinMaxVal(graphs)
        return graphs
    }
}