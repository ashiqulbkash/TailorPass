package com.tech_hunt.tailorpass.ui.screens.prep

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.ic_camera
import com.tech_hunt.tailorpass.resources.pose_distance
import com.tech_hunt.tailorpass.resources.pose_distance_hint
import com.tech_hunt.tailorpass.resources.pose_front
import com.tech_hunt.tailorpass.resources.pose_front_sub
import com.tech_hunt.tailorpass.resources.pose_open_camera
import com.tech_hunt.tailorpass.resources.pose_side
import com.tech_hunt.tailorpass.resources.pose_side_sub
import com.tech_hunt.tailorpass.resources.pose_title
import com.tech_hunt.tailorpass.resources.pose_waist_height
import com.tech_hunt.tailorpass.ui.components.MaapCard
import com.tech_hunt.tailorpass.ui.components.MaapScaffold
import com.tech_hunt.tailorpass.ui.components.PrimaryButton
import com.tech_hunt.tailorpass.ui.components.TopBar
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.theme.PillShape
import com.tech_hunt.tailorpass.ui.util.drawable
import com.tech_hunt.tailorpass.ui.util.str
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt

/** 4.3 / 4.4 Pose guide: front and side pose, phone distance, two tips, then open the camera. */
@Composable
fun PoseGuideScreen(target: MeasureTarget, onAction: (PoseGuideAction) -> Unit) {
    val poses = PrepGuide.poses(target.female)
    MaapScaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.pose_title),
                subtitle = garmentProfileSubtitle(target),
                onBack = { onAction(PoseGuideAction.Back) },
                height = 68.dp,
            )
        },
        bottomBar = {
            PrimaryButton(
                text = stringResource(Res.string.pose_open_camera),
                onClick = { onAction(PoseGuideAction.OpenCamera) },
                icon = painterResource(Res.drawable.ic_camera),
                modifier = Modifier.fillMaxWidth(),
            )
        },
        contentPadding = PaddingValues(start = Dimens.screenPadding, end = Dimens.screenPadding, top = Dimens.space6, bottom = Dimens.space12),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.space12)) {
            MaapCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(start = Dimens.space10, end = Dimens.space10, top = Dimens.space14, bottom = Dimens.space12),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(Dimens.space8)) {
                    PoseColumn(poses.front, stringResource(Res.string.pose_front), stringResource(Res.string.pose_front_sub), Modifier.weight(1f))
                    PoseColumn(poses.side, stringResource(Res.string.pose_side), stringResource(Res.string.pose_side_sub), Modifier.weight(1f))
                }
            }
            MaapCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(start = Dimens.space12, end = Dimens.space12, top = Dimens.space12, bottom = Dimens.space10),
                verticalArrangement = Arrangement.spacedBy(Dimens.space4),
            ) {
                DistanceDiagram(poses.distance)
                Text(
                    text = stringResource(Res.string.pose_distance_hint),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = MaapTheme.colors.textEmphasis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.space8)) {
                PrepGuide.poseTips(target.female).forEach { tip ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Dimens.space10),
                    ) {
                        Icon(
                            painter = painterResource(drawable(tip.iconName)),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(Dimens.iconSize),
                        )
                        Text(
                            text = str(tip.textKey),
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 22.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        }
    }
}

/** One pose: 96×232 figure, 17 bold label, 14 hint. */
@Composable
private fun PoseColumn(drawableName: String, label: String, hint: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.space6),
    ) {
        Image(
            painter = painterResource(drawable(drawableName)),
            contentDescription = null, // label and hint below describe it
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(width = 96.dp, height = 232.dp),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = hint,
            style = MaterialTheme.typography.bodySmall,
            lineHeight = 19.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

/** Design size of the distance diagram (SVG 304×122); label positions below are in these units. */
private const val DIAGRAM_W = 304f
private const val DIAGRAM_H = 122f

/**
 * Phone at waist height, person 2–3 m away. Lines come from the drawable; the two labels are text on top
 * (so they are translated): "waist height" centered at x 150 on baseline 50, the distance pill centered at (154, 88).
 */
@Composable
private fun DistanceDiagram(drawableName: String) {
    val waist = stringResource(Res.string.pose_waist_height)
    val distance = stringResource(Res.string.pose_distance)
    BoxWithConstraints(
        Modifier
            .fillMaxWidth()
            .aspectRatio(DIAGRAM_W / DIAGRAM_H),
    ) {
        val scale = maxWidth / DIAGRAM_W.dp
        Image(
            painter = painterResource(drawable(drawableName)),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize(),
        )
        Text(
            text = waist,
            style = MaterialTheme.typography.bodySmall,
            fontSize = 13.sp,
            lineHeight = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            modifier = Modifier.placeAt(150.dp * scale, 50.dp * scale, onBaseline = true),
        )
        Box(
            Modifier
                .placeAt(154.dp * scale, 88.dp * scale, onBaseline = false)
                .widthIn(min = 100.dp)
                .heightIn(min = 26.dp)
                .background(MaterialTheme.colorScheme.surface, PillShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, PillShape)
                .padding(horizontal = Dimens.space10),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = distance,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 15.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
            )
        }
    }
}

/** Places the element horizontally centered on [x]; vertically its first baseline ([onBaseline]) or its center on [y]. */
private fun Modifier.placeAt(x: Dp, y: Dp, onBaseline: Boolean): Modifier = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints.copy(minWidth = 0, minHeight = 0))
    val anchorY = if (onBaseline) placeable[FirstBaseline].takeIf { it > 0 } ?: placeable.height else placeable.height / 2
    layout(constraints.maxWidth, constraints.maxHeight) {
        placeable.place(x.toPx().roundToInt() - placeable.width / 2, y.toPx().roundToInt() - anchorY)
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun PoseGuideScreenPreview() {
    MaapTheme(darkTheme = false) { PoseGuideScreen(MeasureTarget.SampleRahat) {} }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun PoseGuideFemalePreview() {
    MaapTheme(darkTheme = false) { PoseGuideScreen(MeasureTarget.SampleFarhana) {} }
}
