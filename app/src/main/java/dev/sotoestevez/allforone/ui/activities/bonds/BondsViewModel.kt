package dev.sotoestevez.allforone.ui.activities.bonds

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.ui.viewmodel.ExtendedViewModel
import dev.sotoestevez.allforone.ui.viewmodel.PrivateViewModel
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.repositories.UserRepository
import dev.sotoestevez.allforone.ui.components.exchange.dialog.DeleteBondConfirmation
import dev.sotoestevez.allforone.ui.components.exchange.dialog.DeleteTaskConfirmation
import dev.sotoestevez.allforone.ui.components.exchange.dialog.DialogConfirmationRequest
import dev.sotoestevez.allforone.ui.components.recyclerview.bonds.BondView
import dev.sotoestevez.allforone.ui.components.recyclerview.tasks.TaskView
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import dev.sotoestevez.allforone.util.extensions.invoke
import dev.sotoestevez.allforone.util.extensions.logDebug
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.util.ArrayList

/** ViewModel of the Bonds Activity */
class BondsViewModel(
    savedStateHandle: SavedStateHandle,
    dispatchers: DispatcherProvider = DefaultDispatcherProvider,
    sessionRepository: SessionRepository,
    private val userRepository: UserRepository
) : PrivateViewModel(savedStateHandle, dispatchers, sessionRepository) {

    /** Current QR Code to create a bond */
    val qrCode: LiveData<String>
        get() = mQrCode
    private val mQrCode: MutableLiveData<String> = MutableLiveData("")

    /** List of bonds of the user */
    val bonds: LiveData<List<BondView>>
        get() = mBonds
    private val mList: MutableList<BondView> = mutableListOf()
    private val mBonds: MutableLiveData<List<BondView>> = MutableLiveData(mList)

    /** LiveData holding the last task changed */
    val actionTaskToConfirm: LiveData<DialogConfirmationRequest>
        get() = mActionTaskToConfirm
    private val mActionTaskToConfirm: MutableLiveData<DialogConfirmationRequest> = MutableLiveData()

    /** Mutable live data to manage the QR section loading state */
    val loadingQr: MutableLiveData<Boolean> = MutableLiveData(false)

    /** Timestamp in seconds of the QR request*/
    private var lastQRRequest: Long = 0

    @Suppress("unused") // Used in the factory with a class call
    constructor(builder: ExtendedViewModel.Builder) : this(
        builder.savedStateHandle,
        builder.dispatchers,
        builder.sessionRepository,
        builder.userRepository
    )

    init {
        loading.value = true
        // Load bonds
        viewModelScope.launch(dispatchers.io() + coroutineExceptionHandler) {
            val response = userRepository.getBonds(authHeader())
            logDebug("Retrieved ${response.size} bonds")
            withContext(dispatchers.main()) {
                loading.value = false
                mList.clear()
                mList.addAll(response.map { bond -> BondView(bond) { onRemoveBond(it)} }).also {
                    mBonds.apply { postValue(value) }
                }
            }
        }
    }

    /** Requests a new QR code if the current one is not valid */
    fun generateNewQRCode() {
        if (lastQRRequest + 50 > Instant.now().epochSecond)
            return  // Don't request a new QR if the current one is still valid
        logDebug("Requesting a new QR code")
        loadingQr.value = true
        lastQRRequest = Instant.now().epochSecond
        viewModelScope.launch(dispatchers.io() + coroutineExceptionHandler) {
            val code = userRepository.requestBondingCode(authHeader())
            Log.d(BondsViewModel::class.simpleName, "[${user.value?.id}] Generated new bonding token: ${code.substring(0, 6)}...")
            withContext(dispatchers.main()) {
                mQrCode.value = code
            }
        }
    }

    /**
     * Calls for the removal of a bond with an User
    */
    private fun onRemoveBond(bond: BondView) {
        if (this.user.value?.role == User.Role.PATIENT) {
            mActionTaskToConfirm.value = DeleteBondConfirmation(bond) { removeBond(it) }
        }
    }

    private fun removeBond(bond: BondView) {
        logDebug("Requesting the removal of the bond with ${bond.data.displayName}")
        viewModelScope.launch(dispatchers.io()) { userRepository.unbond(bond.data, authHeader()) }
        mList.remove(bond).also { mBonds.invoke() }
        logDebug("Deleted the bond with User[${bond.data.displayName}]")
    }

}