package com.example.mynailproject.additional

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mynailproject.R
import com.example.mynailproject.active.BasicActivity
import com.example.mynailproject.database.DBCall
import com.example.mynailproject.database.ServiceDate
import com.example.mynailproject.database.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class NotificationService(): Service() {

    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable
    private lateinit var auth: FirebaseAuth
    private var database: DatabaseReference = Firebase.database.reference
    var list = ArrayList<ServiceDate>()
    var isMaster: Boolean = false

    fun getInfo(){
        auth = Firebase.auth
        val current_user = auth.currentUser
        addUserEventListener(database, current_user?.uid!!)
        val role = DBCall.CurrentRole.getRole()
        if (role == null || role == "client") isMaster = false
        else isMaster = true
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("Service", "Created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationId: Int = 101
        val channelId = "channel-id"
        val channelName = "Channel Name"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(channelId, channelName, importance)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
        val intent = Intent(this, BasicActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        mHandler = Handler()
        var i=0
        mRunnable = Runnable {
            getInfo()
            val date_str =  convertLongToDate( list[0].date!!.toLong())
            val builder = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_baseline_star_border_24)
                    .setContentTitle("Запись в салон Nail Studio")
                    .setContentText("Запись на " + date_str + " в " + list[0].hour+":00" )
                    //.setContentText(desc + ", время: " +  Calendar.getInstance().time.toString())
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
            val db_call = DBCall()
            db_call.isNotificated(list[0].date!!)
            with(NotificationManagerCompat.from(this)) {
                notify(notificationId, builder.build()) // посылаем уведомление
            }
            mHandler.postDelayed(mRunnable, 1000*60*60)
        }
        mHandler.postDelayed(mRunnable, 1000)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Service", "Destroyed")
        mHandler.removeCallbacks(mRunnable)
    }

    private fun convertLongToDate(time: Long): String{
        val date = Date(time)
        val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return format.format(date)
    }

    fun addUserEventListener(userReference: DatabaseReference, uid:String) {
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val now = Date().time
                if (list.size > 0) list.clear()

                if (isMaster == false){

                    for (ds in dataSnapshot.child("users").child(uid).child("date").children){
                        val d: ServiceDate? = ds.getValue<ServiceDate>()
                        if (d != null) {
                            val del : Long = d.date!!.toLong() - now
                            if ( del>=1000*60*60*23 &&  del<=1000*60*60*24 && d.notificat==false) list.add(d)
                        }
                    }
                }
                else{
                    for (dss in dataSnapshot.child("users").children){
                        val k: String? = dss.key
                        val u: User? = dss.getValue<User>()
                        for (ds in dss.child("date").children) {
                            val d: ServiceDate? = ds.getValue<ServiceDate>()
                            if (d != null && d.master_uid == uid) {
                                val del : Long = d.date!!.toLong() - now
                                if (del>=0 &&  del<=1000*60*60*24 && d.notificat==false) {
                                    val new_d = ServiceDate(d.hour, d.serv_id, k!!, d.date)
                                    Log.d("HISTORY 1 ", list.size.toString())
                                    list.add(new_d)
                                }

                            }
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        userReference.addValueEventListener(userListener)
    }
}