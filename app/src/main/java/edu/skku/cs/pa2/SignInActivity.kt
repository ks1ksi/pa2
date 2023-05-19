package edu.skku.cs.pa2

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val signInButton = findViewById<Button>(R.id.signin_button)
        val usernameEditText = findViewById<EditText>(R.id.username)

        signInButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            if (username.isEmpty()) {
                Toast.makeText(
                    this@SignInActivity, "Please enter your username", Toast.LENGTH_SHORT
                ).show()
            } else {
                val client = OkHttpClient()
                val url = "http://swui.skku.edu:1399/users"
                val requestBody = JSONObject().put("username", username).toString()
                    .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                val request = Request.Builder().url(url).post(requestBody).build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: java.io.IOException) {
                        Log.e("SignInActivity", "Failed to connect to the server", e)
                        runOnUiThread {
                            Toast.makeText(
                                this@SignInActivity,
                                "Failed to connect to the server",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val responseJson = JSONObject(response.body!!.string())
                        val result = responseJson.getBoolean("success")
                        if (result) {
                            runOnUiThread {
                                Toast.makeText(
                                    this@SignInActivity,
                                    "Successfully signed in",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            val intent = android.content.Intent(
                                this@SignInActivity, MazeSelectionActivity::class.java
                            )
                            intent.putExtra("username", username)
                            startActivity(intent)
                        } else {
                            runOnUiThread {
                                Toast.makeText(
                                    this@SignInActivity, "Wrong User Name", Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                })
            }
        }
    }
}
