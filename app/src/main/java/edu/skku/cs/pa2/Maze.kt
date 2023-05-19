package edu.skku.cs.pa2

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

class Maze {
    var size: Int = 0
    var cells: Array<Array<MazeCell>> = emptyArray()
    var playerX: Int = 0
    var playerY: Int = 0

    fun findNextCell(): Pair<Int, Int> {
        // BFS
        val queue = ArrayDeque<Pair<Int, Int>>()
        val visited = Array(size) { Array(size) { false } }
        val prev = Array(size) { Array(size) { Pair(-1, -1) } }

        queue.add(Pair(playerY, playerX))
        visited[playerY][playerX] = true

        // 상 하 좌 우
        val moves = listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            val y = current.first
            val x = current.second

            if (cells[y][x].isGoal) {
                var cellPosition = prev[y][x]
                while (prev[cellPosition.first][cellPosition.second] != Pair(playerY, playerX)) {
                    cellPosition = prev[cellPosition.first][cellPosition.second]
                }
                return cellPosition
            }

            for (move in moves) {
                val nextY = y + move.first
                val nextX = x + move.second
                // 미로 벗어나지 않고, 방문하지 않았고, 벽이 아닌 경우
                if (nextY in 0 until size && nextX in 0 until size
                    && !visited[nextY][nextX]
                    && !((move.first == -1 && cells[y][x].top)
                            || (move.first == 1 && cells[y][x].bottom)
                            || (move.second == -1 && cells[y][x].left)
                            || (move.second == 1 && cells[y][x].right))) {
                    queue.add(Pair(nextY, nextX))
                    visited[nextY][nextX] = true
                    prev[nextY][nextX] = Pair(y, x)
                }
            }
        }

        // error
        return Pair(-1, -1)
    }


    fun moveLeft(): Boolean {
        if (playerX == 0) return false
        if (cells[playerY][playerX].left) return false
        cells[playerY][playerX].isPlayer = false
        playerX--
        cells[playerY][playerX].isPlayer = true
        return true
    }

    fun moveRight(): Boolean {
        if (playerX == size - 1) return false
        if (cells[playerY][playerX].right) return false
        cells[playerY][playerX].isPlayer = false
        playerX++
        cells[playerY][playerX].isPlayer = true
        return true
    }

    fun moveUp(): Boolean {
        if (playerY == 0) return false
        if (cells[playerY][playerX].top) return false
        cells[playerY][playerX].isPlayer = false
        playerY--
        cells[playerY][playerX].isPlayer = true
        return true
    }

    fun moveDown(): Boolean {
        if (playerY == size - 1) return false
        if (cells[playerY][playerX].bottom) return false
        cells[playerY][playerX].isPlayer = false
        playerY++
        cells[playerY][playerX].isPlayer = true
        return true
    }

    suspend fun loadMaze(mazeName: String) = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val url = "http://swui.skku.edu:1399/maze/map?name=$mazeName"
        val request = Request.Builder().url(url).build()

        try {
            val response = client.newCall(request).execute()
            val body = response.body?.string()
            body?.let {
                val jsonObject = JSONObject(it)
                val mazeString = jsonObject.getString("maze")
                Log.d("Maze", mazeString)
                parseMaze(mazeString)
            }
        } catch (e: IOException) {
            Log.e("Maze", "Failed to connect to the server", e)
        }
    }

    private fun parseMaze(mazeString: String) {
        val lines = mazeString.trim().split("\n")
        size = lines[0].toInt()
        Log.d("lines", lines.toString())
        cells = Array(size) { Array(size) { MazeCell() } }

        for (i in 1 until lines.size) {
            val row = lines[i].trim().split(" ").map { it.toInt() }
            for (j in row.indices) {
                cells[i - 1][j].top = (row[j] and 8) == 8
                cells[i - 1][j].left = (row[j] and 4) == 4
                cells[i - 1][j].bottom = (row[j] and 2) == 2
                cells[i - 1][j].right = (row[j] and 1) == 1
            }
        }

        // set start and goal
        cells[0][0].isPlayer = true
        cells[size - 1][size - 1].isGoal = true
    }

    override fun toString(): String {
        return "Maze(size=$size, cells=${cells.contentToString()})"
    }

}