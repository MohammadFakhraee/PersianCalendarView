package com.maddev.persiancalendarview.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.FontRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.maddev.persiancalendarview.date.AbstractDate

val Number.dp: Float get() = this.toFloat() * Resources.getSystem().displayMetrics.density
val Number.sp: Float get() = this.toFloat() * Resources.getSystem().displayMetrics.scaledDensity

@ColorInt
fun Context.resolveAttributeColor(@AttrRes attribute: Int) = TypedValue().let {
    theme.resolveAttribute(attribute, it, true)
    it.data
}
fun Context.resolveTypeface(@FontRes typeFaceId: Int) = ResourcesCompat.getFont(this, typeFaceId)
fun Context.resolveColor(@ColorRes colorId: Int) = ContextCompat.getColor(this, colorId)

fun Int.formatDay(): String = "%01d".format(this)
fun Int.formatYear(): String = "%04d".format(this)

fun AbstractDate.reInit(timeInMilliSeconds: Long): AbstractDate = apply { init(timeInMilliSeconds) }