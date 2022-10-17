package com.example.mavtech

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mavtech.ui.home.TechDevice
import java.util.*


class ItemDetailActivity : AppCompatActivity() {

    private var device: TechDevice? = null

    private var year = 0
    private var month = 0
    private var day = 0

    private lateinit var fromDateButton: Button
    private lateinit var toDateButton: Button
    private lateinit var rentButton: Button

    private lateinit var fromDate: Date
    private lateinit var toDate: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_detail)
        createNotificationChannel()
        assert(
            supportActionBar != null //null check
        )
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        device = intent.getSerializableExtra("device") as? TechDevice
        val tv = findViewById<TextView>(R.id.deviceNameTV)
        rentButton = findViewById(R.id.rentDeviceButton)
        tv.text = device?.name

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
                alert("Error","From date cannot be after or on the same day as to date. Make sure the renting from date is before renting to date.")
            } else {
                // make updates to firebase database
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

    /*
        FUNCTIONS TO SCHEDULE NOTIFICATIONS
     */

    private fun scheduleNotification() {
        val intent = Intent(applicationContext, Notification::class.java)
        val title = device?.name
        val message = "You rented $title on ${fromDate.toString()} to ${toDate.toString()}. This item is due soon. Please return it before the due date."
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
            alert(title, message+"\nA notification will be sent to you on ${Date(time)}")
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