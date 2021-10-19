package dev.sotoestevez.allforone.ui.components.recyclerview.notifications

import dev.sotoestevez.allforone.ui.components.recyclerview.BindedItemView

/** View of Feed components to display on BindableRecyclerView */
interface NotificationsView: BindedItemView {

    /** Types of views of feed messages*/
    enum class Type {
        /** Notification view */
        NOTIFICATION,
        /** Date */
        DATE
    }

}