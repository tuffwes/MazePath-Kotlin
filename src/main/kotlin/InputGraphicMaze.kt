import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.geom.Line2D
import java.util.*
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.JPanel
import kotlin.math.floor

class InputGraphicMaze : JFrame("MAZE CREATED") {
    private val M: Int
    private val N: Int
    private val C: Array<Intcoll?>
    private val Top: Line2D
    private val Bottom: Line2D
    private val Left: Line2D
    private val Right: Line2D
    private val Line: IntArray
    private val status: BooleanArray
    private var show = 0
    private var Path: LinkedList<Point>? = null
    private val L: LinkedList<Node>
    private val start: Int
    private val delta: Int
    private val drawingStroke: Stroke = BasicStroke(2F)

    //end of constructor
    internal inner class ShowPathListener : ActionListener {
        override fun actionPerformed(e: ActionEvent) {
            show = 1
            title = "A MAZE WITH PATH"
            repaint()
        }
    }

    internal inner class SlotMouseListener : MouseListener {
        override fun mouseClicked(e: MouseEvent) {}
        override fun mouseEntered(e: MouseEvent) {}
        override fun mouseExited(e: MouseEvent) {}
        override fun mousePressed(e: MouseEvent) {
            // get mouse position
            val x = e.x - start
            val y = e.y - start
            val col = 1 + floor((x / delta).toDouble()).toInt()
            val row = 1 + floor((y / delta).toDouble()).toInt()
            if (row in 1..M && col > 0 && col <= N) {
                val sred = Node(row, col, Color.red)
                val sblue = Node(row, col, Color.blue)
                //var i: Int
                //var j: Int
                var h = -1
                var found = false
                var nn: Node? = null
                val J: Iterator<*> = L.iterator()
                while (J.hasNext() && !found) {
                    h++
                    val n = J.next() as Node
                    val q = n.p
                    //i = q.getX().toInt()
                    //j = q.getY().toInt()
                    q.getX().toInt()
                    q.getY().toInt()
                    //val C = n.c
                    if (q == sred.p) {
                        found = true
                        nn = n
                    }
                }
                if (found) {
                    if (nn!!.c == sblue.c) nn.c = Color.red else if (nn.c == sred.c) L.removeAt(h)
                } else L.add(sblue)
                repaint()
            }
        }

        override fun mouseReleased(e: MouseEvent) {}
    }

    inner class Mazepanel : JPanel() {
        public override fun paintComponent(g: Graphics) {
            val graph = g as Graphics2D
            graph.clearRect(0, 0, width, height)
            graph.stroke = drawingStroke
            graph.paint = Color.red
            graph.draw(Top)
            graph.draw(Bottom)
            graph.draw(Left)
            graph.draw(Right)
            val K = N * (M - 1) + (N - 1) * M
            val D = M * (N - 1)
            var i: Int
            var j: Int
            for (s in 1..K - M * N + 1) {
                val k = Line[s]
                var kk: Int
                var line: Line2D
                if (k <= D) {
                    i = (k - 1) / (N - 1) + 1
                    j = k - (i - 1) * (N - 1)
                    line = Line2D.Double(
                        (start + j * delta).toDouble(),
                        (start + (i - 1) * delta).toDouble(),
                        (start + j * delta).toDouble(),
                        (start + i * delta).toDouble()
                    )
                } else {
                    kk = k - D
                    i = (kk - 1) / N + 1
                    j = kk - N * (i - 1)
                    line = Line2D.Double(
                        (start + (j - 1) * delta).toDouble(),
                        (start + i * delta).toDouble(), (start + j * delta).toDouble(), (start + i * delta).toDouble()
                    )
                }
                graph.paint = Color.black
                graph.draw(line)
            }
            if (show == 1) {
                L.clear()
                graph.paint = Color.green
                val I: ListIterator<Point> = Path!!.listIterator()
                while (I.hasNext()) {
                    val p = I.next()
                    i = p.getX().toInt()
                    j = p.getY().toInt()
                    graph.fillRect(start + (j - 1) * delta + 1, start + (i - 1) * delta + 1, delta - 2, delta - 2)
                }
            } else {
                val J: ListIterator<Node> = L.listIterator()
                while (J.hasNext()) {
                    val n = J.next()
                    val q = n.p
                    i = q.getX().toInt()
                    j = q.getY().toInt()
                    val C = n.c
                    graph.paint = C
                    graph.fillRect(start + (j - 1) * delta + 1, start + (i - 1) * delta + 1, delta - 2, delta - 2)
                }
            }
        }
    }

