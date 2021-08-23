package dev.sotoestevez.allforone.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Properties of an user
 *
 * @property id unique identifier of the user
 * @property googleId unique Google identifier of the user
 * @property role type of user
 * @property displayName of the user
 */
@Parcelize
data class User(
	@SerializedName("_id") val id: String,
	val googleId: String,
	var role: Role,
	var displayName: String?
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
