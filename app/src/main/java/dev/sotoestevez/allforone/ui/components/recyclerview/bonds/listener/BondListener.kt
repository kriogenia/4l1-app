package dev.sotoestevez.allforone.ui.components.recyclerview.bonds.listener

import dev.sotoestevez.allforone.ui.components.recyclerview.bonds.BondView
import dev.sotoestevez.allforone.vo.Address

interface BondListener {

    /**
     * Action to perform on phone number press
     *
     * @param number  Phone number to call
     */
    fun onPhoneClick(number: String)

    /**
     * Action to perform on address press
     *
     * @param address Address to display
     */
    fun onAddressClick(address: Address)

    /**
     * Action to perform on email press
     *
     * @param address   Email address
     */
    fun onEmailClick(address: String)

    /**
     * Action to perform on long press
     *
     * @param view      Bond view to manage
     */
    fun onLongPress(view: BondView)

}