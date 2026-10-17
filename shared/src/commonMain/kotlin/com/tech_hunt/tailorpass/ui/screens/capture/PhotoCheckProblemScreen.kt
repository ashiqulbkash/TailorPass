package com.tech_hunt.tailorpass.ui.screens.capture

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.action_retake
import com.tech_hunt.tailorpass.resources.ic_refresh
import com.tech_hunt.tailorpass.resources.ic_warning
import com.tech_hunt.tailorpass.resources.photo_check_title
import com.tech_hunt.tailorpass.ui.components.LineStatus
import com.tech_hunt.tailorpass.ui.components.MaapScaffold
import com.tech_hunt.tailorpass.ui.components.PrimaryButton
import com.tech_hunt.tailorpass.ui.components.StatusLine
import com.tech_hunt.tailorpass.ui.components.TopBar
import com.tech_hunt.tailorpass.ui.diagram.BodyFigure
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.util.str
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** 6.2 A photo was rejected before measuring: what is wrong, which checks passed, retake. */
@Composable
fun PhotoCheckProblemScreen(state: PhotoProblemState, onAction: (PhotoProblemAction) -> Unit) {
    MaapScaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.photo_check_title),
                subtitle = str(state.failedShot.stepKey),
                onBack = { onAction(PhotoProblemAction.Back) },
                height = 68.dp,
            )
        },
        bottomBar = {
            PrimaryButton(
                text = stringResource(Res.string.action_retake),
                onClick = { onAction(PhotoProblemAction.Retake) },
                icon = painterResource(Res.drawable.ic_refresh),
                iconSize = 22.dp,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        contentPadding = PaddingValues(start = Dimens.screenPadding, end = Dimens.screenPadding, top = Dimens.space6, bottom = Dimens.space12),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.space12)) {
            RejectedPhoto(state)
            ProblemCard(state.problem)
            Column(
                modifier = Modifier.padding(horizontal = Dimens.space4),
                verticalArrangement = Arrangement.spacedBy(Dimens.space8),
            ) {
                state.checks.forEach { check ->
                    StatusLine(text = str(check.labelKey), status = if (check.ok) LineStatus.Done else LineStatus.Failed)
                }
            }
        }
    }
}

private val PhotoShape = RoundedCornerShape(16.dp)

/** The rejected photo (design phase: a dark silhouette) with an orange border and the problem tag. */
@Composable
private fun RejectedPhoto(state: PhotoProblemState) {
    val colors = MaapTheme.colors
    val tag = str(state.problem.tagKey)
    Box(
        Modifier
            .fillMaxWidth()
            .height(270.dp)
            .clip(PhotoShape)
            .background(colors.darkPhoto)
            .border(3.dp, colors.problem, PhotoShape)
            .semantics {
                role = Role.Image
                contentDescription = tag
            },
        contentAlignment = Alignment.Center,
    ) {
        BodyFigure(
            outline = state.target.outline(state.failedShot),
            modifier = Modifier.size(width = 92.dp, height = 238.dp),
            fill = colors.darkPhotoBody,
            stroke = colors.darkPhotoEdge,
            strokeWidth = 4f,
        )
        Box(
            Modifier
                .align(Alignment.TopStart)
                .padding(10.dp)
                .background(colors.problem, RoundedCornerShape(12.dp))
                .padding(horizontal = Dimens.space10, vertical = 2.dp)
                .clearAndSetSemantics {},
        ) {
            Text(
                text = tag,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = MaapTheme.colors.onWarning,
            )
        }
    }
}

/** Orange alert card: warning icon, what is wrong, how to fix it (only on 6.2). */
@Composable
private fun ProblemCard(problem: PhotoProblem) {
    val colors = MaapTheme.colors
    val shape = MaterialTheme.shapes.large
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.warningContainer, shape)
            .border(1.dp, colors.warningBorder, shape)
            .padding(Dimens.space14)
            .semantics { liveRegion = LiveRegionMode.Assertive },
        horizontalArrangement = Arrangement.spacedBy(Dimens.space12),
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_warning),
            contentDescription = null,
            tint = colors.warning,
            modifier = Modifier.size(28.dp),
        )
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.space4)) {
            Text(
                text = str(problem.titleKey),
                style = MaterialTheme.typography.titleMedium,
                fontSize = 19.sp,
                lineHeight = 25.sp,
                color = colors.onWarningContainer,
            )
            Text(
                text = str(problem.bodyKey),
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 23.sp,
                color = colors.onWarningContainerBody,
            )
        }
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun PhotoCheckProblemPreview() {
    MaapTheme(darkTheme = false) { PhotoCheckProblemScreen(PhotoProblemState.Sample) {} }
}
