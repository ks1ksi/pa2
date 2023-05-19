package edu.skku.cs.pa2

import android.os.Bundle
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

        // Get the username from the SignInActivity
        val username = intent.getStringExtra("username")
        usernameTextView.text = username

        // Send a HTTP GET request to the server to retrieve a list of mazes
        val request = Request.Builder()
            .url("http://swui.skku.edu:1399/maps")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle the error
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                if (response.isSuccessful && responseBody != null) {
                    val jsonArray = JSONArray(responseBody)

                    // Create a list to store the mazes
                    val mazeData = mutableListOf<MazeData>()

                    // Parse the JSON array
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val name = jsonObject.getString("name")
                        val size = jsonObject.getInt("size")
                        mazeData.add(MazeData(name, size))
                    }

                    // Update the ListView on the UI thread
                    runOnUiThread {
                        val adapter =
                            MazeListAdapter(this@MazeSelectionActivity, R.layout.maze_entry, mazeData)
                        listView.adapter = adapter
                    }
                } else {
                    // Handle the error
                }
            }
        })
    }
}
