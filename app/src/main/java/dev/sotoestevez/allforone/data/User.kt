package dev.sotoestevez.allforone.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Properties of an user
 *
 * @property id Unique identifier of the user
 * @property googleId Unique Google identifier of the user
 * @property role Type of user
 * @property displayName Name of the user to display
 * @property mainPhoneNumber Main phone number of the user
 * @property altPhoneNumber Alternative phone number of the user
 * @property address Postal address of the user
 * @property email Email address of the user
 */
@Parcelize
data class User(
	@SerializedName("_id") val id: String,
	val googleId: String,
	var role: Role,
	var displayName: String? = "",
	var mainPhoneNumber: String? = "",
	var altPhoneNumber: String? = "",
	var address: Address? = null,
	var email: String? = ""
	): Parcelable {

	/** Different types of users of the application */
	enum class Role {

		/** Users that didn't select a [Role] yet */
		@SerializedName("blank") BLANK,

		/** Users helping a patient */
		@SerializedName("keeper") KEEPER,

		/** Users suffering the disease */
		@SerializedName("patient") PATIENT

	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as User

		if (id != other.id) return false

		return true
	}

	override fun hashCode(): Int {
		return id.hashCode()
	}

}
