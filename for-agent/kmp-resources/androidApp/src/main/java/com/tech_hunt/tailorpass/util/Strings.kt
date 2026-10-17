package com.tech_hunt.tailorpass.util

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

/**
 * shared মডিউলের string key ("m_chest") → Android R.string।
 * key আর res/values/strings.xml-এর নাম হুবহু এক।
 * (getIdentifier ধীর নয় এমন নয় — বারবার ডাকলে ফলাফল cache করুন।)
 */
@SuppressLint("DiscouragedApi")
@StringRes
fun Context.stringId(key: String): Int =
    resources.getIdentifier(key, "string", packageName).also { require(it != 0) { "Missing string: $key" } }

@SuppressLint("DiscouragedApi")
@DrawableRes
fun Context.drawableId(name: String): Int =
    resources.getIdentifier(name, "drawable", packageName).also { require(it != 0) { "Missing drawable: $name" } }

@Composable
fun str(key: String, vararg args: Any): String = stringResource(LocalContext.current.stringId(key), *args)
