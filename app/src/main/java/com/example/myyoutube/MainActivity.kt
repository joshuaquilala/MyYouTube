package com.example.myyoutube

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.myyoutube.handlers.YoutubeHandler
import com.example.myyoutube.models.Youtube
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    lateinit var youtubeHandler: YoutubeHandler
    lateinit var titleEditText: EditText
    lateinit var linkEditText: EditText
    lateinit var rankEditText: EditText
    lateinit var reasonEditText: EditText
    lateinit var button: Button
    lateinit var youtubes: ArrayList<Youtube>
    lateinit var youtubeVideosArrayAdapter: ArrayAdapter<Youtube>
    lateinit var youtubeVideosListView: ListView
    lateinit var youtubeGettingEdited: Youtube



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Map everything
        titleEditText = findViewById(R.id.videoTitleEditText)
        linkEditText = findViewById(R.id.videoLinkEditText)
        rankEditText = findViewById(R.id.videoRankEditText)
        reasonEditText = findViewById(R.id.reasonEditText)
        button = findViewById(R.id.button)
        youtubeVideosListView = findViewById(R.id.listView)

        youtubeHandler = YoutubeHandler()
        youtubes = ArrayList()


        button.setOnClickListener{
            val title = titleEditText.text.toString()
            val link = linkEditText.text.toString()
            var rank: Int
            if (rankEditText.text.toString() == "" ) {
                rank = 0
            } else {
                rank = rankEditText.text.toString().toInt()
            }


            val reason = reasonEditText.text.toString()


            if(button.text.toString() == "Add"){
                val restaurant = Youtube(title = title , link = link ,rank =  rank, reason = reason  )
                if(youtubeHandler.create(restaurant)){
                    Toast.makeText(this, "Youtube added.", Toast.LENGTH_SHORT).show()
                }
                clear()
            } else if(button.text.toString() == "Update") {
                val restaurant = Youtube( id = youtubeGettingEdited.id, title = title , link = link ,rank =  rank, reason = reason )
                if(youtubeHandler.update(restaurant)){
                    Toast.makeText(this, "Youtube updated.", Toast.LENGTH_SHORT).show()
                }
                clear()

            }

        }

        registerForContextMenu(youtubeVideosListView)



    }


    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when (item.itemId){
            R.id.editVideo -> {
                youtubeGettingEdited = youtubes[info.position]
                titleEditText.setText(youtubeGettingEdited.title)
                linkEditText.setText(youtubeGettingEdited.link)
                rankEditText.setText(youtubeGettingEdited.rank.toString())
                reasonEditText.setText(youtubeGettingEdited.reason)
                button.setText("Update")
                return true
            }
            R.id.deleteVideo -> {
                if (youtubeHandler.delete(youtubes[info.position])){
                    Toast.makeText(this, "Youtube video updated", Toast.LENGTH_SHORT).show()
                }
                return true
            }
            else -> super.onContextItemSelected(item)


        }

    }


    override fun onStart() {
        super.onStart()
        youtubeHandler.youtubeVideosReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                youtubes.clear()
                p0.children.forEach {
                        it -> val video = it.getValue(Youtube::class.java)
                    youtubes.add(video!!)
                    youtubes.sortWith(object: Comparator<Youtube>{
                        override fun compare(o1: Youtube, o2: Youtube): Int = when {
                            o1.rank > o2.rank -> 1
                            o1.rank == o2.rank -> 0
                            else -> -1
                        }
                    })

                }
                youtubeVideosArrayAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, youtubes)
                youtubeVideosListView.adapter = youtubeVideosArrayAdapter

            }

            override fun onCancelled(p0: DatabaseError) {
                //TODO("Not yet implemented")
            }


        })

    }


    fun clear(){
        titleEditText.text.clear()
        linkEditText.text.clear()
        rankEditText.text.clear()
        reasonEditText.text.clear()
        button.setText("Add")
    }
}