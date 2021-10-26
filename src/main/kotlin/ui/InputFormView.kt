package ui

import datamodel.*
import styles.Styles
import javafx.beans.property.Property
import javafx.scene.control.CheckBox
import javafx.scene.control.TextField
import javafx.util.StringConverter
import javafx.util.converter.IntegerStringConverter
import tornadofx.*


private val doubleStringConverter = object : StringConverter<Number>() {
    override fun fromString(s: String?): Number = s?.toDouble() ?: 0.0
    override fun toString(number: Number?): String = number?.toDouble()?.toBigDecimal()?.stripTrailingZeros()?.toPlainString() ?: ""
}

private val integerStringConverter = object : StringConverter<Number>() {
    private val converter = IntegerStringConverter()
    override fun fromString(s: String?): Number = converter.fromString(s)
    override fun toString(number: Number?): String = converter.toString(number?.toInt())
}

/** @return number of type Double converted from this string
 *          null if cannot convert to valid number */
private fun String?.toValidDouble(): Double? {
    if (isNullOrBlank() || !matches(Regex("[-]?([0-9]*[.])?[0-9]+")))
        return null
    return toDouble()
}

/** @return number of type Int converted from this string
 *          null if cannot convert to valid number */
private fun String?.toValidInt(): Int? {
    if (isNullOrBlank() || !matches(Regex("[-]?[0-9]+")))
        return null
    return toInt()
}


class InputFormView : View() {
    // model is responsible for interaction between user input and program output
    private val model: ChartDataModel by inject()

    private val solutionPageProperty = booleanProperty(model.item is SolutionChartData)
    private val globalErrorPageProperty = booleanProperty(model.item is GlobalTruncErrorChartData)

    init {
        // if another page is selected
        model.itemProperty.onChange {
            solutionPageProperty.value = model.item is SolutionChartData
            globalErrorPageProperty.value = model.item is GlobalTruncErrorChartData
        }
    }

    // Interface description
    override val root = form {
        /** ============= Field set of input text fields ============= */
        fieldset{
            // x0
            field("x₀ :").textfield(model.x0, doubleStringConverter) {
                validator {
                    when {
                        it.isNullOrBlank() -> error("required")
                        it.toValidDouble() == null -> error("not a valid rational number")
                        else -> null
                    }
                }
                defaultBehaviour(model.x0)
            }

            // y0
            field("y₀:").textfield(model.y0, doubleStringConverter) {
                validator {
                    when {
                        it.isNullOrBlank() -> error("required")
                        it.toValidDouble() == null -> error("not a valid rational number")
                        else -> null
                    }
                }
                defaultBehaviour(model.y0)
            }

            // N
            field("N:") {
                textfield(model.N, integerStringConverter) {
                    validator {
                        val N = it.toValidInt()
                        when {
                            it.isNullOrBlank() -> error("required")
                            N == null -> error("not a valid integer number")
                            N < 0 -> error("must be non-negative")
                            N > 3000 -> error("too large")
                            else -> null
                        }
                    }
                    defaultBehaviour(model.N)
                }
                disableWhen(globalErrorPageProperty)
            }

            // X
            field("X: ").textfield(model.X, doubleStringConverter) {
                validator {
                    val X = it.toValidDouble()
                    when {
                        it.isNullOrBlank() -> error("required")
                        X == null -> error("not a valid rational number")
                        X <= model.x0.value -> error("must be greater than x₀")
                        else -> null
                    }
                }
                defaultBehaviour(model.X)
            }

            // N max
            field("N max: ") {
                textfield(model.NMax, integerStringConverter) {
                    validator {
                        val NMax = it.toValidInt()
                        when {
                            it.isNullOrBlank() -> error("required")
                            NMax == null -> error("not a valid integer number")
                            NMax <= 0 -> error("must be positive")
                            NMax > 1000 -> error("too large")
                            else -> null
                        }
                    }
                    defaultBehaviour(model.NMax)
                }
                disableWhen(!globalErrorPageProperty)
            }
        }


        /** ============= Field set of checkboxes ============= */
        fieldset {
            field {
                checkbox(EXACT_SOLUTION_NAME, model.isExactSolutionVisible) {
                    addClass(Styles.exactCheckBox)
                    defaultBehaviour(model.isExactSolutionVisible)
                }
                enableWhen(solutionPageProperty)
            }
            field().checkbox(EULER_METHOD_NAME, model.isEulerMethodVisible) {
                defaultBehaviour(model.isEulerMethodVisible)
                addClass(Styles.eulerCheckBox)
            }
            field().checkbox(IMPROVED_EULER_METHOD_NAME, model.isImprovedEulerMethodVisible) {
                defaultBehaviour(model.isImprovedEulerMethodVisible)
                addClass(Styles.improvedEulerCheckBox)
            }
            field().checkbox(RUNGE_KUTTA_METHOD_NAME, model.isRungeKuttaMethodVisible) {
                defaultBehaviour(model.isRungeKuttaMethodVisible)
                addClass(Styles.rungeKuttaCheckBox)
            }
        }
    }


    /** Function that defines default behaviour of a checkbox */
    private fun CheckBox.defaultBehaviour(boundProperty: Property<out Any>) {
        selectedProperty().onChange {
            if (model.isValid && model.isDirty(boundProperty))
                model.commit()
        }
        action { parent.requestFocus() } // press enter => lose focus
    }

    /** Function that defines default behaviour of a text field */
    private fun TextField.defaultBehaviour(boundProperty: Property<out Any>) {
        textProperty().onChange {
            if (model.isValid && model.isDirty(boundProperty))
                model.commit()
        }
        action { parent.requestFocus() } // press enter => lose focus
    }
}