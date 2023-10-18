package pro.yakuraion.treecomposenavigation.viewmodeltree.screen2

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import kotlinx.parcelize.Parcelize
import java.io.Serializable

class VMChildScreen2ViewModel(
    val arg1: Long,
    val arg2: Float?,
    val arg3: Char,
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
