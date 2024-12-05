package pt.isec.marco.firebase.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isec.marco.firebase.ui.viewmodels.FirebaseViewModel




@Composable
fun FirestoreScreen(
    viewModel: FirebaseViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val error by remember { viewModel.error }
    val user by remember { viewModel.user }
    val nrgames by remember { viewModel.nrgames }
    val topscore by remember { viewModel.topscore }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (error != null) {
            Text(text = "Error: $error", Modifier.background(Color(255, 0, 0)))
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text("User: ${user?.email ?: ""}")
        Text("Nr Games: $nrgames")
        Text("Top Score: $topscore")

        Spacer(Modifier.height(16.dp))
        Button(onClick = { viewModel.addDataToFirestore() }) {
            Text("Add Data")
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = { viewModel.updateDataInFirestore() }) {
            Text("Update data")
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = { viewModel.removeDataFromFirestore() }) {
            Text("Remove data")
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = { viewModel.startObserver() }) {
            Text("Start observer")
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = { viewModel.stopObserver() }) {
            Text("Stop Observer")
        }
//        Spacer(Modifier.height(32.dp))
//        Button(onClick = { viewModel.uploadToStorage(context) }) {
//            Text("Upload to Storage")
//        }
    }
}