package datamodel

/** Data of a chart which abscissa axis is x */
abstract class XBasedChartData(chartData: ChartData? = null) : ChartData(chartData) {
    init {
        // x0 must always be equal to xAxisMinVal (to let approximation methods be computed)
        xAxisMinValProperty.bindBidirectional(x0Property)

        // max boundary of x-axis is exactly max value that x can take
        xAxisMaxValProperty.bindBidirectional(XProperty)
    }
}

/** Data of a chart which abscissa axis is N (for global errors page) */
abstract class NBasedChartData(chartData: ChartData? = null) : ChartData(chartData) {
    init {
        // on GlobalTruncError page xAxisMinVal must always be equal to 0
        // since there x-axis represents values of non-negative N
        xAxisMinVal = 0.0

        // max boundary of x-axis is exactly max value that N can take
        xAxisMaxValProperty.bindBidirectional(NMaxProperty)
    }
}