package windows

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import components.TabButtonColors
import components.TabButtonLocation
import components.tabButton
import data.Colors
import state.GitDownState
import tabs.Tab
import views.CommitView
import views.MapView
import views.StashView
import java.awt.Dimension

@Preview
@Composable
fun GitDown() {

    org.eclipse.jgit.lib.Repository.getGlobalListenerList().addIndexChangedListener {
        GitDownState.test.value += 1
        println("Changed @ addIndexChangedListener")
    }
    org.eclipse.jgit.lib.Repository.getGlobalListenerList().addConfigChangedListener {
        GitDownState.test.value += 1
        println("Changed @ addConfigChangedListener")
    }
    org.eclipse.jgit.lib.Repository.getGlobalListenerList().addRefsChangedListener {
        GitDownState.test.value += 1
        println("Changed @ addRefsChangedListener")
    }

    GitDownState.config.value.addChangeListener {
        GitDownState.test.value += 1
        println("Changed @ addRefsChangedListener")
    }

    Window(
        onCloseRequest = { GitDownState.gitDirectory.value = "" },
        title = GitDownState.projectName.value,
        icon = painterResource(resourcePath = "icons/icon.png"),
    ) {

    this.window.minimumSize = Dimension(800, 500)

        CompositionLocalProvider(
            LocalScrollbarStyle provides ScrollbarStyle(
                minimalHeight = 16.dp,
                thickness = 8.dp,
                shape = MaterialTheme.shapes.small,
                hoverDurationMillis = 300,
                unhoverColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
                hoverColor = MaterialTheme.colors.onSurface.copy(alpha = 0.50f)
            )
        ) {
            Column(modifier = Modifier.fillMaxSize().background(color = data.Colors.DarkGrayBackground)) {
                Row(
                    modifier = Modifier.requiredHeight(48.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Row(modifier = Modifier.padding(10.dp)) {

                            tabButton(
                                TabButtonLocation.Left,
                                Tab.Map,
                                "icons/map.png",
                                "icons/map_white.png",
                                "Shows a map of commit history across branches."
                            )

                            tabButton(
                                TabButtonLocation.Middle,
                                Tab.Commit,
                                "icons/commit.png",
                                "icons/commit_white.png",
                                "Allows you to view and commit changes to the repository."
                            )

                            tabButton(
                                TabButtonLocation.Right,
                                Tab.Stash,
                                "icons/stash.png",
                                "icons/stash_white.png",
                                "Allows you to manage stashes.",
                            )

                        }
                        Column() {
                            Text(
                                "${GitDownState.projectName.value} — Commit",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "${GitDownState.commitCount.value} commits",
                                color = Colors.LightGrayText,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(0.dp, 3.dp, 0.dp, 0.dp)
                            )
                        }
                    }
                }
                Column {
                    when (GitDownState.currentTab.value) {
                        Tab.Commit -> CommitView()
                        Tab.Map -> MapView()
                        Tab.Stash -> StashView()
                    }
                }
            }
        }
    }
}
