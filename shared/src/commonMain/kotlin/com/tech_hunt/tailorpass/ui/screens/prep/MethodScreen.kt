package com.tech_hunt.tailorpass.ui.screens.prep

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.garment_profile_subtitle
import com.tech_hunt.tailorpass.resources.ic_camera_large
import com.tech_hunt.tailorpass.resources.ic_info
import com.tech_hunt.tailorpass.resources.ic_tape
import com.tech_hunt.tailorpass.resources.method_ai_desc
import com.tech_hunt.tailorpass.resources.method_ai_tag
import com.tech_hunt.tailorpass.resources.method_ai_title
import com.tech_hunt.tailorpass.resources.method_choose
import com.tech_hunt.tailorpass.resources.method_hint
import com.tech_hunt.tailorpass.resources.method_tape_desc
import com.tech_hunt.tailorpass.resources.method_tape_tag
import com.tech_hunt.tailorpass.resources.method_tape_title
import com.tech_hunt.tailorpass.resources.method_title
import com.tech_hunt.tailorpass.ui.components.MaapScaffold
import com.tech_hunt.tailorpass.ui.components.MethodCard
import com.tech_hunt.tailorpass.ui.components.TopBar
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.util.str
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** 3.1 Choose method: photo (AI, recommended) or tape. */
@Composable
fun MethodScreen(target: MeasureTarget, onAction: (MethodAction) -> Unit) {
    MaapScaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.method_title),
                subtitle = garmentProfileSubtitle(target),
                onBack = { onAction(MethodAction.Back) },
                height = 68.dp,
            )
        },
        contentPadding = PaddingValues(start = Dimens.screenPadding, end = Dimens.screenPadding, top = Dimens.space12, bottom = Dimens.space24),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.space16)) {
            MethodCard(
                icon = painterResource(Res.drawable.ic_camera_large),
                tag = stringResource(Res.string.method_ai_tag),
                title = stringResource(Res.string.method_ai_title),
                description = stringResource(Res.string.method_ai_desc),
                actionText = stringResource(Res.string.method_choose),
                onClick = { onAction(MethodAction.ChooseAi) },
                highlighted = true,
            )
            MethodCard(
                icon = painterResource(Res.drawable.ic_tape),
                tag = stringResource(Res.string.method_tape_tag),
                title = stringResource(Res.string.method_tape_title),
                description = stringResource(Res.string.method_tape_desc),
                actionText = stringResource(Res.string.method_choose),
                onClick = { onAction(MethodAction.ChooseTape) },
                iconSize = 38.dp,
            )
            Row(
                modifier = Modifier.padding(start = Dimens.space4, end = Dimens.space4, top = Dimens.space4),
                horizontalArrangement = Arrangement.spacedBy(Dimens.space8),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_info),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp).size(20.dp),
                )
                Text(
                    text = stringResource(Res.string.method_hint),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

/** "পাঞ্জাবি · রাহাত" — top-bar subtitle of every measuring-flow screen (3.1, 4.1–4.4). */
@Composable
internal fun garmentProfileSubtitle(target: MeasureTarget): String =
    stringResource(Res.string.garment_profile_subtitle, str(target.garment.labelKey), target.profileName)

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun MethodScreenPreview() {
    MaapTheme(darkTheme = false) { MethodScreen(MeasureTarget.SampleRahat) {} }
}
