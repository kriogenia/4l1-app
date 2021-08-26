package dev.sotoestevez.allforone.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Simplified model representing an address with the needed info to allow personal localization
 * of the user.
 *
 * @property firstLine Main info of the postal address, commonly the street name and number
 * @property secondLine Extra info of the address to complete the postal address, like the door
 * @property locality Locality of the postal address
 * @property region Region or administrative area of the locality
 */
@Parcelize
data class Address(
	val firstLine: String?,
	val secondLine: String?,
	val locality: String?,
	val region: String?
): Parcelable {

	/**
	 * Postal address of the user combining both lines
	 */
	val postalAddress: String
		get() = "$firstLine, $secondLine"

	/**
	 * String combining the locality of the user with its region
	 */
	val localityFull: String
		get() = "$locality ($region)"

}
