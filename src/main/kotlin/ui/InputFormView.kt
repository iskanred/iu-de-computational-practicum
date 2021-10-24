package ui

import datamodel.ChartDataModel
import datamodel.GlobalTruncErrorChartData
import datamodel.SolutionChartData
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.control.TextField
import styles.Styles
import tornadofx.*

class InputFormView : View() {
    // model is responsible for interaction between user input and program output
    private val model: ChartDataModel by inject()

    private val solutionPageProperty = SimpleBooleanProperty(true)
    private val globalErrorPageProperty = SimpleBooleanProperty(false)

    init {
        model.itemProperty.onChange {
            solutionPageProperty.value = model.item is SolutionChartData
            globalErrorPageProperty.value = model.item is GlobalTruncErrorChartData
        }
    }

    override val root = form {
        /** function that defines default behaviour of text field */
        fun TextField.defaultBehaviour() {
            textProperty().onChange {
                model.commit()
            }
            action { parent.requestFocus() } // press enter => lose focus
        }


        /** Field set of input text fields */
        fieldset {
            field("x₀ :").textfield(model.x0) {
                validator {
                    when {
                        it.isNullOrBlank() -> error("required")
                        !it.matches(Regex("[-]?([0-9]*[.])?[0-9]+")) -> error("not a valid number")
                        else -> null
                    }
                }
                defaultBehaviour()
            }
            field("y₀:").textfield(model.y0) {
                validator {
                    when {
                        it.isNullOrBlank() -> error("required")
                        !it.matches(Regex("[-]?([0-9]*[.])?[0-9]+")) -> error("not a valid number")
                        else -> null
                    }
                }
                defaultBehaviour()
            }
            field("N:") {
                textfield(model.N) {
                    validator {
                        when {
                            it.isNullOrBlank() -> error("required")
                            !it.matches(Regex("[-]?[0-9]+")) -> error("not a valid number")
                            it.toInt() < 0 -> error("must be non-negative")
                            else -> null
                        }
                    }
                    defaultBehaviour()
                }
                disableWhen(globalErrorPageProperty)
            }
            field("X: ").textfield(model.xMaxForGlobalError) {
                validator {
                    when {
                        it.isNullOrBlank() -> error("required")
                        !it.matches(Regex("[-]?([0-9]*[.])?[0-9]+")) -> error("not a valid number")
                        it.toDouble() <= model.x0.value -> error("must be greater than x0")
                        else -> null
                    }
                }
                defaultBehaviour()
                enableWhen(globalErrorPageProperty)
            }
        }

        /** Field set of checkboxes */
        fieldset {
            field {
                checkbox("Exact Solution", model.isExactSolutionVisible) {
                    action {
                        if (model.isDirty)
                            model.commit()
                        parent.requestFocus()
                    }
                    addClass(Styles.exactCheckBox)
                }
                enableWhen(solutionPageProperty)
            }
            field().checkbox("Euler Method", model.isEulerMethodVisible) {
                action {
                    if (model.isDirty)
                        model.commit()
                    parent.requestFocus()
                }
                addClass(Styles.eulerCheckBox)
            }
            field().checkbox("Improved Euler Method", model.isImprovedEulerMethodVisible) {
                action {
                    if (model.isDirty)
                        model.commit()
                    parent.requestFocus()
                }
                addClass(Styles.improvedEulerCheckBox)
            }
            field().checkbox("Runge–Kutta Method", model.isRungeKuttaMethodVisible) {
                action {
                    if (model.isDirty)
                        model.commit()
                    parent.requestFocus()
                }
                addClass(Styles.rungeKuttaCheckBox)
            }
        }
    }
}