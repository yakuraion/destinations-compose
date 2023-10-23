package pro.yakuraion.destinationscompose.parameters

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParcelableParameter(
    val first: Int,
    val second: String,
) : Parcelable
