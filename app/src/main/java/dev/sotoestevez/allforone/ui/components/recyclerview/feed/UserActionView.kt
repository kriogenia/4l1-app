package dev.sotoestevez.allforone.ui.components.recyclerview.feed

import dev.sotoestevez.allforone.R

/**
 * View for user's action to notify in the feed
 *
 * @property template   text template
 * @property author     author of the action
 * @property subject    affected action
 */
class UserActionView(val template: Int, val author: String, val subject: String) : FeedView {

    override val id: String = author + subject + template

    override val layoutId: Int = R.layout.content_feed_user_action

    override val viewType: Int = FeedView.Type.ACTION.ordinal

}