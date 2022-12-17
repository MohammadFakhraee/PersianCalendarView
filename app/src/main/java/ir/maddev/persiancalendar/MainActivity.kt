package ir.maddev.persiancalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.maddev.persiancalendarview.calendar.PersianCalendarView
import com.maddev.persiancalendarview.date.DateType

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
    }
}