package com.example.pokeapirecycle

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONObject

class  MainActivity : AppCompatActivity() {
    private val client = AsyncHttpClient()
    private lateinit var rvPoke: RecyclerView
    private lateinit var pokeList: MutableList<SimplePokemon>
    private lateinit var adapter: PokeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvPoke = findViewById(R.id.rvPoke)
        pokeList = mutableListOf()
        adapter = PokeAdapter(pokeList)
        rvPoke.adapter = adapter
        rvPoke.layoutManager = LinearLayoutManager(this)

        fetchPokemonList()
    }

    private fun fetchPokemonList() {

        val url = "https://pokeapi.co/api/v2/pokemon?limit=20"

        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JsonHttpResponseHandler.JSON?) {
                val results = json?.jsonObject?.getJSONArray("results") ?: return
                for (i in 0 until results.length()) {
                    val obj = results.getJSONObject(i)
                    val detailUrl = obj.getString("url")
                    fetchPokemonDetail(detailUrl)
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e("MainActivity", "List fetch failed", throwable)
            }
        })
    }

    private fun fetchPokemonDetail(url: String) {


        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JsonHttpResponseHandler.JSON?) {
                val obj: JSONObject = json?.jsonObject ?: return
                val name = obj.getString("name")
                val id = obj.getInt("id")
                val imageUrl = obj.getJSONObject("sprites").getString("front_default")
                Log.d("Pokemon", "Fetched detail for $name")
                pokeList.add(SimplePokemon(name, id, imageUrl))
                adapter.notifyItemInserted(pokeList.size - 1)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e("MainActivity", "Detail fetch failed", throwable)
            }
        })
    }
}
