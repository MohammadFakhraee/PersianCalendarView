[![](https://jitpack.io/v/MohammadFakhraee/PersianCalendarView.svg)](https://jitpack.io/#MohammadFakhraee/PersianCalendarView)

# PersianCalendarView

An android date picker for Persian people. Great for those who containing Jalali (Shamsi) calendar in their app. It is created accorfing to
android's original CalendarView but it **does not** override it.

![LighCalendar](https://user-images.githubusercontent.com/52785844/208228358-d8009619-1f89-4281-bce8-d590e92e9eda.PNG)
![NightCalendar](https://user-images.githubusercontent.com/52785844/208228243-9ef80f50-c41b-4816-8110-b43c288dd184.PNG)

This view has some features such as:

* Handling infinite loop for calendar.
* Containing both Jalali (Shamsi) and Gregorian calendars.
* Being compatible with application's theme color (both light-night theme).

## Gradle

**Step 1.** Add the Jitpack repository to your project `build.gradle` file

```gradle
allprojects {
   repositories {
    ...
    maven { url 'https://jitpack.io' }
   }
}
```

**Step 2.** Add the dependency

```gradle
dependencies {
   implementation "com.github.MohammadFakhraee:PersianCalendarView:$latest_version"
}
```

## Usage

### 1. Using view inside layout

```xml

<com.maddev.persiancalendarview.calendar.PersianCalendarView android:id="@+id/shamsiView" android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

#### Xml customization

There are some attributes you can set via xml file.

```xml
<com.maddev.persiancalendarview.calendar.PersianCalendarView
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/shamsiView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:rectPadding="2dp"
        app:rectCornerRadius="8dp"
        app:daysTextSize="14sp"
        app:highlightDayColor="@color/teal_200"
        app:onHighlightDayColor="@color/teal_700"
        app:dayOfMonthColor="@color/black"
        app:dayOutOfMonthColor="@color/gray"
        />                 
```

#### Kotlin customization

All of the xml attributes can be changed with kotlin codes.

```kotlin
        findViewById<PersianCalendarView>(R.id.shamsiView).apply {
            setDateType(DateType.PERSIAN)        // Easily change the calendar type you wanna show (DateType.PERSIAN, DateType.GREGORIAN)
            setDaysPadding(2f)                   // Add padding to days rectangle
            setRectCornerRadius(8f)              // Change rectangle corner radius
            setDaysTextSize(14f)                 // Option 1. change text size with float
            setDaysTextDp(8)                     // Option 2. change text size with dp
            setDaysTextSp(8)                     // Option 3. change text size with sp
            // Option 1. Change colors individually
            setHighlightDayColor(Color.RED)      // Color of the rectangle and today's text color
            setOnHighlightDayColor(Color.WHITE)  // Color of text above the rectangle
            setDayOfMonthColor(Color.BLACK)      // Color for days within the shown month
            setDayOutOfMonthColor(Color.GRAY)    // Color for days outside of the shown month
            // Option 2. Change color palette in one call
            setColorPalette(
                highlightDay = Color.RED,
                onHighlightDay = Color.WHITE,
                dayOfMonth = Color.BLACK,
                dayOutOfMonth = Color.GRAY
            )
            // Set a date as selected. Will select today if this method is not called
            selectDate(
                timeInMilliSecond = desiredDateInMilliSecond,
                animation = true
            )
            // Set first day of week manually
            setFirstDayOfWeek(AbstractDate.DayOfWeek.SUNDAY.position)
        }
```

**CAUTION!** be careful to support dark/light theme when changing default colors.

**Note:** additional attributes will be added to the view.

### 2. Calendar View Listener

You can easily set user's selected day listener by calling `setOnDaySelectedListener` method.

```kotlin
        findViewById<PersianCalendarView>(R.id.shamsiView).apply {
            setOnDaySelectedListener { 
                    year,        // User selected year (in selected DateType)
                    month,       // User selected month (in selected DateType)
                    day,         // User selected day (in selected DateType)
                    timeMillis   // User selected date in milli seconds
                ->
            }
        }
```

### 3. Using dialog date picker

In order to use date picker in the form of dialog, instead of in-layout view, you can do as below:

```kotlin

// 1. (optional) you can set customized attributes to PersianCalendarView by creating a CalendarViewStyle instance in a builder pattern:
val calendarViewStyle = CalendarViewStyle.Builder()
    .setRectCornerRadius(16f)
    .setDaysTextSizeSp(10)
    .build()

// 2. Easily create a MaterialPersianDatePicker dialog with its own builder class as below:
val materialPersianCalendarDatePicker = MaterialPersianDatePicker.Builder()
    .setCalendarViewStyle(calendarViewStyle)
    .setDateType(DateType.PERSIAN)
    .setSelectedDate(System.currentTimeMillis())
    .build()
```

## Todo

This view needs to be extended and support following features:

1. Another calendar types such as ARABIC (GHAMARI)
2. Add holidays to calendar
3. Multiple days selection

## License

    Copyright 2022 Mohammad H. Fakhraee

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
