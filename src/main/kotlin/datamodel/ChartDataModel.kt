package datamodel

import tornadofx.*

class ChartDataModel : ItemViewModel<ChartData>(SolutionChartData()) {
    val x0 = bind(ChartData::x0Property)
    val y0 = bind(ChartData::y0Property)
    val N = bind(ChartData::NProperty)
    val X = bind(ChartData::XProperty)
    val NMax = bind(ChartData::NMaxProperty)

    val isExactSolutionVisible = bind(ChartData::isExactSolutionVisibleProperty)
    val isEulerMethodVisible = bind(ChartData::isEulerMethodVisibleProperty)
    val isImprovedEulerMethodVisible = bind(ChartData::isImprovedEulerMethodVisibleProperty)
    val isRungeKuttaMethodVisible = bind(ChartData::isRungeKuttaMethodVisibleProperty)

    val xAxisMinVal = bind(ChartData::xAxisMinValProperty)
    val xAxisMaxVal = bind(ChartData::xAxisMaxValProperty)
    val yAxisMinVal = bind(ChartData::yAxisMinValProperty)
    val yAxisMaxVal = bind(ChartData::yAxisMaxValProperty)

    val graphs = bind(ChartData::graphsProperty)

    val gridStep = bind(ChartData::gridStepProperty)

    /**
     * When input data is changing the grids that are displayed
     *  must be updated. Also grid step must be updated
     * Notice that the grid step must be calculated before grids
     *  since grids uses grid step to be calculated
     */
    override fun onCommit() {
        item.updateGraphs()
        super.onCommit()
    }
}