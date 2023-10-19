package pro.yakuraion.destinationscompose.viewmodeltree.screen2

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import kotlinx.parcelize.Parcelize
import pro.yakuraion.destinationscompose.core.DestinationOptionalParameter
import pro.yakuraion.destinationscompose.core.NotDestinationParameter
import java.io.Serializable

class VMChildScreen2ViewModel(
    val arg1: Long,
    @DestinationOptionalParameter(defaultValue = "13f") val arg2: Float?,
    @NotDestinationParameter val arg3: String,
    val arg4: ViewModelSerializableParameter,
    val arg5: ViewModelParcelableParameter,
) : ViewModel()

data class ViewModelSerializableParameter(
    val first: Int,
    val second: Boolean?,
) : Serializable

@Parcelize
data class ViewModelParcelableParameter(
    val first: Float,
    val second: String?,
) : Parcelable
