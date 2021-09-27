package dev.sotoestevez.allforone.ui.components.recyclerview.feed

import dev.sotoestevez.allforone.R


class DateHeaderView(val date: String) : FeedView {

    override val layoutId: Int = R.layout.content_feed_date

    override val viewType: Int = FeedView.Type.HEADER.ordinal

}