    fun Rows(): Int {
        return M
    }

    fun Cols(): Int {
        return N
    }

    fun can_go(i: Int, j: Int, c: Char): Boolean {
        val D = M * (N - 1)
        var result = false
        //val K = N * (M - 1) + (N - 1) * M
        if (c == 'U') result = !status[D + (i - 2) * N + j]
        if (c == 'R') result = !status[(i - 1) * (N - 1) + j]
        if (c == 'D') result = !status[D + (i - 1) * N + j]
        if (c == 'L') result = !status[(i - 1) * (N - 1) + j - 1]
        return result
    }

    fun showPath(P: LinkedList<Point>?) {
        Path = P
        repaint()
    }

    private fun union(A: Intcoll?, B: Intcoll?, C: Array<Intcoll?>) {
        var i = 1
        var n: Int = B!!.get_howmany()
        while (n > 0) {
            if (B.belongs(i)) {
                A!!.insert(i)
                n--
            }
            i++
        }
    }

    private inner class Node(X: Int, Y: Int, color: Color) {
        val p = Point(X, Y)
        var c = color
    }

    init {
        val integers = JOptionPane.showInputDialog("Enter #rows and #columns(e.g. 4 4)")
        val `in` = Scanner(integers)
        M = `in`.nextInt()
        N = `in`.nextInt()
        L = LinkedList()
        C = arrayOfNulls(N * M + 1)
        var k: Int
        k = 1
        while (k <= M * N) {
            C[k] = Intcoll()
            C[k]?.insert(k)
            k++
        }
        start = 50
        delta = 15
        Top = Line2D.Double(
            (start + delta).toDouble(), start.toDouble(), (start + N * delta).toDouble(),
            start.toDouble()
        )
        Bottom = Line2D.Double(
            start.toDouble(), (start + M * delta).toDouble(),
            (start + (N - 1) * delta).toDouble(), (start + M * delta).toDouble()
        )
        Left = Line2D.Double(start.toDouble(), start.toDouble(), start.toDouble(), (start + M * delta).toDouble())
        Right = Line2D.Double(
            (start + N * delta).toDouble(), start.toDouble(), (start + N * delta).toDouble(),
            (start + M * delta).toDouble()
        )
        var i: Int
        var j: Int
        val size = M * (N - 1) + (M - 1) * N + 1
        //Add all internal lines
        Line = IntArray(size)
        status = BooleanArray(size)
        k = 1
        while (k < size) {
            Line[k] = k
            status[k] = true
            k++
        }
        // Randomly generate the next line to remove if its adjacent
        // cells are not already connected
        val gen = Random()
        var L = M * (N - 1) + (M - 1) * N
        //val S = 1
        var cell1: Int
        var cell2: Int
        //var temp: Int
        var count = 0
        var a: Int
        var b: Int
        var slot: Int
        //remove enough lines to generate the maze
        while (count < M * N - 1) {
            slot = gen.nextInt(L) + 1
            k = Line[slot]
            val D = M * (N - 1)
            if (k <= D) {
                i = (k - 1) / (N - 1) + 1
                j = k - (i - 1) * (N - 1)
                cell1 = (i - 1) * N + j
                cell2 = cell1 + 1
            } else {
                val K = k - D
                i = (K - 1) / N + 1
                j = K - N * (i - 1)
                cell1 = (i - 1) * N + j
                cell2 = cell1 + N
            } // Find a and b - the sets of adjacent cells cell1 and cell2
            a = 1
            while (!C[a]?.belongs(cell1)!!) a++
            b = 1
            while (!C[b]?.belongs(cell2)!!) b++
            //if not in same set - take the union of the sets
            //and remove the line between the two cells
            if (a != b) {
                if (a < b) {
                    union(C[a], C[b], C)
                    C[b] = C[M * N - count]
                } else {
                    union(C[b], C[a], C)
                    C[a] = C[M * N - count]
                }
                status[k] = false
                count++
                Line[slot] = Line[L]
                L--
            }
        } //end of while
        val mazepanel = Mazepanel()
        layout = BorderLayout()
        add(mazepanel, BorderLayout.CENTER)
        val slotlistener = SlotMouseListener()
        mazepanel.addMouseListener(slotlistener)
        val button = JButton("Click to show Path")
        add(button, BorderLayout.NORTH)
        val buttonlistener: ActionListener = ShowPathListener()
        button.addActionListener(buttonlistener)
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(1200, 800)
        isVisible = true
    }
} // end of class
