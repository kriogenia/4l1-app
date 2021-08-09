package dev.sotoestevez.allforone.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Properties of an user
 *
 * @property id unique identifier of the user
 * @property googleId unique Google identifier of the user
 * @property role type of user
 * @property displayName of the user
 */
class User(
	@SerializedName("_id") val id: String,
	val googleId: String,
	val role: Role,
	val displayName: String?
): Parcelable {

	/**
	 * Different types of users of the application
	 */
	enum class Role() {

		/**
		 * Users that didn't select a [Role] yet
		 */
		@SerializedName("blank") BLANK,

		/**
		 * Users helping a patient
		 */
		@SerializedName("keeper") KEEPER,

		/**
		 * Users suffering the disease
		 */
		@SerializedName("patient") PATIENT

	}

	constructor(parcel: Parcel) : this(
		parcel.readString()!!,
		parcel.readString()!!,
		Role.valueOf(parcel.readString()!!),
		parcel.readString()
	)

	/**
	 * Creates a Parcel of the User
	 */
	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(id)
		parcel.writeString(googleId)
		parcel.writeString(role.name)
		parcel.writeString(displayName)
	}

	/**
	 * Overriding of describe Contents function
	 */
	override fun describeContents(): Int = 0

	/**
	 * Parcelable CREATOR constant
	 */
	companion object CREATOR : Parcelable.Creator<User> {

		/**
		 * Overrides builder method to create from Parcel
		 *
		 * @param parcel to deserialize
		 * @return deserialized user
		 */
		override fun createFromParcel(parcel: Parcel): User = User(parcel)

		/**
		 * Override of the User array builder
		 *
		 * @param size of the array
		 * @return array of null users to fill
		 */
		override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)

	}

}
