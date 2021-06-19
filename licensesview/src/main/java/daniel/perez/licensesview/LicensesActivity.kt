package daniel.perez.licensesview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import daniel.perez.core.compose.Header
import daniel.perez.licensesview.ui.theme.EvenceTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LicensesActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val viewModel: LicensesViewModel by viewModels()
        setContent {
            LicensesScreen(object: LicensesData {
                override fun getLicenses(): Flow<List<License>> = viewModel.licenses()
            })
        }
    }
}

@Composable
fun LicensesScreen(licensesData: LicensesData)
{
    val licenses = licensesData.getLicenses().collectAsState(initial = emptyList())

    EvenceTheme {
        Surface(color = MaterialTheme.colors.background) {
            Scaffold(
                topBar = { Header(text = "Evence") }
            ) {
                Column {
                    MessageCard()
                    LicensesList(
                        licenses = listOf(
                            License("Example Project", "GPL"),
                            License("Example Project", "GPL"),
                            License("Example Project", "GPL"),
                            License("Example Project", "GPL"),
                            License("Example Project", "GPL"),
                            License("Example Project", "GPL"),
                            License("Example Project", "GPL")
                        ),
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun LicensesScreenPreview()
{
    LicensesScreen( object: LicensesData {
        override fun getLicenses(): Flow<List<License>>
        {
            return flow {
                val list = listOf(
                    License("Example Project", "GPL"),
                    License("Example Project", "GPL"),
                    License("Example Project", "GPL"),
                    License("Example Project", "GPL"),
                    License("Example Project", "GPL"),
                    License("Example Project", "GPL"),
                    License("Example Project", "GPL")
                )
                emit(list)
            }
        }
    })
}

@Composable
fun MessageCard()
{
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Evence Loves Open Source",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "This application is built with the following open source projects.",
            modifier = Modifier.fillMaxWidth(0.8f),
            maxLines = 2,
            overflow = TextOverflow.Visible
        )
    }
}

@Composable
fun LicensesList(
    licenses: List<License>,
    modifier: Modifier = Modifier
)
{
    LazyColumn(
        modifier = modifier
    ) {
        items(licenses) { license ->
            LicenseItem(license = license)
        }
    }
}

@Composable
fun LicenseItem(license: License)
{
    ConstraintLayout(
        modifier = Modifier.fillMaxWidth()
    ) {
        val (projectName, licenseConstraint) = createRefs()

        Text(
            text = license.projectName,
            modifier = Modifier.constrainAs(projectName) {
                start.linkTo(parent.start, margin = 16.dp)
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Divider()

        Text(
            text = license.licenseType,
            modifier = Modifier.constrainAs(licenseConstraint) {
                end.linkTo(parent.end, margin = 16.dp)
            }
        )
    }
}