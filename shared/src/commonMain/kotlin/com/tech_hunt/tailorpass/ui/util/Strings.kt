package com.tech_hunt.tailorpass.ui.util

import androidx.compose.runtime.Composable
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.allDrawableResources
import com.tech_hunt.tailorpass.resources.allStringResources
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Model/logic keys ("m_chest", "ic_garment_panjabi") → composeResources.
 * A key is exactly the resource name in composeResources/values/strings.xml or drawable/.
 */
fun stringRes(key: String): StringResource =
    Res.allStringResources[key] ?: error("Missing string: $key")

fun drawable(name: String): DrawableResource =
    Res.allDrawableResources[name] ?: error("Missing drawable: $name")

@Composable
fun str(key: String, vararg args: Any): String = stringResource(stringRes(key), *args)
