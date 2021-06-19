import java.awt.Point
import java.util.*

class Maze {
    private val maze = InputGraphicMaze()
    private val R = maze.Rows()
    private val C = maze.Cols()

    // Creates the path through maze, starting at cell (srow, scol)
    // and ending at cell (erow and ecol),  in L
    fun CreatePath(
        maze: InputGraphicMaze,
        srow: Int, scol: Int, erow: Int, ecol: Int, L: LinkedList<Point>
    ): Boolean {
        val stack: Deque<IntArray> = LinkedList()
        var crow = srow
        var ccol = scol
        var cdir = 0
        var pdir = -1
        val dirs = charArrayOf('R', 'D', 'L', 'U')
        while (crow != erow || ccol != ecol) {
            if (cdir == 0) {
                println("$crow,$ccol,$cdir,$pdir")
                if (ccol < C && maze.can_go(crow, ccol, dirs[cdir]) && pdir != cdir) {
                    println("first")
                    stack.addFirst(intArrayOf(crow, ccol, cdir, pdir))
                    pdir = (cdir + 2) % 4
                    ccol++
                    cdir = 0
                } else cdir++
            }
            if (cdir == 1) {
                println("$crow,$ccol,$cdir,$pdir")
                if (crow < R && maze.can_go(crow, ccol, dirs[cdir]) && pdir != cdir) {
                    println("second")
                    stack.addFirst(intArrayOf(crow, ccol, cdir, pdir))
                    pdir = (cdir + 2) % 4
                    crow++
                    cdir = 0
                } else cdir++
            }
            if (cdir == 2) {
                println("$crow,$ccol,$cdir,$pdir")
                if (ccol > 1 && maze.can_go(crow, ccol, dirs[cdir]) && pdir != cdir) {
                    println("third")
                    stack.addFirst(intArrayOf(crow, ccol, cdir, pdir))
                    pdir = (cdir + 2) % 4
                    ccol--
                    cdir = 0
                } else cdir++
            }
            if (cdir == 3) {
                println("$crow,$ccol,$cdir,$pdir")
                if (crow > 1 && maze.can_go(crow, ccol, dirs[cdir]) && pdir != cdir) {
                    println("fourth")
                    stack.addFirst(intArrayOf(crow, ccol, cdir, pdir))
                    pdir = (cdir + 2) % 4
                    crow--
                    cdir = 0
                } else cdir++
            }
            if (cdir == 4) {
                if (stack.size == 0) break
                val tmp = stack.removeFirst()
                crow = tmp[0]
                ccol = tmp[1]
                cdir = 1 + tmp[2]
                pdir = tmp[3]
                println("pop:$crow,$ccol,$cdir,$pdir")
            }
        }
        if (crow != erow || ccol != ecol) {
            System.err.print("Could not find path")
            return false
        }
        L.add(Point(crow, ccol))
        var i = stack.size
        var data: IntArray
        while (i > 0) {
            data = stack.removeFirst()
            L.add(Point(data[0], data[1]))
            i--
        }
        return true
    }

    init {
        // an R rows x C columns maze
        // Path holds the cells of the path
        val Path = LinkedList<Point>()
        // Create the path
        CreatePath(maze, 1, 1, R, C, Path)
        // show the path in the maze
        maze.showPath(Path)
    }
}