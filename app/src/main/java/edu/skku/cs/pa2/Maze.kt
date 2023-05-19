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