package edu.skku.cs.pa2

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException

class MazeSelectionActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    private lateinit var listView: ListView
    private lateinit var usernameTextView: TextView
    private lateinit var explanationTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maze_selection)

        listView = findViewById(R.id.maze_list)
        usernameTextView = findViewById(R.id.username)
        explanationTextView = findViewById(R.id.explanation)

        val username = intent.getStringExtra("username")
        usernameTextView.text = username

        val request = Request.Builder().url("http://swui.skku.edu:1399/maps").build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Maze", "Failed to connect to the server", e)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                if (response.isSuccessful && responseBody != null) {
                    val jsonArray = JSONArray(responseBody)
                    val mazeData = mutableListOf<MazeData>()

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val name = jsonObject.getString("name")
                        val size = jsonObject.getInt("size")
                        mazeData.add(MazeData(name, size))
                    }

                    runOnUiThread {
                        val adapter = MazeListAdapter(
                            this@MazeSelectionActivity,
                            R.layout.maze_entry,
                            mazeData
                        )
                        listView.adapter = adapter
                    }
                } else {
                    Log.e("Maze", "Failed to get maze list")
                }
            }
        })
    }
}
