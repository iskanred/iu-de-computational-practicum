package ui

import styles.Styles
import tornadofx.*

class DiffEquationInfoView : View() {
    override val root = vbox {
        spacing = 20.0

        vbox(2.0) {
            text("Differential Equation:").addClass(Styles.diffEquationInfoTitle)
            text("y' = sec(x) - y · tan(x)").addClass(Styles.diffEquationInfoText)
        }
        vbox(2.0) {
            text("Exact Solution:").addClass(Styles.diffEquationInfoTitle)
            text("sin(x) + C · cos(x), where C ∈ R").addClass(Styles.diffEquationInfoText)
        }

        vboxConstraints {
            marginTopBottom(15.0)
        }
    }
}