package styles

import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    private val exactColor = Color.RED
    private val eulerColor = Color.BLUE
    private val improvedEulerColor = Color.ORANGE
    private val rungeKuttaColor = Color.GREEN

    companion object {
        val exactSeries by cssclass()
        val eulerSeries by cssclass()
        val improvedEulerSeries by cssclass()
        val rungeKuttaSeries by cssclass()

        val exactCheckBox by cssclass()
        val eulerCheckBox by cssclass()
        val improvedEulerCheckBox by cssclass()
        val rungeKuttaCheckBox by cssclass()

        val diffEquationInfoTitle by cssclass()
        val diffEquationInfoText by cssclass()
    }

    init {
        val checkboxMark = mixin {
            and(selected) { mark { backgroundColor += c("white") } }
        }

        exactSeries { stroke = Paint.valueOf(exactColor.toString()) }
        eulerSeries { stroke = Paint.valueOf(eulerColor.toString()) }
        improvedEulerSeries { stroke = Paint.valueOf(improvedEulerColor.toString()) }
        rungeKuttaSeries { stroke = Paint.valueOf(rungeKuttaColor.toString()) }

        diffEquationInfoTitle {
            fontWeight = FontWeight.EXTRA_BOLD
            fontSize = 25.px
        }

        diffEquationInfoText {
            fontWeight = FontWeight.MEDIUM
            fontStyle = FontPosture.ITALIC
            fontSize = 22.5.px
            fontFamily = "Times New Roman"
        }

        exactCheckBox {
            box {
                backgroundColor += exactColor
            }
            and(focused) {
                box {
                    backgroundColor += exactColor.darker()
                }
            }
            +checkboxMark
        }
        eulerCheckBox {
            box { backgroundColor += eulerColor }
            and(focused) {
                box {
                    backgroundColor += eulerColor.darker()
                }
            }
            +checkboxMark
        }
        improvedEulerCheckBox {
            box { backgroundColor += improvedEulerColor }
            and(focused) {
                box {
                    backgroundColor += improvedEulerColor.darker()
                }
            }
            +checkboxMark
        }
        rungeKuttaCheckBox {
            box { backgroundColor += rungeKuttaColor }
            and(focused) {
                box {
                    backgroundColor += rungeKuttaColor.darker()
                }
            }
            +checkboxMark
        }
    }
}