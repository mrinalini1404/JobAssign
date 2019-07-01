package com.jobs.jobassign

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.SparseBooleanArray
import android.view.*
import android.view.View.*
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.jobs.jobassign.Model.Item
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.jobs.jobassign.Interface.ItemClickListener
import com.jobs.jobassign.ViewHolder.ItemViewHolder
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_login.*
import java.text.FieldPosition

class DashboardActivity : AppCompatActivity() {

    internal var items:MutableList<Item> = ArrayList()
    internal lateinit var adapter:FirebaseRecyclerAdapter<Item,ItemViewHolder>
    internal var expandState = SparseBooleanArray()

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var ref: DatabaseReference
    lateinit var jobList:MutableList<Jobs>
    lateinit var listView:ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        //Init View
        recycler_data.setHasFixedSize(true)
        recycler_data.layoutManager=LinearLayoutManager(this)

        retrieveData()

        //setData
        setdata()

    }

    private fun setdata()
    {
        val query = FirebaseDatabase.getInstance().reference.child("Jobs ")
        val options = FirebaseRecyclerOptions.Builder<Item>()
            .setQuery(query,Item::class.java)
            .build()

        adapter = object:FirebaseRecyclerAdapter<Item,ItemViewHolder>(options)
        {
            override fun getItemViewType(position: Int): Int {
                return if (items[position].isExpandable)

                    else
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
                if(viewType == 0)
                {
                    val itemView= LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.layout_with_child,parent,false)
                    return ItemViewHolder(itemView,viewType==1)
                }

                else
                {
                    val itemView= LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.layout_with_child,parent,false)
                    return ItemViewHolder(itemView,viewType==1)

                }

            }

            override fun onBindViewHolder(holder: ItemViewHolder, position: Int, model: Item) {
                when(holder.itemViewType){
                    0->
                    {

                    }
                    1->
                    {
                        holder.setIsRecyclable(false)
                        holder.txt_item_text.text=model.text

                        holder.setiItemClickListener(object : ItemClickListener{
                            
                        })
                    }
                }

            }

        }
    }

    private fun retrieveData(){
        items.clear()

        val db=FirebaseDatabase.getInstance()
            .reference
            .child("Jobs")

        db.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Log.d("Error",""+p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
               for (itemSnapShot in p0.children)
               {
                   val item=itemSnapShot.getValue(Item::class.java)

                   items.add(item!!)
               }
            }

        })
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure!")
        builder.setMessage("Do you want to close the app?")
        builder.setPositiveButton("Yes",{ dialogInterface: DialogInterface, i: Int -> finish()})
        builder.setNegativeButton("No",{ dialogInterface: DialogInterface, i: Int -> })
        builder.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var selectedOption = ""
        when(item?.itemId){
            R.id.logout -> selectedOption ="Logout"
            R.id.job_request -> selectedOption="jobs"

        }
        if(selectedOption == "Logout")
        {
            auth.signOut()
            googleSignInClient.signOut().addOnCompleteListener(this) {
                updateUI(null)
            }
        }else if(selectedOption == "jobs")
        {
            startActivity(Intent(this,JobCreateActivity::class.java))
        }

        Toast.makeText(this,"Option : " + selectedOption, Toast.LENGTH_SHORT).show()
        return super.onOptionsItemSelected(item)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
        } else {

            startActivity(Intent(this,LoginActivity::class.java))
        }
    }
}
