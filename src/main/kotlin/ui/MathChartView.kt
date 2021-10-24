package ui

import datamodel.ChartDataModel
import javafx.geometry.Side
import javafx.scene.Cursor
import javafx.scene.chart.NumberAxis
import javafx.util.StringConverter
import styles.Styles
import tornadofx.*
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode


private const val ZOOM_FACTOR_X = 0.025

/** Is necessary to display axis marks in more convenient way */
private val tickLabelConverter = object : StringConverter<Number>() {
    override fun fromString(s: String): Number = s.toDouble()
    override fun toString(number: Number): String =
        BigDecimal.valueOf(number.toDouble())
            .round(MathContext(2, RoundingMode.HALF_UP))
            .toString()
}


class MathChartView : View() {
    // is responsible for interaction between input and output data
    private val model: ChartDataModel by inject()

    override val root = linechart(x = NumberAxis(), y = NumberAxis()) {
        animated = false
        createSymbols = true
        isLegendVisible = false
        cursor = Cursor.CROSSHAIR

        dataProperty().onChange { seriesList ->
            if (seriesList == null || seriesList.isEmpty())
                return@onChange

            seriesList.forEach { series ->
                when(series.name) {
                    "Exact Solution" -> series.node?.addClass(Styles.exactSeries)
                    "Euler Method" -> series.node?.addClass(Styles.eulerSeries)
                    "Improved Euler Method" -> series.node?.addClass(Styles.improvedEulerSeries)
                    "Runge Kutta Method" -> series.node?.addClass(Styles.rungeKuttaSeries)
                }
            }
        }

        // bind chart data with data of computations
        dataProperty().bind(model.graphs)

        with(xAxis as NumberAxis) {
            isAutoRanging = false
            side = Side.BOTTOM
            lowerBoundProperty().bind(model.xAxisMinVal)
            upperBoundProperty().bind(model.xAxisMaxVal)
            tickUnitProperty().bind(model.gridStep)
        }
        with(yAxis as NumberAxis) {
            isAutoRanging = false
            side = Side.LEFT
            lowerBoundProperty().bind(model.yAxisMinVal - 1.0)
            upperBoundProperty().bind(model.yAxisMaxVal + 1.0)
            tickUnitProperty().bind((model.yAxisMaxVal - model.yAxisMinVal) / 10.0)
            tickLabelFormatter = tickLabelConverter
        }
        setOnScroll {
            if (!model.isValid)
                return@setOnScroll

            val zoomChangeX = it.deltaY * ZOOM_FACTOR_X
            model.xAxisMaxVal -= zoomChangeX
            when {
                model.isValid -> model.commit()
                else -> model.rollback()
            }
        }
    }
}