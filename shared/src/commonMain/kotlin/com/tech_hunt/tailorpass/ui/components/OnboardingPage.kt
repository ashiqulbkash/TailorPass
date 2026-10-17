package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme

/**
 * One onboarding page (1.2–1.4): 280dp illustration card · optional eyebrow · 26 bold title · bullets.
 * Skip/Next and the dots stay outside the pager (OnboardingScreen).
 */
@Composable
fun OnboardingPage(
    title: String,
    modifier: Modifier = Modifier,
    eyebrow: String? = null,
    illustration: @Composable BoxScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier.verticalScroll(rememberScrollState())) {
        // Illustrations are light-palette artwork (no dark design for onboarding), so they keep the light theme.
        MaapTheme(darkTheme = false) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center,
                content = illustration,
            )
        }
        if (eyebrow != null) {
            Spacer(Modifier.height(Dimens.space20))
            Text(
                text = eyebrow,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.height(2.dp))
        } else {
            Spacer(Modifier.height(28.dp))
        }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(Dimens.space14))
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.space12), content = content)
    }
}

/** Bullet row: 32dp round marker + 17 text, 12dp apart. */
@Composable
fun OnboardingBullet(
    text: String,
    markerColor: Color,
    marker: @Composable BoxScope.() -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.space12),
    ) {
        Box(
            modifier = Modifier.size(32.dp).background(markerColor, CircleShape),
            contentAlignment = Alignment.Center,
            content = marker,
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = 24.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

/** Page dots: current = 24×8 primary pill, others 8×8 grey, 8dp apart. */
@Composable
fun PageDots(
    count: Int,
    current: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.semantics { this.contentDescription = contentDescription },
        horizontalArrangement = Arrangement.spacedBy(Dimens.space8, Alignment.CenterHorizontally),
    ) {
        repeat(count) { index ->
            Box(
                Modifier
                    .width(if (index == current) 24.dp else 8.dp)
                    .height(8.dp)
                    .background(
                        if (index == current) MaterialTheme.colorScheme.primary else MaapTheme.colors.dotInactive,
                        RoundedCornerShape(4.dp),
                    ),
            )
        }
    }
}
