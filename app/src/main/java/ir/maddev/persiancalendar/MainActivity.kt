package ir.maddev.persiancalendar

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.maddev.persiancalendarview.calendar.PersianCalendarView
import com.maddev.persiancalendarview.date.DateType
import com.maddev.persiancalendarview.picker.CalendarViewStyle
import com.maddev.persiancalendarview.picker.MaterialPersianDatePicker

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<PersianCalendarView>(R.id.shamsiView).apply {
            setDateType(DateType.PERSIAN)
        }

        findViewById<PersianCalendarView>(R.id.gregorianView).apply {
            setDateType(DateType.GREGORIAN)
        }

        val calendarViewStyle = CalendarViewStyle.Builder()
            .setRectCornerRadius(16f)
            .setDaysTextSizeSp(10)
            .build()

        MaterialPersianDatePicker.Builder()
            .setCalendarViewStyle(calendarViewStyle)
            .setDateType(DateType.PERSIAN)
            .setSelectedDate(System.currentTimeMillis())
            .build().let {
                supportFragmentManager.beginTransaction().add(it, "FRAG_DATE_PICKER").commit()

                it.addOnPositiveButtonClickListener { year, month, day, timeInMillis ->
                    Toast.makeText(this, "$year/$month/$day - $timeInMillis", Toast.LENGTH_SHORT).show()
                }
                it.addOnNegativeButtonClickListener {
                    Toast.makeText(this, "on button cancel clicked", Toast.LENGTH_SHORT).show()
                }
                it.addOnCancelListener {
                    Toast.makeText(this, "on cancel", Toast.LENGTH_SHORT).show()
                }
                it.addOnDismissListener {
                    Toast.makeText(this, "on dismiss", Toast.LENGTH_SHORT).show()
                }
            }
    }
}