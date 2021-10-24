package ui

import datamodel.ChartDataModel
import datamodel.GlobalTruncErrorChartData
import datamodel.LocalTruncErrorChartData
import datamodel.SolutionChartData
import javafx.scene.control.TabPane
import javafx.scene.layout.ColumnConstraints
import javafx.stage.Screen
import tornadofx.*

class MainView : View("Differential Equations: Computational Practicum by Iskander Navikov B20-02") {
    // model is responsible for interaction between user input and program output
    private val model: ChartDataModel by inject()

    private val inputFormView: InputFormView by inject()
    private val diffEquationInfoView: DiffEquationInfoView by inject()
    private val mathChartView: MathChartView by inject()

    override val root = vbox {
        // Tabs to select a page
        tabpane {
            // make impossible to close the tab
            tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE

            tab("Solution").whenSelected {
                model.rebind { model.item = SolutionChartData(model.item) }
                model.commit()
            }
            tab("Local Truncation Error").whenSelected {
                model.rebind { model.item = LocalTruncErrorChartData(model.item) }
                model.commit()
            }
            tab("Global Truncation Error").whenSelected {
                model.rebind { model.item = GlobalTruncErrorChartData(model.item) }
                model.commit()
            }
        }

        // Main content (chart, input form, ...)
        gridpane {
            row {
                addColumn(0, mathChartView.root)
                vbox {
                    prefHeight = Screen.getPrimary().bounds.height * 0.85
                    add(diffEquationInfoView)
                    add(inputFormView)
                }
            }

            val col1Constraints = ColumnConstraints()
            col1Constraints.percentWidth = 82.5
            val col2Constraints = ColumnConstraints()
            col2Constraints.percentWidth = 17.5

            columnConstraints.addAll(col1Constraints, col2Constraints)
        }
    }
}