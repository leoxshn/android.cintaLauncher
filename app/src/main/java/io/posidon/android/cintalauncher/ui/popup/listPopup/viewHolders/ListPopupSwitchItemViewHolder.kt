package io.posidon.android.cintalauncher.ui.popup.listPopup.viewHolders

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.*
import android.util.StateSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import io.posidon.android.cintalauncher.R
import io.posidon.android.cintalauncher.color.ColorTheme
import io.posidon.android.cintalauncher.ui.feed.items.viewHolders.applyIfNotNull
import io.posidon.android.cintalauncher.ui.popup.listPopup.ListPopupItem
import posidon.android.conveniencelib.dp

class ListPopupSwitchItemViewHolder(itemView: View) : ListPopupViewHolder(itemView) {

    val icon = itemView.findViewById<ImageView>(R.id.icon)

    val text = itemView.findViewById<TextView>(R.id.text)
    val description = itemView.findViewById<TextView>(R.id.description)

    val switch = itemView.findViewById<SwitchCompat>(R.id.toggle)

    val ripple = RippleDrawable(ColorStateList.valueOf(0), null, ColorDrawable(0xffffffff.toInt()))

    init {
        itemView.background = ripple
    }

    override fun onBind(item: ListPopupItem) {
        text.text = item.text
        description.text = item.description

        itemView.setOnClickListener {
            switch.toggle()
        }

        text.setTextColor(ColorTheme.cardTitle)
        switch.trackDrawable = generateTrackDrawable(itemView.context)
        switch.thumbDrawable = generateThumbDrawable(itemView.context)

        ripple.setColor(ColorStateList.valueOf(ColorTheme.accentColor and 0xffffff or 0x33000000))

        applyIfNotNull(description, item.description) { view, value ->
            view.text = value
            description.setTextColor(ColorTheme.cardDescription)
        }
        applyIfNotNull(icon, item.icon) { view, value ->
            view.setImageDrawable(value)
            view.imageTintList = ColorStateList.valueOf(ColorTheme.cardDescription)
        }
        switch.isChecked = (item.value as? Boolean) ?: false
        switch.setOnCheckedChangeListener(item.onToggle!!)
    }

    private fun generateTrackDrawable(context: Context): Drawable {
        val out = StateListDrawable()
        out.addState(intArrayOf(android.R.attr.state_checked), generateBG(context, ColorTheme.accentColor and 0x00ffffff or 0x55000000))
        out.addState(StateSet.WILD_CARD, generateBG(context, ColorTheme.cardHint and 0x00ffffff or 0x55000000))
        return out
    }

    private fun generateThumbDrawable(context: Context): Drawable {
        val out = StateListDrawable()
        out.addState(intArrayOf(android.R.attr.state_checked), generateCircle(context, ColorTheme.accentColor))
        out.addState(StateSet.WILD_CARD, generateCircle(context, ColorTheme.cardHint and 0x00ffffff or 0x55000000))
        return out
    }

    fun generateCircle(context: Context, color: Int): Drawable {
        val r = context.dp(18).toInt()
        val inset = context.dp(4).toInt()
        return LayerDrawable(arrayOf(
            GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(color)
                setSize(r, r)
                setStroke(1, 0x33000000)
            },
        )).apply {
            setLayerInset(0, inset, inset, inset, inset)
        }
    }

    fun generateBG(context: Context, color: Int): Drawable {
        return GradientDrawable().apply {
            cornerRadius = Float.MAX_VALUE
            setColor(color)
            setStroke(1, 0x88000000.toInt())
        }
    }
}