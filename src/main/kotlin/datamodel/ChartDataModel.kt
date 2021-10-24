package datamodel

import javafx.beans.property.SimpleDoubleProperty
import tornadofx.*

class ChartDataModel : ItemViewModel<ChartData>(SolutionChartData()) {
    val x0 = bind(ChartData::x0Property)
    val y0 = bind(ChartData::y0Property)
    val N = bind(ChartData::NProperty)

    val isExactSolutionVisible = bind(ChartData::isExactSolutionVisibleProperty)
    val isEulerMethodVisible = bind(ChartData::isEulerMethodVisibleProperty)
    val isImprovedEulerMethodVisible = bind(ChartData::isImprovedEulerMethodVisibleProperty)
    val isRungeKuttaMethodVisible = bind(ChartData::isRungeKuttaMethodVisibleProperty)

    val xAxisMinVal = bind(ChartData::xAxisMinValProperty)
    val xAxisMaxVal = bind(ChartData::xAxisMaxValProperty)
    val yAxisMinVal = bind(ChartData::yAxisMinValProperty)
    val yAxisMaxVal = bind(ChartData::yAxisMaxValProperty)

    val xMaxForGlobalError = bind(ChartData::xMaxForGlobalErrorProperty)

    val graphs = bind(ChartData::graphsProperty)

    /** grid step that will shown on the chart */
    val gridStep = SimpleDoubleProperty(item.computeGridStep())

    /**
     * When input data is changing the grids that are displayed
     *  must be updated. Also grid step must be updated
     * Notice that the grid step must be calculated before grids
     *  since grids uses grid step to be calculated
     */
    override fun onCommit() {
        gridStep.value = item.computeGridStep()
        graphs.value = item.computeGraphs(gridStep.value)
        super.onCommit()
    }
}