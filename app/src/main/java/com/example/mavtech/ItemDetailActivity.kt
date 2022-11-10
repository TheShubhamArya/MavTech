package com.example.mavtech

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.util.*
import java.time.format.DateTimeFormatter


class ItemDetailActivity : AppCompatActivity() {

    private var device: RentalItem? = null

    private var year = 0
    private var month = 0
    private var day = 0

    private lateinit var fromDateButton: Button
    private lateinit var toDateButton: Button
    private lateinit var rentButton: Button

    private lateinit var fromDate: Date
    private lateinit var toDate: Date

    private lateinit var dbref: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var currentUser : FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_detail)
        dbref = FirebaseDatabase.getInstance().getReference("RentalItem")
        firebaseAuth = Firebase.auth
        currentUser = firebaseAuth.currentUser!!

        createNotificationChannel()
        assert(
            supportActionBar != null //null check
        )
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        device = intent.getSerializableExtra("device") as? RentalItem
//        Toast.makeText(this, "${device?.id}", Toast.LENGTH_SHORT).show()
        val deviceNameTV = findViewById<TextView>(R.id.deviceNameTV)
        val deviceDescriptionTV = findViewById<TextView>(R.id.deviceDetailTV)
        rentButton = findViewById(R.id.rentDeviceButton)
        deviceNameTV.text = device?.name
        deviceDescriptionTV.text = device?.description

        val deviceImageView = findViewById<ImageView>(R.id.deviceImageView)
        deviceImageView.setImageResource(R.drawable.ic_devices)

        // Initialize date for buttons as current date.
        val calendarInstance = Calendar.getInstance()
        day = calendarInstance.get(Calendar.DATE)
        month = calendarInstance.get(Calendar.MONTH)
        year = calendarInstance.get(Calendar.YEAR)

        fromDate = getTime(year, month, day)
        toDate = getTime(year, month, day)

        fromDateButton = findViewById(R.id.fromDateButton)
        toDateButton = findViewById(R.id.toDateButton)

        showDate(year, month, day, fromDateButton)
        showDate(year, month, day, toDateButton)

        fromDateButton.setOnClickListener {
            DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    showDate(year, monthOfYear, dayOfMonth, fromDateButton)
                    fromDate = getTime(year, monthOfYear, dayOfMonth)
                },
                year,
                month,
                day
            ).show()
        }

        toDateButton.setOnClickListener {
            DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    showDate(year, monthOfYear, dayOfMonth, toDateButton)
                    toDate = getTime(year, monthOfYear, dayOfMonth)
                },
                year,
                month,
                day
            ).show()
        }

        rentButton.setOnClickListener {
            // check if the dates make sense
            if (fromDate >= toDate) {
                alert(
                    "Error",
                    "From date cannot be after or on the same day as to date. Make sure the renting from date is before renting to date."
                )
            } else {
                // check if the another user has not taken the item since
                // make updates to firebase database
                saveData()
                // schedule a notification for the time it is supposed to be rented
                scheduleNotification()
                // give users a alert that they have rented the item and exit the screen
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun showDate(year: Int, month: Int, day: Int, button: Button) {
        val dateString = "${month + 1}/${day}/${year}"
        button.setText(dateString)
    }

    private fun alert(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }

    private fun updateUserData() {
        // add rentalItem ID to currently rented [Strings] and rental History[(String,fromDate,toDate)]
        dbref = FirebaseDatabase.getInstance().getReference("User").child(currentUser.uid)
        dbref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                var currentRentals = snapshot.child("currentRentals")
                var currentRentalString = currentRentals.value
                currentRentalString = StringBuilder().append(currentRentalString).append(", ${device?.id}").toString()
                val currentlyRented = CurrentlyRented(
                    id = device?.id,
                    dateFrom = fromDate.toString(),
                    dateTo = toDate.toString(),
                    device = device
                )

                device?.id?.let {
                    dbref.child("CurrentlyRented").push().setValue(currentlyRented)
                        .addOnCompleteListener{
                            Toast.makeText(this@ItemDetailActivity,"User updated",Toast.LENGTH_SHORT).show()
                        }
                }
                device?.id?.let {
                    dbref.child("AllRented").push().setValue(currentlyRented)
                        .addOnCompleteListener{
                            Toast.makeText(this@ItemDetailActivity,"User updated",Toast.LENGTH_SHORT).show()
                        }
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun updateRentalItemData() {
        // assign rentalFrom and rentalTo dates and rentedByUser field
        var updateDevice = device
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        updateDevice?.rentedFromDate = fromDate.toString()
        updateDevice?.rentedToDate = toDate.toString()
        updateDevice?.rentedByUserID = currentUser.uid
        updateDevice?.id?.let { dbref.child(it).setValue(updateDevice) }
            ?.addOnCompleteListener{
                Toast.makeText(this@ItemDetailActivity,"Item updated",Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveData() {
        updateRentalItemData()
        updateUserData()
    }

    /*
        FUNCTIONS TO SCHEDULE NOTIFICATIONS
     */

    private fun scheduleNotification() {
        val intent = Intent(applicationContext, Notification::class.java)
        val title = device?.name
        val message =
            "You rented $title on ${fromDate.toString()} to ${toDate.toString()}. This item is due soon. " +
                    "Please return it before the due date."
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val notificationTime = Calendar.getInstance()
        notificationTime.set(toDate.getYear(), toDate.getMonth(), toDate.date, 12, 0)

        var time = notificationTime.timeInMillis
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        if (title != null) {
            alert(title, message + "\nA notification will be sent to you on ${Date(time)}")
        }
    }

    private fun getTime(year: Int, month: Int, day: Int, hour: Int = 12, min: Int = 0): Date {
        val calendar = android.icu.util.Calendar.getInstance()
        calendar.set(year, month, day, hour, min)
        return calendar.time
    }

    private fun createNotificationChannel() {
        val name = "Notify Channel"
        val desc = "A description of the channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}
