package dev.sotoestevez.allforone.ui.activities.bonds

import android.content.Intent
import android.net.Uri

/**
 * Information to the construction of bond action intent
 */
sealed class ActionIntent(
    private val action: String,
    private val data: Uri,
    private val extras: Map<String, Array<String>> = mapOf()
) {

    fun build(): Intent {
        val intent = Intent(action)
        intent.data = data
        extras.forEach(intent::putExtra)
        return intent
    }

}

/** Intent to make a call to the specified number */
class CallIntent(number: String): ActionIntent(
    Intent.ACTION_DIAL,
    Uri.parse("tel:$number")
)

/** Intent to show on Maps the specified Address */
class AddressIntent(address: String): ActionIntent(
    Intent.ACTION_VIEW,
    Uri.parse("geo:0,0?q=${Uri.encode(address)}")
)

/** Intent to send an email to the specified address */
class EmailIntent(address: String): ActionIntent(
    Intent.ACTION_SENDTO,
    Uri.parse("mailto:"),
    mapOf(Intent.EXTRA_EMAIL to arrayOf(address))
)