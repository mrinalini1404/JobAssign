package com.jobs.jobassign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_job_create.*


class JobCreateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_create)

        job_btn.setOnClickListener{
            saveData()
        }
    }

    private fun saveData(){
        val nameOfcompany=company_name.text.toString().trim()
        val numWorkers=num_workers.text.toString().trim()
        val geoLoc=geo_loc.text.toString().trim()
        val pay=payment.text.toString().trim()
        val dur=duration.text.toString().trim()
        val add=address.text.toString().trim()
        val start=start_time.text.toString().trim()
        val end=end_time.text.toString().trim()
        val selec = selection.text.toString().trim()

        if(nameOfcompany.isEmpty()){
            company_name.error="Please enter Company name"
            return
        }
        if(selec.isEmpty()){
            selection.error="'Please enter selection"
            return
        }
         if(add.isEmpty()){
            address.error="'Please enter address"
            return
        }
        if(num_workers.text.toString().isEmpty()){
            num_workers.error="'Please enter Company name"
            return
        }
        if(geo_loc.text.toString().isEmpty()){
            geo_loc.error="'Please enter Company name"
            return
        }
        if(payment.text.toString().isEmpty()){
            payment.error="'Please enter Company name"
            return
        }
        if(duration.text.toString().isEmpty()){
            duration.error="'Please enter Company name"
            return
        }
        if(start_time.text.toString().isEmpty()){
            start_time.error="'Please enter Company name"
            return
        }
        if(end_time.text.toString().isEmpty()){
            end_time.error="'Please enter Company name"
            return
        }


        val myDatabase = FirebaseDatabase.getInstance().getReference("Jobs")
        val jobsId = myDatabase.push().key.toString()
        val job = Jobs(jobsId,nameOfcompany,numWorkers,geoLoc,pay,dur,add,start,end,selec)
        myDatabase.child(jobsId).setValue(job).addOnCompleteListener {
            Toast.makeText(this,"Saved",Toast.LENGTH_LONG).show()
        }


    }
}
