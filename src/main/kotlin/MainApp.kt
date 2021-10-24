import ui.MainView
import styles.Styles
import tornadofx.*

class MainApp : App(MainView::class, Styles::class) {
    init { reloadStylesheetsOnFocus() }
}

fun main(args: Array<String>) {
    launch<MainApp>(args)
}